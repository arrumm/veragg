package com.veragg.website.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veragg.website.jobs.DraftToAuctionMergeJob;

@Controller
@RequestMapping("/admin")
public class JobAdminController {

    private final DraftToAuctionMergeJob mergeJob;

    @Autowired
    public JobAdminController(DraftToAuctionMergeJob mergeJob) {
        this.mergeJob = mergeJob;
    }

    @GetMapping(value = "/jobs/merge", produces = "application/json")
    @ResponseBody
    public Map<String, Object> startMergeJob() {
        final Map<String, Object> result = new HashMap<>();
        mergeJob.run();
        result.put("result", "job executed");
        return result;
    }

}

