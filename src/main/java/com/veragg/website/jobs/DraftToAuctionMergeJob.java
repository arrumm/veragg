package com.veragg.website.jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.services.AuctionMergeService;

@Component
public class DraftToAuctionMergeJob {

    AuctionMergeService auctionMergeService;

    @Autowired
    public DraftToAuctionMergeJob(AuctionMergeService auctionMergeService) {
        this.auctionMergeService = auctionMergeService;
    }

    //TODO: fix cron
    @Scheduled(cron = "* * * * 10")
    public void run() {

        List<AuctionDraft> sortedDrafts = auctionMergeService.getSortedDrafts();

        sortedDrafts.forEach(auctionDraft -> auctionMergeService.merge(auctionDraft));

    }

}
