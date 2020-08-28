package com.veragg.website.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionStatus;
import com.veragg.website.domain.Court;
import com.veragg.website.repository.AuctionRepo;
import com.veragg.website.repository.DocumentAuctionRepo;

import static java.util.Objects.nonNull;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepo auctionRepo;
    private final DocumentAuctionRepo documentAuctionRepo;

    @Autowired
    public AuctionServiceImpl(AuctionRepo auctionRepo, DocumentAuctionRepo documentAuctionRepo) {
        this.auctionRepo = auctionRepo;
        this.documentAuctionRepo = documentAuctionRepo;
    }

    @Override
    public Auction saveDraft(Auction auction) {
        Auction auctionFound = findDraftByFileNumberCourtSource(auction.getFileNumber(), auction.getCourt(), auction.getSource());
        if (nonNull(auctionFound)) {
            return auctionFound;
        }
        auction.getDocuments().forEach(documentAuctionRepo::save);
        return auctionRepo.save(auction);
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
    public List<Auction> findAll() {
        return auctionRepo.findAll();
    }

    @Override
    public List<Auction> findAllDrafts() {
        return auctionRepo.findAllByAuctionStatus(AuctionStatus.DRAFT);
    }

    @Override
    public Auction findDraftByFileNumberCourtSource(String fileNumber, Court court, AuctionSource source) {
        return auctionRepo.findByFileNumberAndCourtAndSourceAndAuctionStatus(fileNumber, court, source, AuctionStatus.DRAFT);
    }

}
