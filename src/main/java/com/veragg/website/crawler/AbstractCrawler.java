package com.veragg.website.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veragg.website.domain.Auction;

import jdk.internal.joptsimple.internal.Strings;

//TODO: should be regular job
public abstract class AbstractCrawler implements Crawling {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //TODO: add repository

    @Override
    public void process() {

        Set<String> urlsToFetch = collectUrls(getStartURL(), getStartCrawlDepth(), getEndCrawlDepth());

        for (String url : urlsToFetch) {
            try {
                String pageData = getPageContent(url);
                Auction auction = parseAuction(pageData);
                //TODO: AuctionRepository.save(auction)
                //  or compare in the service and persist then
            } catch (IOException e) {
                LOGGER.error("Page data fetch error", e);
            }
        }

    }

    private Set<String> collectUrls(String startUrl, int startDepth, int endDepth) {

        if (startDepth >= endDepth) {
            return fetchUrls(startUrl);
        }

        Set<String> resultUrls = new HashSet<>();
        Set<String> currentUrls = fetchUrls(startUrl);
        currentUrls.forEach(currentUrl -> resultUrls.addAll(collectUrls(currentUrl, startDepth + 1, endDepth)));

        return resultUrls;
    }

    /**
     * Fetches urls from url with logic defined in specific class
     *
     * @param startUrl
     * @return
     */
    abstract Set<String> fetchUrls(String startUrl);

    public String getPageContent(String url) throws IOException {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        return EntityUtils.toString(entity);
                    }
                } else {
                    LOGGER.warn("Page data from url {} cannot be fetched, status {}", url, status);
                }

            }
        }
        return Strings.EMPTY;
    }

    /**
     * Parse the page and map it to {@link com.veragg.website.domain.Auction}
     * specific to {@link AbstractCrawler} implementation
     * @param pageData
     */
    abstract Auction parseAuction(String pageData);

    abstract String getStartURL();

    abstract int getStartCrawlDepth();

    abstract int getEndCrawlDepth();

}
