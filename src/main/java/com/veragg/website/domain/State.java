package com.veragg.website.domain;

public enum State {
    BW("Baden Wurt"),
        BE("Berlin"),
        BY("Bayern");
    //    BB,
    //    HB,
    //    HH

    private String name;

    State(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
