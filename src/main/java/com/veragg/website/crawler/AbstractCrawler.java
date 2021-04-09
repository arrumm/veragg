package com.veragg.website.crawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veragg.website.crawler.mapping.AuctionMapperService;
import com.veragg.website.crawler.model.BaseAuctionDTO;
import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.services.AuctionService;
import com.veragg.website.services.AuctionSourceService;

import lombok.NonNull;

import static java.util.Objects.isNull;

public abstract class AbstractCrawler<T extends BaseAuctionDTO> implements Crawling {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final Set<String> visitedUrls = new HashSet<>();

    AuctionMapperService<T> auctionMapper;

    AuctionService auctionService;

    AuctionSourceService auctionSourceService;

    Parsing<T> auctionParser;

    protected AbstractCrawler() {
    }

    @Override
    public void crawl() {

        Set<String> urlsToFetch = collectAuctionUrls(getStartURL(), 0, getContainerPageUrlPattern(), getAuctionUrlPattern());

        if (!urlsToFetch.isEmpty()) {
            AuctionSource auctionSource = auctionSourceService.findByName(getSourceName());
            T auctionDTO = null;
            for (String url : urlsToFetch) {
                try {
                    PageData pageData = new PageData(url);
                    Document doc = getDocumentFromContent(pageData.fetch(), pageData.getUrl());
                    auctionDTO = auctionParser.parse(doc);
                    Auction auction = auctionMapper.map(auctionDTO);
                    auction.setSource(auctionSource);
                    persist(auction);
                } catch (IOException e) {
                    LOGGER.error("Page data fetch from [{}] failed", url, e);
                } catch (ParseException e) {
                    LOGGER.error("Auction parse from [{}] failed", auctionDTO, e);
                }
            }
        }

        cleanup();

    }

    private void persist(Auction auction) {
        Auction existingAuction = auctionService.findBy(auction);
        if (isNull(existingAuction)) {
            auctionService.save(auction);
        }
    }

    protected Document getDocumentFromContent(PageData pageData, String baseUri) throws IOException {
        return Jsoup.parse(new ByteArrayInputStream(pageData.getContent().getBytes()), "UTF-8", baseUri);
    }

    /**
     * @param currentRelatedUrl        url to fetch auctions urls from / crawl further
     * @param startDepth               start level of depth of recursive call
     * @param urlsToCrawlPattern       to get urls of pages possibly contains auction urls
     * @param extractAuctionUrlPattern to get auction urls
     * @return set of auction urls
     */
    Set<String> collectAuctionUrls(@NonNull String currentRelatedUrl, int startDepth, @NonNull Pattern urlsToCrawlPattern, @NonNull Pattern extractAuctionUrlPattern) {

        if (startDepth < 0 || getMaxCrawlDepth() < 0 || startDepth > getMaxCrawlDepth()) {
            throw new IllegalArgumentException("Current depth of crawling shouldn't be more than maximum depth");
        }

        final Set<String> fetchedUrls = new HashSet<>();

        // TODO Roman: 26-Dec-20 test for absolute url
        final String currentAbsoluteUrl = getAbsoluteUrl(currentRelatedUrl);

        if (!visitedUrls.contains(currentAbsoluteUrl)) {
            final String currentPageContent;
            try {
                currentPageContent = new PageData(currentAbsoluteUrl).fetch().getContent();
                visitedUrls.add(currentAbsoluteUrl);

                fetchedUrls.addAll(fetchUrls(extractAuctionUrlPattern, currentPageContent));
                fetchedUrls.removeAll(visitedUrls);

                //if urls not found continue crawling
                if (fetchedUrls.isEmpty() && startDepth != getMaxCrawlDepth()) {
                    Set<String> urlsToCrawl = fetchUrls(urlsToCrawlPattern, currentPageContent);
                    urlsToCrawl.removeAll(visitedUrls);
                    urlsToCrawl.forEach(url -> fetchedUrls.addAll(collectAuctionUrls(url, startDepth + 1, urlsToCrawlPattern, extractAuctionUrlPattern)));
                }

            } catch (IOException e) {
                LOGGER.error("Error get content of [{}]", currentAbsoluteUrl, e);
            }
        }

        fetchedUrls.removeAll(visitedUrls);
        return fetchedUrls;
    }

    private String getAbsoluteUrl(final String currentUrl) {
        if (!currentUrl.startsWith("http")) {
            return getBaseUrl() + currentUrl;
        }
        return currentUrl;
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

    abstract String getStartURL();

    abstract String getBaseUrl();

    abstract int getMaxCrawlDepth();

    abstract Pattern getAuctionUrlPattern();

    abstract Pattern getContainerPageUrlPattern();

    abstract String getSourceName();

    private void cleanup() {
        this.visitedUrls.clear();
    }

}
