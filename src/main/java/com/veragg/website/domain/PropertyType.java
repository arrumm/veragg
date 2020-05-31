package com.veragg.website.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum PropertyType {

    ONE_FAMILY_HOUSE("Einfamilienhaus", "one"),
    FLAT("Wohnung", ""),
    MORE_FAMILY_HOUSE("Mehrfamilienhaus", ""),
    TWO_FAMILIES_HOUSE("Zweifamilienhaus", "");

    private String name;

    private Set<String> synonyms = new HashSet<>();

    PropertyType(final String name, final String... synonyms) {
        this.name = name;
        this.synonyms.addAll(Arrays.asList(synonyms));
    }

    public String getName() {
        return name;
    }

    public Set<String> getSynonyms() {
        return synonyms;
    }

    public static PropertyType getByName(String name) {
        for (PropertyType propertyType : PropertyType.values()) {
            if (propertyType.getName().equals(name)) {
                return propertyType;
            }
        }
        return null;
    }

    public static PropertyType getBySynonym(String synonym) {
        for (PropertyType propertyType : PropertyType.values()) {
            if (propertyType.getSynonyms().contains(synonym)) {
                return propertyType;
            }
        }
        return null;
    }

}
