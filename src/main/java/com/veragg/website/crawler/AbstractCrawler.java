package com.veragg.website.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veragg.website.domain.Auction;

//TODO: should be regular job
public abstract class AbstractCrawler implements Crawling {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Set<String> visitedUrls = new HashSet<>();
    //TODO: add repository

    @Override
    public void process() {

        Set<String> urlsToFetch = collectUrls(getStartURL(), 0, getMaxCrawlDepth());

        for (String url : urlsToFetch) {
            //            try {
            System.out.println(url);
            //                String pageData = getPageContent(url);
            //                Auction auction = parseAuction(pageData);
            //TODO: AuctionRepository.save(auction)
            //  or compare in the service and persist then
            //            } catch (IOException e) {
            //                LOGGER.error("Page data fetch error", e);
            //            }
        }

    }

    private Set<String> collectUrls(String currentUrl, int currentDepth, int maxDepth) {

        final Set<String> fetchedUrls = new HashSet<>();

        if (!visitedUrls.contains(currentUrl)) {
            Pattern fetchUrlPattern = getExtractLinkPattern();

            final String currentPageContent;
            try {
                currentPageContent = getPageContent(currentUrl);
                visitedUrls.add(currentUrl);

                fetchedUrls.addAll(fetchUrls(fetchUrlPattern, currentPageContent));
                fetchedUrls.removeAll(visitedUrls);
                if (fetchedUrls.isEmpty()) {

                    if (currentDepth == maxDepth) {
                        fetchedUrls.addAll(fetchUrls(fetchUrlPattern, currentPageContent));
                        fetchedUrls.removeAll(visitedUrls);
                        return fetchedUrls;
                    }

                    Pattern crawlUrlPattern = getCrawlLInkPattern();
                    Set<String> urlsToCrawl = fetchUrls(crawlUrlPattern, currentPageContent);
                    urlsToCrawl.removeAll(visitedUrls);
                    urlsToCrawl.forEach(url -> fetchedUrls.addAll(collectUrls(url, currentDepth + 1, maxDepth)));
                }

            } catch (IOException e) {
                LOGGER.error("Error while get page content", e);
            }
        }

        return fetchedUrls;
    }

    /**
     * Fetches urls from url with logic defined in specific class
     *
     * @param r
     * @param pageContent
     * @return
     * @throws IOException
     */
    private Set<String> fetchUrls(final Pattern r, final String pageContent) {
        Matcher m;
        m = r.matcher(pageContent);
        Set<String> urls = new HashSet<>();
        while (m.find()) {
            urls.add(m.group());
        }
        return urls;
    }

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
     * Parse the page and map it to {@link com.veragg.website.domain.Auction} specific to {@link AbstractCrawler} implementation
     *
     * @param pageData
     */
    abstract Auction parseAuction(String pageData);

    abstract String getStartURL();

    abstract int getMaxCrawlDepth();

    abstract Pattern getExtractLinkPattern();

    abstract Pattern getCrawlLInkPattern();

}
