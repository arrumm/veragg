package com.veragg.website.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionHistory;
import com.veragg.website.domain.AuctionHistoryStatus;
import com.veragg.website.repository.AuctionHistoryRepo;

@Service
public class AuctionHistoryServiceImpl implements AuctionHistoryService {

    AuctionHistoryRepo auctionHistoryRepo;

    @Autowired
    public AuctionHistoryServiceImpl(AuctionHistoryRepo auctionHistoryRepo) {
        this.auctionHistoryRepo = auctionHistoryRepo;
    }

    @Override
    public AuctionHistory getHistoryAdded(Auction auction) {
        //@formatter:off
        return AuctionHistory.builder()
                .auction(auction)
                .historyStatus(AuctionHistoryStatus.ADDED)
                .build();
        //@formatter:on

    }

    @Override
    public List<AuctionHistory> saveAll(List<AuctionHistory> auctionHistoryList) {
        return auctionHistoryRepo.saveAll(auctionHistoryList);
    }

}
