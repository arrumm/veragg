package com.veragg.website.services;

import java.util.List;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.Court;

public interface AuctionService {

    Auction saveDraft(Auction auction);

    List<Auction> saveAll(List<Auction> auction);

    Auction findById(Long id);

    void delete(Auction auction);

    List<Auction> getAll();

    List<Auction> findAllDrafts();

    Auction findDraftBy(String fileNumber, Court court, AuctionSource source);

}
