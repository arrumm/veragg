package com.veragg.website.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionStatus;
import com.veragg.website.domain.Court;

@Repository
public interface AuctionRepo extends JpaRepository<Auction, Long> {

    Auction findByFileNumberAndCourtAndSourceAndAuctionStatus(String fileNumber, Court court, AuctionSource source, AuctionStatus auctionStatus);

    List<Auction> findAllByAuctionStatus(AuctionStatus status);

    Page<Auction> findAllByAuctionStatus(AuctionStatus status, Pageable pageable);

    Page<Auction> findAllByAuctionStatusAndAppointmentIsAfter(AuctionStatus status, LocalDateTime date, Pageable pageable);

    Page<Auction> findAll(Pageable pageable);

}
