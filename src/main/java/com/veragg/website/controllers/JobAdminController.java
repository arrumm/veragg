package com.veragg.website.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veragg.website.crawler.Crawling;
import com.veragg.website.crawler.HanmarkCrawler;
import com.veragg.website.crawler.VersteigerungspoolCrawler;
import com.veragg.website.jobs.CrawlerJob;
import com.veragg.website.jobs.CrawlerJobImpl;
import com.veragg.website.jobs.CrawlerJobRunnerServiceImpl;
import com.veragg.website.jobs.DownloadDocumentsJob;
import com.veragg.website.jobs.DraftToAuctionMergeJob;

@Controller
@RequestMapping("/admin")
public class JobAdminController {

    private static final String JOB_EXECUTED_MESSAGE = "job executed";

    private static final String RESULT_KEY = "result";

    private final DraftToAuctionMergeJob mergeJob;

    private final DownloadDocumentsJob downloadDocumentsJob;

    private final HanmarkCrawler hanmarkCrawler;

    private final VersteigerungspoolCrawler versteigerungspoolCrawler;

    private final CrawlerJobRunnerServiceImpl jobRunnerService;

    @Autowired
    public JobAdminController(DraftToAuctionMergeJob mergeJob, DownloadDocumentsJob downloadDocumentsJob, @Qualifier("hanmarkCrawler") HanmarkCrawler hanmarkCrawler,
            @Qualifier("versteigerungspoolCrawler") VersteigerungspoolCrawler versteigerungspoolCrawler, CrawlerJobRunnerServiceImpl jobRunnerService) {
        this.mergeJob = mergeJob;
        this.downloadDocumentsJob = downloadDocumentsJob;
        this.hanmarkCrawler = hanmarkCrawler;
        this.versteigerungspoolCrawler = versteigerungspoolCrawler;
        this.jobRunnerService = jobRunnerService;
    }

    @GetMapping(value = "/jobs/merge", produces = "application/json")
    @ResponseBody
    public Map<String, Object> startMergeJob() {
        final Map<String, Object> result = new HashMap<>();
        mergeJob.run();
        result.put(RESULT_KEY, JOB_EXECUTED_MESSAGE);
        return result;
    }

    @GetMapping(value = "/jobs/hanmark", produces = "application/json")
    @ResponseBody
    public Map<String, Object> startHanmarkCrawler() {
        final CrawlerJob<? extends Crawling> job = new CrawlerJobImpl(hanmarkCrawler);
        jobRunnerService.run(job);

        return Map.of(RESULT_KEY, JOB_EXECUTED_MESSAGE);
    }

    @GetMapping(value = "/jobs/versteigerungspool", produces = "application/json")
    @ResponseBody
    public Map<String, Object> startVersteigerungsCrawler() {
        final CrawlerJob<? extends Crawling> job = new CrawlerJobImpl(versteigerungspoolCrawler);
        jobRunnerService.run(job);

        return Map.of(RESULT_KEY, JOB_EXECUTED_MESSAGE);
    }

    @GetMapping(value = "/jobs/download", produces = "application/json")
    @ResponseBody
    public Map<String, Object> startDownloadMissing() {
        final Map<String, Object> result = new HashMap<>();
        downloadDocumentsJob.run();
        result.put(RESULT_KEY, JOB_EXECUTED_MESSAGE);
        return result;
    }

}

