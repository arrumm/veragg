package com.veragg.website.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veragg.website.domain.Auction;
import com.veragg.website.repository.AuctionRepo;
import com.veragg.website.repository.DocumentAuctionRepo;

import lombok.NonNull;

import static java.util.Objects.nonNull;

@Service
public class AuctionServiceImpl implements AuctionService<Auction> {

    private final AuctionRepo auctionRepo;
    private final DocumentAuctionRepo documentAuctionRepo;

    @Autowired
    public AuctionServiceImpl(AuctionRepo auctionRepo, DocumentAuctionRepo documentAuctionRepo) {
        this.auctionRepo = auctionRepo;
        this.documentAuctionRepo = documentAuctionRepo;
    }

    @Transactional
    @Override
    public Auction save(@NonNull Auction auction) {
        Auction existingAuction = auctionRepo.findByFileNumberAndCourt(auction.getFileNumber(), auction.getCourt());
        if (nonNull(existingAuction)) {
            return existingAuction;
        } else {
            auction.getDocuments().forEach(documentAuctionRepo::save);
            return auctionRepo.save(auction);
        }
    }

    @Override
    public Auction findById(Long id) {
        return auctionRepo.findById(id).orElse(null);
    }

    @Override
    public Auction findByFileNumber(String fileNumber) {
        return auctionRepo.findByFileNumber(fileNumber);
    }

    @Override
    public void delete(Auction auction) {
        auctionRepo.delete(auction);
    }

    @Override
    public List<Auction> findAll() {
        return auctionRepo.findAll();
    }
}
