package com.veragg.website.crawler.mapping;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.crawler.model.HanmarkAuctionDTO;
import com.veragg.website.domain.Address;
import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionStatus;
import com.veragg.website.domain.BuyLimit;
import com.veragg.website.domain.Court;
import com.veragg.website.domain.Document;
import com.veragg.website.domain.DocumentType;
import com.veragg.website.domain.PropertyType;
import com.veragg.website.services.CourtService;

import static java.util.Objects.nonNull;

@Service
public class HanmarkAuctionMapperServiceImpl implements AuctionMapperService<HanmarkAuctionDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HanmarkAuctionMapperServiceImpl.class);

    private static final String DATE_REGEX = "([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d [012]{0,1}[0-9]:[0-6][0-9]";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder().appendPattern("dd.MM.yyyy HH:mm").toFormatter().withZone(ZoneId.of("Europe/Berlin"));

    private final CourtService courtService;
    private final NormalizationService normalizationService;

    @Autowired
    public HanmarkAuctionMapperServiceImpl(final CourtService courtService, NormalizationService normalizationService) {
        this.courtService = courtService;
        this.normalizationService = normalizationService;
    }

    @Override
    public List<Auction> map(final HanmarkAuctionDTO auctionDTO) throws ParseException {

        Address address = AddressHelper.getAddress(auctionDTO.getStreetAddress(), auctionDTO.getCityAddress());
        String courtName = normalizationService.normalizeCity(auctionDTO.getCourtName());
        Court court = courtService.findBy(courtName, address.getZipCode());
        String expertDescription = auctionDTO.getExpertDescription();
        Set<PropertyType> propertyTypes = getPropertyTypes(auctionDTO.getPropertyTypeName(), expertDescription);

        //@formatter:off
        Auction auction = Auction.builder()
                .court(court)
                .address(address)
                .propertyTypes(propertyTypes)
                .appointment(getAppointmentDate(auctionDTO.getAppointmentDate()))
                .fileNumber(auctionDTO.getFileNumber())
                .buyLimit(getLimit(auctionDTO.getLimitDescription()))
                .amount(Integer.parseInt(normalizationService.normalizeAmount(auctionDTO.getAmount())))
                .expertiseDescription(expertDescription)
                .propertyBuildingDescription(auctionDTO.getBuildingDescription())
                .outdoorDescription(auctionDTO.getOutdoorDescription())
                .propertyPlotDescription(auctionDTO.getPlotDescription())
                .sourceUrl(auctionDTO.getSourceUrl())
                .auctionStatus(AuctionStatus.DRAFT)
                .build();
        //@formatter:on

        List<Document> documents = new ArrayList<>();
        auctionDTO.getImageLinks().forEach(imageUrl -> documents.add(new Document(imageUrl, DocumentType.IMAGE, auctionDTO.getImageLinks().indexOf(imageUrl) + 1)));
        auctionDTO.getExpertiseLinks().forEach(expertiseUrl -> documents.add(new Document(expertiseUrl, DocumentType.EXPERTISE)));
        auctionDTO.getOtherDocumentLinks().forEach(otherDocumentUrl -> documents.add(new Document(otherDocumentUrl, DocumentType.OTHER)));
        documents.forEach(document -> document.setAuction(auction));
        auction.setDocuments(documents);

        return Collections.singletonList(auction);
    }

    private LocalDateTime getAppointmentDate(String appointmentDate) {
        String normalizedDate = extractByPattern(Pattern.compile(DATE_REGEX), appointmentDate);
        return LocalDateTime.parse(normalizedDate, DATE_TIME_FORMATTER);
    }

    private Set<PropertyType> getPropertyTypes(final String propertyTypeName, String expertiseDescription) {
        Set<PropertyType> propertyTypes = new HashSet<>();
        PropertyType propertyTypeByName = PropertyType.getByName(propertyTypeName);
        if (nonNull(propertyTypeByName)) {
            propertyTypes.add(propertyTypeByName);
        }
        propertyTypes.addAll(PropertyType.getBySynonym(propertyTypeName));
        propertyTypes.addAll(PropertyType.getBySynonymIn(expertiseDescription));
        if (propertyTypes.isEmpty()) {
            LOGGER.warn("No property type found {}", propertyTypeName);
        }
        return propertyTypes;
    }

    private String extractByPattern(Pattern pattern, String source) {
        Matcher m = pattern.matcher(source);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    private BuyLimit getLimit(final String limitDescription) {
        BuyLimit buyLimit;
        try {
            buyLimit = BuyLimit.valueOf(limitDescription);
        } catch (IllegalArgumentException e) {
            return BuyLimit.findBySynonym(limitDescription);
        }
        return buyLimit;
    }
}
