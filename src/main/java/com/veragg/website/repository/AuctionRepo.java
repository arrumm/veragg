package com.veragg.website.repository;

import org.springframework.data.repository.CrudRepository;

import com.veragg.website.domain.Auction;

public interface AuctionRepo extends CrudRepository<Auction, Long> {
}
