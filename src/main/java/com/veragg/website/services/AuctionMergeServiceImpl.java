package com.veragg.website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionDraft;

import lombok.NonNull;

@Service
public class AuctionMergeServiceImpl implements AuctionMergeService {

    AuctionServiceImpl auctionService;

    AuctionDraftServiceImpl auctionDraftService;

    AuctionMapper auctionMapper;

    @Autowired
    public AuctionMergeServiceImpl(AuctionServiceImpl auctionService, AuctionMapper auctionMapper, AuctionDraftServiceImpl auctionDraftService) {
        this.auctionMapper = auctionMapper;
        this.auctionService = auctionService;
        this.auctionDraftService = auctionDraftService;
    }

    @Transactional
    @Override
    public Auction merge(@NonNull AuctionDraft auctionDraft) {

        Auction auction = auctionMapper.getAuction(auctionDraft);
        Auction savedAuction = auctionService.save(auction);
        auctionDraftService.delete(auctionDraft);
        return savedAuction;

    }
}
