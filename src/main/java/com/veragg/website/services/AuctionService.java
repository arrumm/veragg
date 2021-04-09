package com.veragg.website.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.veragg.website.domain.Auction;

public interface AuctionService {

    Auction save(Auction auction);

    List<Auction> saveAll(List<Auction> auction);

    Auction findById(Long id);

    void delete(Auction auction);

    Page<Auction> getAll(Pageable pageable);

    Page<Auction> findAllAvailable(Pageable pageable);

    List<Auction> findAll();

    Auction findBy(Auction auction);
}
