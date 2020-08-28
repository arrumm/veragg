package com.veragg.website.services;

import java.util.List;

import com.veragg.website.domain.Document;

public interface DocumentService {

    Document findByUrl(String url);

    Document save(Document document);

    List<Document> getToDownload();

}
