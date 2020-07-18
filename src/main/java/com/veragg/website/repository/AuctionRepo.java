package com.veragg.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veragg.website.domain.Auction;

@Repository
public interface AuctionRepo extends JpaRepository<Auction, Long> {

    Auction findByFileNumber(String fileNumber);

}
