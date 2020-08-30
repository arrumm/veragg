package com.veragg.website.services;

public class FileUrlHelper {

    private FileUrlHelper() {
    }

    public static String getFileName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

}
