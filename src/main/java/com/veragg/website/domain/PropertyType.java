package com.veragg.website.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum PropertyType {

    FLAT("Wohnung", "Eigentumswohnung", "Einzimmerwohnung", "Zweizimmerwohnung", "Dreizimmerwohnung", "Vierzimmerwohnung", "Fünfzimmerwohnung und mehr", "Sonstige Wohnung",
            "Wohn- und Geschäftsräume"),
    //house
    ONE_FAMILY_HOUSE("Einfamilienhaus", "Wohn- und Geschäftshaus"),
    MORE_FAMILY_HOUSE("Mehrfamilienhaus"),
    TWO_FAMILIES_HOUSE("Zweifamilienhaus"),
    //plot
    AGRICULTURAL_PLOT("Landwirtschaftsfläche"),
    FOREST_PLOT("land-/ forstwirtschtl. Flächen"),
    BUILDING_PLOT("Baugrundstück"),
    FARM_PLOT("Ackerland"),

    COMMERCIAL_PROPERTY("Gewerbe", "Wohn-/Gewerbeimmobilie", "Wohn- und Geschäftshaus", "Büro-/Werkstatt-/Lagergebäude", "Büro/Laden", "Büro", "Lagerfläche", "Hotel", "Pension", "Klinik/Pflegeheim",
            "Halle/Werkstatt", "Lagerhalle", "Ladengeschäft", "Halle", "Wohn- und Geschäftsräume", "Sonstige (Gewerbeobjekt)", "Freizeitanlage");

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

    public static Set<PropertyType> getBySynonym(String synonym) {
        Set<PropertyType> propertyTypes = new HashSet<>();
        Arrays.stream(PropertyType.values()).filter(propertyType -> propertyType.getSynonyms().contains(synonym)).forEach(propertyTypes::add);
        return propertyTypes;
    }

}
