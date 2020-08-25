package com.veragg.website.services;

import java.util.List;

import com.veragg.website.domain.Auction;

public interface AuctionService<T extends Auction> {

    T save(T auction);

    T findById(Long id);

    T findByFileNumber(String fileNumber);

    void delete(T auction);

    List<T> findAll();

}
