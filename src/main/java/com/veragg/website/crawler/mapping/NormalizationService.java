package com.veragg.website.crawler.mapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class NormalizationService {

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("([0-9.,]+)");

    public String normalizeCity(String name) {

        String result = name;
        result = result.replace("ü", "u");
        result = result.replace("ö", "o");
        result = result.replace("ä", "a");
        result = result.replace(" -", "-");
        result = result.replace("- ", "-");
        result = result.replace("ß", "ss");

        return result;

    }

    public String normalizeAmount(final String amount) {
        Matcher m = AMOUNT_PATTERN.matcher(amount);
        String extractedAmount = m.find() ? m.group() : "";
        int commaIndex = extractedAmount.indexOf(',');
        String integerPart = commaIndex > -1 ? extractedAmount.substring(0, commaIndex) : extractedAmount;
        return integerPart.replace(".", "");
    }

}
