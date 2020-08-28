package com.veragg.website.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Document;
import com.veragg.website.repository.DocumentAuctionRepo;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentAuctionRepo documentRepo;

    @Autowired
    public DocumentServiceImpl(DocumentAuctionRepo documentRepo) {
        this.documentRepo = documentRepo;
    }

    @Override
    public Document findByUrl(String url) {
        return documentRepo.findByUrl(url);
    }

    @Override
    public Document save(Document document) {
        return documentRepo.save(document);
    }

    @Override
    public List<Document> getToDownload() {
        return documentRepo.findAllByFilePathIsNull();
    }
}
