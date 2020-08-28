package com.veragg.website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.Document;
import com.veragg.website.repository.DocumentAuctionRepo;

@Service
public class DocumentAuctionServiceImpl implements DocumentService<Auction> {

    private final DocumentAuctionRepo documentRepo;

    @Autowired
    public DocumentAuctionServiceImpl(DocumentAuctionRepo documentRepo) {
        this.documentRepo = documentRepo;
    }

    @Override
    public Document<Auction> findByUrl(String url) {
        return documentRepo.findByUrl(url);
    }

    @Override
    public Document<Auction> save(Document<Auction> document) {
        return documentRepo.save(document);
    }
}
