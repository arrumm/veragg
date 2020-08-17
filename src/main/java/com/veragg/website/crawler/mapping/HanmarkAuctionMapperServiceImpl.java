package com.veragg.website.crawler.mapping;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.crawler.model.HanmarkAuctionDTO;
import com.veragg.website.domain.Address;
import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.domain.BuyLimit;
import com.veragg.website.domain.Court;
import com.veragg.website.domain.PropertyType;
import com.veragg.website.services.CourtService;

import static java.util.Objects.nonNull;

@Service
public class HanmarkAuctionMapperServiceImpl implements AuctionMapperService<HanmarkAuctionDTO> {

    private static final String HOUSE_NUMBER_REGEX = "\\d+(\\/\\d+)*$";
    private static final String ZIPCODE_REGEX = "^\\d{5}";
    private static final String AMOUNT_REGEX = "([0-9.,]+)";
    private static final String AMOUNT_CENT_REGEX = ",.+";
    private static final String DATE_REGEX = "([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d [012]{0,1}[0-9]:[0-6][0-9]";
    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";

    private final CourtService courtService;

    @Autowired
    public HanmarkAuctionMapperServiceImpl(final CourtService courtService) {
        this.courtService = courtService;
    }

    @Override
    public AuctionDraft map(final HanmarkAuctionDTO auctionDTO) throws ParseException {

        Address address = getAddress(auctionDTO.getStreetAddress(), auctionDTO.getCityAddress());
        Court court = courtService.findBy(auctionDTO.getCourtName(), address.getZipCode());
        Set<PropertyType> propertyTypes = getPropertyTypes(auctionDTO.getPropertyTypeName());

        //@formatter:off
        return AuctionDraft.draftBuilder()
                .court(court)
                .address(address)
                .propertyTypes(propertyTypes)
                .appointment(getAppointmentDate(auctionDTO.getAppointmentDate()))
                .fileNumber(auctionDTO.getFileNumber())
                .buyLimit(getLimit(auctionDTO.getLimitDescription()))
                .amount(Integer.parseInt(getNormalizedAmount(auctionDTO.getAmount())))
                .expertiseDescription(auctionDTO.getExpertDescription())
                .propertyBuildingDescription(auctionDTO.getBuildingDescription())
                .outdoorDescription(auctionDTO.getOutdoorDescription())
                .propertyPlotDescription(auctionDTO.getPlotDescription())
                .sourceUrl(auctionDTO.getSourceUrl())
                .build();
        //@formatter:on
    }

    private LocalDateTime getAppointmentDate(String appointmentDate) {
        String normalizedDate = extractByPattern(Pattern.compile(DATE_REGEX), appointmentDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDateTime.parse(normalizedDate, formatter);
    }

    private Set<PropertyType> getPropertyTypes(final String propertyTypeName) {
        Set<PropertyType> propertyTypes = new HashSet<>();
        PropertyType propertyTypeByName = PropertyType.getByName(propertyTypeName);
        if (nonNull(propertyTypeByName)) {
            propertyTypes.add(propertyTypeByName);
        }
        propertyTypes.addAll(PropertyType.getBySynonym(propertyTypeName));
        return propertyTypes;
    }

    private String getNormalizedAmount(final String amount) {
        String extractedAmount = extractByPattern(Pattern.compile(AMOUNT_REGEX), amount);
        extractedAmount = extractedAmount.replaceAll(AMOUNT_CENT_REGEX, "");
        return extractedAmount.replaceAll("\\.", "");
    }

    private Address getAddress(String streetAddress, String cityAddress) {
        Address address = new Address();

        String houseNumber = extractByPattern(Pattern.compile(HOUSE_NUMBER_REGEX), streetAddress);
        address.setNumber(houseNumber.trim());
        address.setStreet(streetAddress.replaceFirst(houseNumber, StringUtils.EMPTY).trim());

        String zipCode = extractByPattern(Pattern.compile(ZIPCODE_REGEX), cityAddress);
        address.setZipCode(zipCode.trim());
        address.setCity(cityAddress.replaceAll(zipCode, StringUtils.EMPTY).trim());

        return address;
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
