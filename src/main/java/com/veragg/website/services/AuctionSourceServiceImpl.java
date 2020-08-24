package com.veragg.website.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionSourceType;
import com.veragg.website.repository.AuctionSourceRepo;

@Service
public class AuctionSourceServiceImpl implements AuctionSourceService {

    private AuctionSourceRepo auctionSourceRepo;

    @Autowired
    public AuctionSourceServiceImpl(final AuctionSourceRepo auctionSourceRepo) {
        this.auctionSourceRepo = auctionSourceRepo;
    }

    @Override
    public AuctionSource findByName(String name) {
        return auctionSourceRepo.findByName(name);
    }

    @Override
    public Set<AuctionSource> findPdfSources() {
        return auctionSourceRepo.findAllByAuctionSourceType(AuctionSourceType.PDF);
    }
}
