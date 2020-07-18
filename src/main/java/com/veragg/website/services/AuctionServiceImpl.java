package com.veragg.website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Auction;
import com.veragg.website.repository.AuctionRepo;

@Service
public class AuctionServiceImpl implements AuctionService<Auction> {

    private AuctionRepo auctionRepo;

    @Autowired
    public AuctionServiceImpl(AuctionRepo auctionRepo) {
        this.auctionRepo = auctionRepo;
    }

    @Override
    public Auction save(Auction auction) {
        return auctionRepo.save(auction);
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
