package com.veragg.website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionDraft;

import lombok.NonNull;

@Service
public class AuctionMergeServiceImpl implements AuctionMergeService {

    AuctionServiceImpl auctionService;

    AuctionMapper auctionMapper;

    @Autowired
    public AuctionMergeServiceImpl(AuctionServiceImpl auctionService, AuctionMapper auctionMapper) {
        this.auctionMapper = auctionMapper;
        this.auctionService = auctionService;
    }

    @Override
    public Auction merge(@NonNull AuctionDraft auctionDraft) {

        Auction auction = auctionMapper.getAuction(auctionDraft);
        return auctionService.save(auction);

    }
}
