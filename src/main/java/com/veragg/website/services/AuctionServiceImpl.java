package com.veragg.website.services;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionStatus;
import com.veragg.website.domain.Court;
import com.veragg.website.repository.AuctionRepo;
import com.veragg.website.repository.CourtRepo;
import com.veragg.website.repository.DocumentAuctionRepo;

import lombok.NonNull;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepo auctionRepo;
    private final DocumentAuctionRepo documentAuctionRepo;
    private final CourtRepo courtRepo;

    @Autowired
    public AuctionServiceImpl(AuctionRepo auctionRepo, DocumentAuctionRepo documentAuctionRepo, CourtRepo courtRepo) {
        this.auctionRepo = auctionRepo;
        this.documentAuctionRepo = documentAuctionRepo;
        this.courtRepo = courtRepo;
    }

    @Override
    @Transactional
    public Auction saveDraft(Auction auction) {
        auction.setDocuments(auction.getDocuments().stream().filter(Objects::nonNull).collect(Collectors.toList()));
        auction.getDocuments().forEach(documentAuctionRepo::save);
        Auction savedAuction = auctionRepo.save(auction);

        Court auctionCourt = auction.getCourt();
        auctionCourt.getAuctions().add(auction);
        courtRepo.save(auctionCourt);
        return savedAuction;
    }

    @Override
    public List<Auction> saveAll(List<Auction> auctions) {
        return auctionRepo.saveAll(auctions);
    }

    @Override
    public Auction findById(Long id) {
        return auctionRepo.findById(id).orElse(null);
    }

    @Override
    public void delete(Auction auction) {
        auctionRepo.delete(auction);
    }

    @Override
    public List<Auction> getAll() {
        return auctionRepo.findAll();
    }

    @Override
    public List<Auction> findAllDrafts() {
        return auctionRepo.findAllByAuctionStatus(AuctionStatus.DRAFT);
    }

    @Override
    public Auction findDraftBy(@NonNull String fileNumber, @NonNull Court court, @NonNull AuctionSource source) {
        return auctionRepo.findByFileNumberAndCourtAndSourceAndAuctionStatus(fileNumber, court, source, AuctionStatus.DRAFT);
    }

}
