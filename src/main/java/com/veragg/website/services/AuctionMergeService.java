package com.veragg.website.services;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionDraft;

public interface AuctionMergeService {

    Auction merge(AuctionDraft auctionDraft);

}
