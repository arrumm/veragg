package com.veragg.website.jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.veragg.website.domain.Auction;
import com.veragg.website.services.AuctionMergeService;

@Component
public class DraftToAuctionMergeJob {

    AuctionMergeService auctionMergeService;

    @Autowired
    public DraftToAuctionMergeJob(AuctionMergeService auctionMergeService) {
        this.auctionMergeService = auctionMergeService;
    }

    @Async
    @Scheduled(cron = "0 0 13 * * *")
    public void run() {
        List<Auction> sortedDrafts = auctionMergeService.getSortedDrafts();
        auctionMergeService.merge(sortedDrafts);
    }

}
