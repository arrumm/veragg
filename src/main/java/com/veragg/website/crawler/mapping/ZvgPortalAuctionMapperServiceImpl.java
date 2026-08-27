package com.veragg.website.crawler.mapping;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.crawler.model.ZvgLotAmountDTO;
import com.veragg.website.crawler.model.ZvgPortalAuctionDTO;
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
public class ZvgPortalAuctionMapperServiceImpl implements AuctionMapperService<ZvgPortalAuctionDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZvgPortalAuctionMapperServiceImpl.class);

    private static final String APPOINTMENT_SUFFIX = " Uhr";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEEE, d. MMMM yyyy, HH:mm", Locale.GERMANY);

    private final CourtService courtService;
    private final NormalizationService normalizationService;

    @Autowired
    public ZvgPortalAuctionMapperServiceImpl(final CourtService courtService, final NormalizationService normalizationService) {
        this.courtService = courtService;
        this.normalizationService = normalizationService;
    }

    @Override
    public List<Auction> map(final ZvgPortalAuctionDTO auctionDTO) throws ParseException {

        Address address = AddressHelper.getAddress(auctionDTO.getStreetAddress(), auctionDTO.getCityAddress());
        String courtName = normalizationService.normalizeCity(auctionDTO.getCourtName());
        Court court = courtService.findBy(courtName, address.getZipCode());
        String expertDescription = auctionDTO.getExpertDescription();
        Set<PropertyType> propertyTypes = getPropertyTypes(auctionDTO.getPropertyTypeName(), expertDescription);
        LocalDateTime appointment = getAppointmentDate(auctionDTO.getAppointmentDate());

        List<Auction> auctions = new ArrayList<>();
        for (ZvgLotAmountDTO lot : auctionDTO.getLots()) {

            //@formatter:off
            Auction auction = Auction.builder()
                    .court(court)
                    .address(address)
                    .propertyTypes(propertyTypes)
                    .appointment(appointment)
                    .fileNumber(auctionDTO.getFileNumber())
                    .lotNumber(lot.getLotNumber())
                    .buyLimit(BuyLimit.NA)
                    .amount(Integer.parseInt(normalizationService.normalizeAmount(lot.getAmount())))
                    .expertiseDescription(expertDescription)
                    .sourceUrl(auctionDTO.getSourceUrl())
                    .auctionStatus(AuctionStatus.DRAFT)
                    .build();
            //@formatter:on

            auction.setDocuments(buildDocuments(auctionDTO, auction));
            auctions.add(auction);
        }

        return auctions;
    }

    private List<Document> buildDocuments(ZvgPortalAuctionDTO auctionDTO, Auction auction) {
        List<Document> documents = new ArrayList<>();
        auctionDTO.getImageLinks().forEach(imageUrl -> documents.add(new Document(imageUrl, DocumentType.IMAGE, auctionDTO.getImageLinks().indexOf(imageUrl) + 1)));
        auctionDTO.getExpertiseLinks().forEach(expertiseUrl -> documents.add(new Document(expertiseUrl, DocumentType.EXPERTISE)));
        auctionDTO.getOtherDocumentLinks().forEach(otherDocumentUrl -> documents.add(new Document(otherDocumentUrl, DocumentType.OTHER)));
        documents.forEach(document -> document.setAuction(auction));
        return documents;
    }

    private LocalDateTime getAppointmentDate(String appointmentDate) throws ParseException {
        String normalizedDate = appointmentDate.trim();
        if (normalizedDate.endsWith(APPOINTMENT_SUFFIX)) {
            normalizedDate = normalizedDate.substring(0, normalizedDate.length() - APPOINTMENT_SUFFIX.length());
        }
        try {
            return LocalDateTime.parse(normalizedDate, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ParseException(appointmentDate, e.getErrorIndex());
        }
    }

    private Set<PropertyType> getPropertyTypes(final String propertyTypeName, String expertiseDescription) {
        Set<PropertyType> propertyTypes = new HashSet<>();
        Arrays.stream(propertyTypeName.split(",")).map(String::trim).forEach(typeName -> {
            PropertyType propertyTypeByName = PropertyType.getByName(typeName);
            if (nonNull(propertyTypeByName)) {
                propertyTypes.add(propertyTypeByName);
            }
            propertyTypes.addAll(PropertyType.getBySynonym(typeName));
        });
        propertyTypes.addAll(PropertyType.getBySynonymIn(expertiseDescription));
        if (propertyTypes.isEmpty()) {
            LOGGER.warn("No property type found {}", propertyTypeName);
        }
        return propertyTypes;
    }

}
