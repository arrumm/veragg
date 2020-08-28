package com.veragg.website.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionSourceType;

import lombok.NonNull;

@Service
public class AuctionMergeServiceImpl implements AuctionMergeService {

    AuctionServiceImpl auctionService;

    @Autowired
    public AuctionMergeServiceImpl(AuctionServiceImpl auctionService) {
        this.auctionService = auctionService;
    }

    @Transactional
    @Override
    public List<Auction> merge(@NonNull List<Auction> auctions) {
        return auctionService.saveAll(auctions);
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
