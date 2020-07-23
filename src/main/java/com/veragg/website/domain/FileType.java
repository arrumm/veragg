package com.veragg.website.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum FileType {
    IMAGE("jpeg", "jpg", "gif", "png", "svg"),
    PDF("pdf");

    private Set<String> extensions = new HashSet<>();

    FileType(final String... extensions) {
        this.extensions.addAll(Arrays.asList(extensions));
    }

}
