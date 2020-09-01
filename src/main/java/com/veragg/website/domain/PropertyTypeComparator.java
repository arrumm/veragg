package com.veragg.website.domain;

import java.util.Comparator;

public class PropertyTypeComparator implements Comparator<PropertyType> {
    @Override
    public int compare(PropertyType o1, PropertyType o2) {
        return o1.getPriority().compareTo(o2.getPriority());
    }
}
