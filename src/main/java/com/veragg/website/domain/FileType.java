package com.veragg.website.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum FileType {
    IMAGE("jpeg", "jpg", "gif", "png", "svg"),
    PDF("pdf"),
    DOC("doc", "docx"),
    XLS("xls", "xlsx");

    private final Set<String> extensions = new HashSet<>();

    FileType(final String... extensions) {
        this.extensions.addAll(Arrays.asList(extensions));
    }

    public Set<String> getExtensions() {
        return extensions;
    }

    public static FileType getByExtension(String extension) {
        return Arrays.stream(FileType.values()).filter(propertyType -> propertyType.getExtensions().contains(extension)).findFirst().orElse(null);
    }

}
