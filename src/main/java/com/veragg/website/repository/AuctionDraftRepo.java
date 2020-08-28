package com.veragg.website.repository;

import org.springframework.stereotype.Repository;

import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.Court;

import lombok.NonNull;

@Repository
public interface AuctionDraftRepo extends BaseAuctionRepo<AuctionDraft> {

    AuctionDraft findByFileNumberAndCourtAndSource(@NonNull String fileNumber, @NonNull Court<AuctionDraft> court, @NonNull AuctionSource source);

}
