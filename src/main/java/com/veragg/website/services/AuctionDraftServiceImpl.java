package com.veragg.website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.repository.AuctionDraftRepo;

@Service
public class AuctionDraftServiceImpl implements AuctionService<AuctionDraft> {

    private final AuctionDraftRepo auctionRepo;

    @Autowired
    public AuctionDraftServiceImpl(AuctionDraftRepo auctionRepo) {
        this.auctionRepo = auctionRepo;
    }

    @Override
    public AuctionDraft save(AuctionDraft auction) {
        return auctionRepo.save(auction);
    }

    @Override
    public AuctionDraft findById(Long id) {
        return auctionRepo.findById(id).orElse(null);
    }

    @Override
    public AuctionDraft findByFileNumber(String fileNumber) {
        return auctionRepo.findByFileNumber(fileNumber);
    }

    @Override
    public void delete(AuctionDraft auction) {
        auctionRepo.delete(auction);
    }

}
