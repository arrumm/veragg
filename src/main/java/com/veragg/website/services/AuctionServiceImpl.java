package com.veragg.website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Auction;
import com.veragg.website.repository.AuctionRepo;

import lombok.NonNull;

import static java.util.Objects.nonNull;

@Service
public class AuctionServiceImpl implements AuctionService<Auction> {

    private final AuctionRepo auctionRepo;

    @Autowired
    public AuctionServiceImpl(AuctionRepo auctionRepo) {
        this.auctionRepo = auctionRepo;
    }

    @Override
    public Auction save(@NonNull Auction auction) {
        Auction existingAuction = auctionRepo.findByFileNumber(auction.getFileNumber());
        if (nonNull(existingAuction)) {
            return existingAuction;
        } else {
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
}
