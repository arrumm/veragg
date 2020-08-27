package com.veragg.website.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.repository.AuctionDraftRepo;

import static java.util.Objects.nonNull;

@Service
public class AuctionDraftServiceImpl implements AuctionService<AuctionDraft> {

    private final AuctionDraftRepo auctionDraftRepo;

    @Autowired
    public AuctionDraftServiceImpl(AuctionDraftRepo auctionDraftRepo) {
        this.auctionDraftRepo = auctionDraftRepo;
    }

    @Override
    public AuctionDraft save(AuctionDraft auctionDraft) {
        AuctionDraft auctionDraftFound = auctionDraftRepo.findByFileNumberAndCourtAndSource(auctionDraft.getFileNumber(), auctionDraft.getCourt(), auctionDraft.getSource());
        if (nonNull(auctionDraftFound)) {
            return auctionDraftFound;
        }
        return auctionDraftRepo.save(auctionDraft);
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
