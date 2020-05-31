package com.veragg.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veragg.website.domain.Auction;

public interface AuctionRepo extends JpaRepository<Auction, Long> {

    Auction save(Auction auction);

    Auction findAuctionByFileNumber(String fileNumber);

}
