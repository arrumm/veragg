package com.veragg.website.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionSourceType;
import com.veragg.website.domain.AuctionStatus;

import lombok.NonNull;

@Service
public class AuctionMergeServiceImpl implements AuctionMergeService {

    AuctionServiceImpl auctionService;

    @Autowired
    public AuctionMergeServiceImpl(AuctionServiceImpl auctionService) {
        this.auctionService = auctionService;
    }

    @Override
    public List<Auction> merge(@NonNull List<Auction> auctions) {
        if (!auctions.isEmpty()) {
            auctions.forEach(auction -> auction.setAuctionStatus(AuctionStatus.ACTIVE));
            return auctionService.saveAll(auctions);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Auction> getSortedDrafts() {

        List<Auction> allDrafts = auctionService.findAllDrafts();

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
