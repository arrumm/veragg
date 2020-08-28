package com.veragg.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veragg.website.domain.Document;

@Repository
public interface DocumentAuctionRepo extends JpaRepository<Document, Long> {

    Document findByUrl(String url);

    List<Document> findAllByFilePathIsNull();

}
