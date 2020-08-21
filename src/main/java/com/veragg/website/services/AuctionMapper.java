package com.veragg.website.services;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionDraft;

public interface AuctionMapper {

    Auction getAuction(AuctionDraft draft);

}
