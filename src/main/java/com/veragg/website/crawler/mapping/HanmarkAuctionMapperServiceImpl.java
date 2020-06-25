package com.veragg.website.crawler.mapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
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

import static java.util.Objects.isNull;

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
    public AuctionDraft map(final HanmarkAuctionDTO auctionModel) throws ParseException {

        Address address = getAddress(auctionModel.getStreetAddress(), auctionModel.getCityAddress());
        Court court = courtService.findBy(auctionModel.getCourtName(), address.getZipCode());
        PropertyType propertyType = getPropertyType(auctionModel.getPropertyTypeName());

        //@formatter:off
        return AuctionDraft.draftBuilder()
                .court(court)
                .address(address)
                .propertyType(propertyType)
                .appointment(getAppointmentDate(auctionModel.getAppointmentDate()))
                .fileNumber(auctionModel.getFileNumber())
                .buyLimit(getLimit(auctionModel.getLimitDescription()))
                .amount(Integer.parseInt(getNormalizedAmount(auctionModel.getAmount())))
                .expertiseDescription(auctionModel.getExpertDescription())
                .propertyBuildingDescription(auctionModel.getBuildingDescription())
                .outdoorDescription(auctionModel.getOutdoorDescription())
                .propertyPlotDescription(auctionModel.getPlotDescription())
                .build();
        //@formatter:on
    }

    private Date getAppointmentDate(String appointmentDate) throws ParseException {
        String normalizedDate = extractByPattern(Pattern.compile(DATE_REGEX), appointmentDate);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return dateFormatter.parse(normalizedDate);
    }

    private PropertyType getPropertyType(final String propertyTypeName) {
        PropertyType propertyType = PropertyType.getByName(propertyTypeName);
        if (isNull(propertyType)) {
            propertyType = PropertyType.getBySynonym(propertyTypeName);
        }

        return propertyType;
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
