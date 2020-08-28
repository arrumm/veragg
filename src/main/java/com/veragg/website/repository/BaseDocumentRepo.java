package com.veragg.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.veragg.website.domain.BaseAuction;
import com.veragg.website.domain.Document;

@NoRepositoryBean
public interface BaseDocumentRepo<T extends BaseAuction> extends JpaRepository<T, Long> {

    Document<T> findByUrl(String url);

    Document<T> save(Document<? extends BaseAuction> document);

}
