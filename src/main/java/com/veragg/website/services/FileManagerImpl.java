package com.veragg.website.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Document;

import lombok.NonNull;

@Service
public class FileManagerImpl implements FileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManagerImpl.class);
    private static final String FILE_NOT_EXIST_MESSAGE = "File doesn't exist: ";
    private static final String UPDATE_FILE_INFO_MESSAGE = "Update mode. File {} already exists. Will be replaced with the new binary content";

    @Value("${downloads.storageRootPath}")
    private String storageRootPath;

    @Override
    public boolean deleteFile(Document file) {
        Path storagePath = getFilesystemPath(file);

        if (notExists(storagePath)) {
            throw new FileManagementException(FILE_NOT_EXIST_MESSAGE + file.getStoreName());
        }

        deleteFile(storagePath);

        return true;
    }

    private boolean notExists(Path storagePath) {
        return !exists(storagePath);
    }

    private boolean exists(Path storagePath) {
        return storagePath.toFile().exists();
    }

    @Override
    public String transferToFile(@NonNull String fileStoreName, @NonNull ReadableByteChannel readableByteChannel) {
        Path storagePath = StoragePathService.generateFilesystemPath(storageRootPath, fileStoreName);

        Path storagePathParent = storagePath.getParent();
        if (notExists(storagePathParent)) {
            createDirectories(storagePathParent);
        }

        if (exists(storagePath)) {
            LOGGER.info(UPDATE_FILE_INFO_MESSAGE, storagePath);
            deleteFile(storagePath);
        }

        createFile(storagePath);

        saveToFile(readableByteChannel, storagePath);

        return storagePath.toFile().getAbsolutePath();
    }

    private void saveToFile(@NonNull ReadableByteChannel readableByteChannel, @NonNull Path storagePath) {
        try (FileOutputStream fos = new FileOutputStream(storagePath.toFile())) {
            fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (FileNotFoundException e) {
            throw new FileManagementException("File not found in " + storagePath, e);
        } catch (IOException e) {
            throw new FileManagementException("Save file data failed", e);
        }
    }

    private void createFile(Path storagePath) {
        try {
            Files.createFile(storagePath);
        } catch (final IOException e) {
            throw new FileManagementException("Creating file path failed " + storagePath, e);
        }
    }

    private void deleteFile(Path storagePath) {
        try {
            Files.delete(storagePath);
        } catch (IOException e) {
            throw new FileManagementException("Couldn't delete the file: " + storagePath, e);
        }
    }

    private void createDirectories(Path storagePath) {
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new FileManagementException("Failed creating folders for the path " + storagePath, e);
        }
    }

    @Override
    public void readFile(Document file, OutputStream outputStream) {
        final Path storagePath = getFilesystemPath(file);

        if (notExists(storagePath)) {
            throw new FileManagementException(FILE_NOT_EXIST_MESSAGE + file.getStoreName());
        }

        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(storagePath, BasicFileAttributes.class);
            if (fileAttributes.size() == 0) {
                throw new FileManagementException("File has zero byte size: " + file.getStoreName());
            }
        } catch (final IOException e) {
            throw new FileManagementException("Reading file attributes failed.", e);
        }

        try (InputStream inputStream = Files.newInputStream(storagePath)) {
            IOUtils.copyLarge(inputStream, outputStream);
        } catch (final Exception e) {
            throw new FileManagementException("Reading file data failed.", e);
        }
    }

    private Path getFilesystemPath(Document file) {
        return StoragePathService.generateFilesystemPath(storageRootPath, file.getStoreName());
    }

}
