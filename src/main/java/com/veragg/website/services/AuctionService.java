package com.veragg.website.services;

import java.util.List;

import com.veragg.website.domain.BaseAuction;

public interface AuctionService<T extends BaseAuction> {

    T save(T auction);

    T findById(Long id);

    T findByFileNumber(String fileNumber);

    void delete(T auction);

    List<T> findAll();

}
