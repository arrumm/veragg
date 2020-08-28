package com.veragg.website.repository;

import org.springframework.stereotype.Repository;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.Court;

@Repository
public interface AuctionRepo extends BaseAuctionRepo<Auction> {

    Auction findByFileNumberAndCourt(String fileNumber, Court<Auction> court);

}
