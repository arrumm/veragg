package com.veragg.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.veragg.website.domain.BaseAuction;

@NoRepositoryBean
public interface BaseAuctionRepo<T extends BaseAuction> extends JpaRepository<T, Long> {

    T findByFileNumber(String fileNumber);

}
