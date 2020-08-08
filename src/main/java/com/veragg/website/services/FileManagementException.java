package com.veragg.website.services;

public class FileManagementException extends RuntimeException {

    public FileManagementException(String string) {
        super(string);
    }

    public FileManagementException(String string, Throwable throwable) {
        super(string, throwable);
    }

}
