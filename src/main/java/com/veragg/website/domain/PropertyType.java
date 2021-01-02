package com.veragg.website.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum PropertyType {

    FLAT("Wohnung", 2, "Eigentumswohnung", "Einzimmerwohnung", "Zweizimmerwohnung", "Dreizimmerwohnung", "Vierzimmerwohnung", "Fünfzimmerwohnung und mehr", "Sonstige Wohnung",
            "Wohn- und Geschäftsräume"),

    ONE_FAMILY_HOUSE("Einfamilienhaus", 1, "Wohn- und Geschäftshaus"),
    TWO_FAMILIES_HOUSE("Zweifamilienhaus", 1),
    MORE_FAMILY_HOUSE("Mehrfamilienhaus", 1),
    HALF_HOUSE("Doppelhaushälfte", 1),
    TOWNHOUSE("Reihenhaus", 1),
    COTTAGE("Wochenendhaus", 1, "Ferienhaus-/wohnung"),

    AGRICULTURAL_PLOT("Landwirtschaftsfläche", 4),
    FOREST_PLOT("land-/ forstwirtschtl. Flächen", 4),
    BUILDING_PLOT("Baugrundstück", 4),
    FARM_PLOT("Ackerland", 4),
    CARPORT("Stellplatz", 4),
    OTHER_PLOT("Grundstück", 4),

    COMMERCIAL_PROPERTY("Gewerbe", 3, "Wohn-/Gewerbeimmobilie", "Wohn- und Geschäftshaus", "Büro-/Werkstatt-/Lagergebäude", "Büro/Laden", "Büro", "Lagerfläche", "Hotel", "Pension",
            "Klinik/Pflegeheim", "Halle/Werkstatt", "Lagerhalle", "Ladengeschäft", "Halle", "Wohn- und Geschäftsräume", "Sonstige (Gewerbeobjekt)", "Freizeitanlage"),

    OTHER("Ruine", 5);

    private final String name;
    private final Set<String> synonyms = new HashSet<>();
    private final Integer priority;

    PropertyType(String name, Integer priority, String... synonyms) {
        this.name = name;
        this.priority = priority;
        this.synonyms.addAll(Arrays.asList(synonyms));
    }

    public String getName() {
        return name;
    }

    public Set<String> getSynonyms() {
        return synonyms;
    }

    public static PropertyType getByName(String name) {
        for (PropertyType propertyType : values()) {
            if (propertyType.getName().equals(name)) {
                return propertyType;
            }
        }
        return null;
    }

    public static Set<PropertyType> getBySynonym(String synonym) {
        Set<PropertyType> propertyTypes = new HashSet<>();
        Arrays.stream(values()).filter(propertyType -> propertyType.getSynonyms().contains(synonym)).forEach(propertyTypes::add);
        return propertyTypes;
    }

    public static Set<PropertyType> getBySynonymIn(String description) {
        Set<PropertyType> propertyTypes = new HashSet<>();
        //@formatter:off
        Arrays.stream(values())
                .filter(propertyType -> propertyType.getSynonyms().stream()
                        .anyMatch(description::contains))
                .forEach(propertyTypes::add);
        //@formatter:on
        return propertyTypes;
    }

    public Integer getPriority() {
        return priority;
    }
}
