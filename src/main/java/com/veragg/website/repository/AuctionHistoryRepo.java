package com.veragg.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veragg.website.domain.AuctionHistory;

public interface AuctionHistoryRepo extends JpaRepository<AuctionHistory, String> {
}
