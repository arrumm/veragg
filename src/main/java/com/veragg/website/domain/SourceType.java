package com.veragg.website.domain;

import lombok.Getter;

@Getter
public enum SourceType {
    WEBSITE(1),
    PDF(2);

    SourceType(final Integer priority) {
        this.priority = priority;
    }

    private Integer priority;
}
