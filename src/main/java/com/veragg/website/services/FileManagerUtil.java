package com.veragg.website.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.NonNull;

public class FileManagerUtil {

    static Path generateFilesystemPath(@NonNull String storageRootPath, @NonNull String storeName) {
        StringBuilder storagePath = new StringBuilder(storageRootPath);
        if (!storageRootPath.endsWith(File.separator)) {
            storagePath.append(File.separator);
        }
        storagePath.append(storeName.charAt(0));
        storagePath.append(File.separator);
        storagePath.append(storeName.charAt(1));
        storagePath.append(File.separator);
        storagePath.append(storeName);

        return Paths.get(storagePath.toString());
    }

    private FileManagerUtil() {
    }
}
