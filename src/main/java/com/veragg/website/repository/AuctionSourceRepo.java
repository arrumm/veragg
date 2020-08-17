package com.veragg.website.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionSourceType;

public interface AuctionSourceRepo extends JpaRepository<AuctionSource, Integer> {

    AuctionSource findByName(String name);

    Set<AuctionSource> findAllBySourceType(AuctionSourceType auctionSourceType);

}
