package com.veragg.website.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionSourceType;

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
        return auctionService.save(auction);

    }

    @Override
    public List<AuctionDraft> getSortedDrafts() {

        List<AuctionDraft> allDrafts = auctionDraftService.findAll();

        allDrafts.sort((a, b) -> {
            AuctionSource sourceA = a.getSource();
            AuctionSource sourceB = b.getSource();
            AuctionSourceType auctionSourceTypeA = sourceA.getAuctionSourceType();
            AuctionSourceType auctionSourceTypeB = sourceB.getAuctionSourceType();
            if (auctionSourceTypeA.equals(auctionSourceTypeB)) {
                return sourceA.getPriority().compareTo(sourceB.getPriority());
            } else {
                Integer ordinalA = auctionSourceTypeA.ordinal();
                int ordinalB = auctionSourceTypeB.ordinal();
                if (ordinalA.equals(ordinalB)) {
                    return a.getFileNumber().compareTo(b.getFileNumber());
                } else {
                    return Integer.compare(ordinalA, ordinalB);
                }
            }
        });

        return allDrafts;
    }

}
