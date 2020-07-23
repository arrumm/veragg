package com.veragg.website.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

@Getter
public enum BuyLimit {
    L50(50),
    L70(70),
    L100(100, "keine Grenze"),
    NA(100, "keine Angabe");

    private Integer value;
    private Set<String> synonyms = new HashSet<>();

    BuyLimit(final Integer value, final String... synonyms) {
        this.value = value;
        this.synonyms.addAll(Arrays.asList(synonyms));
    }

    public static BuyLimit findBySynonym(String synonym) {
        for (BuyLimit buyLimit : BuyLimit.values()) {
            if (buyLimit.getSynonyms().contains(synonym)) {
                return buyLimit;
            }
        }
        return null;
    }

}
