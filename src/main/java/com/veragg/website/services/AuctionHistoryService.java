package com.veragg.website.services;

import java.util.List;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionHistory;

public interface AuctionHistoryService {

    AuctionHistory getHistoryAdded(Auction auction);

    List<AuctionHistory> saveAll(List<AuctionHistory> auctionHistoryList);

}
