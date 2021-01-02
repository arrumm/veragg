package com.veragg.website.domain;

import lombok.Getter;

@Getter
public enum AuctionSourceType {
    WEBSITE(1),
    PDF(2);

    AuctionSourceType(final Integer priority) {
        this.priority = priority;
    }

    private final Integer priority;
}
