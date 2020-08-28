package com.veragg.website.services;

import org.springframework.stereotype.Service;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.Document;
import com.veragg.website.repository.DocumentAuctionRepo;

@Service
public class AuctionDocumentServiceImpl implements DocumentService<Auction> {

    private final DocumentAuctionRepo documentRepo;

    public AuctionDocumentServiceImpl(DocumentAuctionRepo documentRepo) {
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
