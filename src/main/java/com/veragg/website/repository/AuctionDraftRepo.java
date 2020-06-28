package com.veragg.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veragg.website.domain.AuctionDraft;

@Repository
public interface AuctionDraftRepo extends JpaRepository<AuctionDraft, Long> {

    AuctionDraft findByFileNumber(String fileNumber);

}
