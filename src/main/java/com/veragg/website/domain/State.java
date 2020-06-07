package com.veragg.website.domain;

import lombok.Getter;

@Getter
public enum State {
    BW("Baden-Württemberg"),
    BE("Berlin"),
    BY("Bayern"),
    RP("Rheinland-Pfalz");

    private String name;

    State(final String name) {
        this.name = name;
    }

}
