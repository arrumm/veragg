package com.veragg.website.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

@Getter
public enum Limit {
    L50(50),
    L70(70),
    L100(100, "keine Grenze", "keine Angabe");

    private Integer value;
    private Set<String> synonyms = new HashSet<>();

    Limit(final Integer value, final String... synonyms) {
        this.value = value;
        this.synonyms.addAll(Arrays.asList(synonyms));
    }

    public static Limit findBySynonym(String synonym) {
        for (Limit limit : Limit.values()) {
            if (limit.getSynonyms().contains(synonym)) {
                return limit;
            }
        }
        return null;
    }

}
