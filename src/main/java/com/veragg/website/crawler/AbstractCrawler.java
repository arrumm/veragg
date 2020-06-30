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
import com.veragg.website.services.AuctionDraftServiceImpl;

import static com.veragg.website.crawler.InternetUtils.getPageContent;

public abstract class AbstractCrawler implements Crawling {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final Set<String> visitedUrls = new HashSet<>();

    AuctionMapperService auctionMapper;

    AuctionDraftServiceImpl auctionService;

    public AbstractCrawler() {
    }

    @Override
    public void process() {

        Set<String> urlsToFetch = collectUrls(getStartURL(), 0, getMaxCrawlDepth());

        for (String url : urlsToFetch) {
            try {
                String pageData = getPageContent(url);
                BaseAuctionDTO auctionDTO = parseAuction(url, new ByteArrayInputStream(pageData.getBytes()));
                AuctionDraft auctionDraft = auctionMapper.map(auctionDTO);
                auctionService.save(auctionDraft);
            } catch (IOException e) {
                LOGGER.error("Page data fetch error", e);
            } catch (ParseException e) {
                LOGGER.error("Fetched data parse error", e);
            }
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

                    Pattern crawlUrlPattern = getCrawlLinkPattern();
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
     * @param pattern     to fetch urls by
     * @param pageContent to fetch urls from
     * @return set of urls
     */
    private Set<String> fetchUrls(final Pattern pattern, final String pageContent) {
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
     * @param pageData
     */
    abstract BaseAuctionDTO parseAuction(String url, InputStream pageData) throws IOException;

    abstract String getStartURL();

    abstract int getMaxCrawlDepth();

    abstract Pattern getExtractLinkPattern();

    abstract Pattern getCrawlLinkPattern();

}
