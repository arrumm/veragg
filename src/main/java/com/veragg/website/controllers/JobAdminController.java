package com.veragg.website.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veragg.website.crawler.Crawling;
import com.veragg.website.crawler.HanmarkCrawler;
import com.veragg.website.jobs.CrawlerJob;
import com.veragg.website.jobs.CrawlerJobImpl;
import com.veragg.website.jobs.DraftToAuctionMergeJob;

@Controller
@RequestMapping("/admin")
public class JobAdminController {

    private final DraftToAuctionMergeJob mergeJob;

    private HanmarkCrawler hanmarkCrawler;

    @Autowired
    public JobAdminController(DraftToAuctionMergeJob mergeJob, HanmarkCrawler hanmarkCrawler) {
        this.mergeJob = mergeJob;
        this.hanmarkCrawler = hanmarkCrawler;
    }

    @GetMapping(value = "/jobs/merge", produces = "application/json")
    @ResponseBody
    public Map<String, Object> startMergeJob() {
        final Map<String, Object> result = new HashMap<>();
        mergeJob.run();
        result.put("result", "job executed");
        return result;
    }

    @GetMapping(value = "/jobs/hanmark", produces = "application/json")
    @ResponseBody
    public Map<String, Object> startHanmarkCrawler() {
        final Map<String, Object> result = new HashMap<>();

        final CrawlerJob<? extends Crawling> job = new CrawlerJobImpl(hanmarkCrawler);
        job.execute();

        result.put("result", "job executed");
        return result;
    }

}

