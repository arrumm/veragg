package com.veragg.website.services;

import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;

import com.veragg.website.domain.Document;

public interface FileManager {

    boolean deleteFile(Document file);

    String saveFile(String filePath, ReadableByteChannel readableByteChannel);

    void readFile(Document file, OutputStream outputStream);

}
