package com.veragg.website.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Document;

import lombok.NonNull;

import static java.util.Objects.nonNull;

@Service
public class DownloadServiceImpl implements DownloadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadServiceImpl.class);

    private final FileManager fileManager;

    @Autowired
    public DownloadServiceImpl(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void downloadFile(@NonNull Document document) {

        URL downloadFileUrl = null;
        try {
            downloadFileUrl = new URL(document.getUrl());
        } catch (MalformedURLException e) {
            LOGGER.error("URL is wrong and cannot be fetched: {}", document.getUrl(), e);
        }

        if (nonNull(downloadFileUrl)) {
            try (ReadableByteChannel readableByteChannel = Channels.newChannel(downloadFileUrl.openStream())) {
                fileManager.transferToFile(document.getStoreName(), readableByteChannel);
            } catch (FileManagementException e) {
                LOGGER.error("Unable to save save the file from url {}", document.getUrl(), e);
            } catch (IOException e) {
                LOGGER.error("Unable to download file from url {}", document.getUrl(), e);
            }
        }

    }
}
