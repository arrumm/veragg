package com.veragg.website.crawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veragg.website.crawler.mapping.AuctionMapperService;
import com.veragg.website.crawler.model.BaseAuctionDTO;
import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.services.AuctionService;

import lombok.NonNull;

import static com.veragg.website.crawler.InternetUtils.getPageContent;

public abstract class AbstractCrawler implements Crawling {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final Set<String> visitedUrls = new HashSet<>();

    AuctionMapperService auctionMapper;

    AuctionService<AuctionDraft> auctionService;

    public AbstractCrawler() {
    }

    @Override
    public void process() {

        Set<String> urlsToFetch = collectAuctionUrls(getStartURL(), 0, getContainerPageUrlPattern(), getAuctionUrlPattern());

        for (String url : urlsToFetch) {
            try {
                String pageData = getPageContent(url);
                BaseAuctionDTO auctionDTO = fetchAuction(new ByteArrayInputStream(pageData.getBytes()), url);
                AuctionDraft auctionDraft = auctionMapper.map(auctionDTO);
                auctionService.save(auctionDraft);
            } catch (IOException e) {
                LOGGER.error("Page data fetch error", e);//TODO roman: test exception
            } catch (ParseException e) {
                LOGGER.error("Fetched data parse error", e);//TODO roman: test exception
            }
        }

    }

    /**
     * @param currentUrl               url to fetch auctions urls from / crawl further
     * @param startDepth               start level of depth of recursive call
     * @param urlsToCrawlPattern       to get urls of pages possibly contains auction urls
     * @param extractAuctionUrlPattern to get auction urls
     * @return set of auction urls
     */
    Set<String> collectAuctionUrls(@NonNull String currentUrl, int startDepth, @NonNull Pattern urlsToCrawlPattern, @NonNull Pattern extractAuctionUrlPattern) {

        if (startDepth < 0 || getMaxCrawlDepth() < 0 || startDepth > getMaxCrawlDepth()) {
            throw new IllegalArgumentException("Current depth of crawling shouldn't be more than maximum depth");
        }

        final Set<String> fetchedUrls = new HashSet<>();

        if (!visitedUrls.contains(currentUrl)) {
            final String currentPageContent;
            try {
                currentPageContent = getPageContent(currentUrl);
                visitedUrls.add(currentUrl);

                fetchedUrls.addAll(fetchUrls(extractAuctionUrlPattern, currentPageContent));
                fetchedUrls.removeAll(visitedUrls);

                //if urls not found continue crawling
                if (fetchedUrls.isEmpty() && startDepth != getMaxCrawlDepth()) {
                    Set<String> urlsToCrawl = fetchUrls(urlsToCrawlPattern, currentPageContent);
                    urlsToCrawl.removeAll(visitedUrls);
                    urlsToCrawl.forEach(url -> fetchedUrls.addAll(collectAuctionUrls(url, startDepth + 1, urlsToCrawlPattern, extractAuctionUrlPattern)));
                }

            } catch (IOException e) {
                LOGGER.error("Error get content of [{}]", currentUrl, e);
            }
        }

        fetchedUrls.removeAll(visitedUrls);
        return fetchedUrls;
    }

    /**
     * Fetches urls from url with logic defined in specific class
     *
     * @param pattern     to fetch urls by
     * @param pageContent to fetch urls from
     * @return set of urls
     */
    Set<String> fetchUrls(final Pattern pattern, final String pageContent) {
        Matcher m = pattern.matcher(pageContent);
        Set<String> urls = new HashSet<>();
        while (m.find()) {
            urls.add(m.group());
        }
        return urls;
    }

    /**
     * Parse the page and map it to {@link BaseAuctionDTO} specific to {@link AbstractCrawler} implementation
     *
     * @param pageData input stream of page data
     */
    abstract BaseAuctionDTO fetchAuction(InputStream pageData, String baseUri) throws IOException;

    abstract String getStartURL();

    abstract int getMaxCrawlDepth();

    abstract Pattern getAuctionUrlPattern();

    abstract Pattern getContainerPageUrlPattern();

}
