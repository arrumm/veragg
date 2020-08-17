package com.veragg.website.services;

import java.util.Set;

import com.veragg.website.domain.AuctionSource;

public interface AuctionSourceService {

    AuctionSource findByName(String name);

    Set<AuctionSource> findPdfSources();

}
