package com.veragg.website.crawler.mapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.veragg.website.crawler.model.HanmarkAuctionModel;
import com.veragg.website.domain.Address;
import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.domain.Limit;

public class HanmarkAuctionMapperServiceImpl implements AuctionMapperService<HanmarkAuctionModel> {

    private static final String HOUSE_NUMBER_REGEX = "\\d+(\\/\\d+)*$";
    private static final String ZIPCODE_REGEX = "^\\d{5}";
    private static final String AMOUNT_REGEX = "([0-9.,]+)";

    @Override
    public AuctionDraft map(final HanmarkAuctionModel auctionModel) {

        AuctionDraft draft = AuctionDraft.builder()
                .fileNumber(auctionModel.getFileNumber())
                .limit(getLimit(auctionModel.getLimitDescription()))
                .address(getAddress(auctionModel.getStreetAddress(), auctionModel.getCityAddress()))
                .amount(Integer.parseInt(extractByPattern(Pattern.compile(AMOUNT_REGEX), auctionModel.getAmount())))
                .build();

        draft.setExpertiseDescription(auctionModel.getExpertDescription());
        draft.setPropertyBuildingDescription(auctionModel.getBuildingDescription());
        draft.setOutdoorDescription(auctionModel.getOutdoorDescription());
        draft.setPropertyPlotDescription(auctionModel.getPlotDescription());

        return draft;
    }


    private Address getAddress(String streetAddress, String cityAddress) {
        Address address = new Address();

        String houseNumber = extractByPattern(Pattern.compile(HOUSE_NUMBER_REGEX), streetAddress);
        address.setNumber(houseNumber);
        address.setStreet(streetAddress.replaceFirst(houseNumber, StringUtils.EMPTY));

        String zipCode = extractByPattern(Pattern.compile(ZIPCODE_REGEX), cityAddress);
        address.setZipCode(zipCode);
        address.setCity(cityAddress.replaceAll(zipCode, StringUtils.EMPTY));

        return address;
    }

    private String extractByPattern(Pattern pattern, String source) {
        Matcher m = pattern.matcher(source);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    private Limit getLimit(final String limitDescription) {
        Limit limit;
        try {
            limit = Limit.valueOf(limitDescription);
        } catch (IllegalArgumentException e) {
            return Limit.findBySynonym(limitDescription);
        }
        return limit;
    }
}
