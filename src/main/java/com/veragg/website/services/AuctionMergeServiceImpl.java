package com.veragg.website.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionHistory;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionSourceType;
import com.veragg.website.domain.AuctionStatus;

import lombok.NonNull;

@Service
public class AuctionMergeServiceImpl implements AuctionMergeService {

    AuctionService auctionService;

    AuctionHistoryService auctionHistoryService;

    @Autowired
    public AuctionMergeServiceImpl(AuctionService auctionService, AuctionHistoryService auctionHistoryService) {
        this.auctionService = auctionService;
        this.auctionHistoryService = auctionHistoryService;
    }

    @Override
    @Transactional
    public List<Auction> mergeDrafts(@NonNull List<Auction> auctions) {
        if (!auctions.isEmpty()) {
            auctions.forEach(auction -> auction.setAuctionStatus(AuctionStatus.ACTIVE));
            //@formatter:off
            List<AuctionHistory> auctionHistoryList = auctions.stream()
                    .map(auction -> auctionHistoryService.getHistoryAdded(auction))
                    .collect(Collectors.toList());
            //@formatter:on
            auctionHistoryService.saveAll(auctionHistoryList);
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
