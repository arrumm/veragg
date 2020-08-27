package com.veragg.website.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.repository.AuctionDraftRepo;

@Service
public class AuctionDraftServiceImpl implements AuctionService<AuctionDraft> {

    private final AuctionDraftRepo auctionDraftRepo;

    @Autowired
    public AuctionDraftServiceImpl(AuctionDraftRepo auctionDraftRepo) {
        this.auctionDraftRepo = auctionDraftRepo;
    }

    @Override
    public AuctionDraft save(AuctionDraft auction) {
        return auctionDraftRepo.save(auction);
    }

    @Override
    public AuctionDraft findById(Long id) {
        return auctionDraftRepo.findById(id).orElse(null);
    }

    @Override
    public AuctionDraft findByFileNumber(String fileNumber) {
        return auctionDraftRepo.findByFileNumber(fileNumber);
    }

    @Override
    public void delete(AuctionDraft auction) {
        auctionDraftRepo.delete(auction);
    }

    @Override
    public List<AuctionDraft> findAll() {
        return auctionDraftRepo.findAll();
    }

}
