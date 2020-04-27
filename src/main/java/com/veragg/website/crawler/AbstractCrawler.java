package com.veragg.website.crawler;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCrawler implements Crawling {

    //TODO:

    @Override
    public Set<String> collectUrls(final String startUrl, final int startDepth, final int endDepth) {

        if (startDepth == endDepth) {
            return fetchUrls(startUrl);
        }

        Set<String> resultUrls = new HashSet<>();
        Set<String> currentUrls = fetchUrls(startUrl);
        currentUrls.forEach(s -> resultUrls.addAll(collectUrls(s, startDepth + 1, endDepth)));

        return resultUrls;
    }

    public abstract Set<String> fetchUrls(final String startUrl);

    //TODO: add method to make request and extract urls by regexp

    //TODO: method to parse page itself

}
