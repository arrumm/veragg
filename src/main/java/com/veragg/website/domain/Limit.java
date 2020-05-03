package com.veragg.website.domain;

public enum Limit {
    L50(50),
    L70(70),
    L0(0);

    private int value;

    Limit(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
