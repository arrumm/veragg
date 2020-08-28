package com.veragg.website.services;

import com.veragg.website.domain.BaseAuction;
import com.veragg.website.domain.Document;

public interface DocumentService<T extends BaseAuction> {

    Document<T> findByUrl(String url);

    Document<T> save(Document<T> document);

}
