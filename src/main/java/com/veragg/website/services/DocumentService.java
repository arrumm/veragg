package com.veragg.website.services;

import com.veragg.website.domain.Document;

public interface DocumentService {

    Document findByUrl(String url);

    Document save(Document document);

}
