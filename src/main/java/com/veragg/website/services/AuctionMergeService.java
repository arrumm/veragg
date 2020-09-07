package com.veragg.website.services;

import java.util.List;

import com.veragg.website.domain.Auction;

public interface AuctionMergeService {

    List<Auction> mergeDrafts(List<Auction> auctions);

    List<Auction> getSortedDrafts();

}
