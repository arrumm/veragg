package com.veragg.website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.domain.Document;
import com.veragg.website.repository.DocumentAuctionDraftRepo;

@Service
public class DocumentAuctionDraftServiceImpl implements DocumentService<AuctionDraft> {

    private final DocumentAuctionDraftRepo documentRepo;

    @Autowired
    public DocumentAuctionDraftServiceImpl(DocumentAuctionDraftRepo documentRepo) {
        this.documentRepo = documentRepo;
    }

    @Override
    public Document<AuctionDraft> findByUrl(String url) {
        return documentRepo.findByUrl(url);
    }

    @Override
    public Document<AuctionDraft> save(Document<AuctionDraft> document) {
        return documentRepo.save(document);
    }
}
