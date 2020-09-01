package com.veragg.website.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

@Getter
public enum BuyLimit {
    L0("keine Grenze", 0, "keine Grenze"),
    L70("gelten", 50, "gelten"),
    NA("keine Angabe", 100, "keine Angabe");

    private final String name;
    private final Integer value;
    private final Set<String> synonyms = new HashSet<>();

    BuyLimit(String name, final Integer value, final String... synonyms) {
        this.name = name;
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
