package com.veragg.website.crawler.mapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.veragg.website.domain.Address;

public class AddressHelper {

    private static final Pattern HOUSE_NUMBER_PATTERN = Pattern.compile("[\\d\\+]+(\\d+)*");
    private static final Pattern ZIPCODE_PATTERN = Pattern.compile("^\\d{5}");

    private AddressHelper() {
    }

    public static Address getAddress(String streetAddress, String cityAddress) {

        Address.AddressBuilder addressBuilder = Address.builder();

        String houseNumber = extractByPattern(HOUSE_NUMBER_PATTERN, streetAddress).replace("+", "-");
        addressBuilder.number(houseNumber.trim()).street(streetAddress.replaceFirst(houseNumber, StringUtils.EMPTY).trim());

        String zipCode = extractByPattern(ZIPCODE_PATTERN, cityAddress);
        addressBuilder.zipCode(zipCode.trim()).city(cityAddress.replaceAll(zipCode, StringUtils.EMPTY).trim());

        return addressBuilder.build();
    }

    private static String extractByPattern(Pattern pattern, String source) {
        Matcher m = pattern.matcher(source);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

}
