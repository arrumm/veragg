package com.veragg.website.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.veragg.website.crawler.mapping.HanmarkAuctionMapperServiceImpl;
import com.veragg.website.crawler.model.HanmarkAuctionModel;
import com.veragg.website.services.CourtService;

import static java.util.Objects.isNull;

@Component
public class HanmarkCrawler extends AbstractCrawler implements ApplicationListener<ContextRefreshedEvent> {

    private final String START_URL = "https://www.hanmark.de/amtsgerichte.html";
    private final String LINK_CRAWL_REGEXP = "(https://www.hanmark.de/).+(.html)";
    private final String LINK_EXTRACT_REGEXP = "(https://www.hanmark.de/wertgutachten-)([0-9])+(.html)";

    private final static String COURT_XPATH = ".block > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > strong:nth-child(1)";
    private final static String FILE_CSS_PATH = ".block > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2) > strong:nth-child(1)";
    private final static String TYPE_CSS_PATH = ".block > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(2) > strong:nth-child(1)";
    private final static String STREET_CSS_PATH = ".block > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(2) > strong:nth-child(1)";
    private final static String CITY_CSS_PATH = ".block > tbody:nth-child(1) > tr:nth-child(5) > td:nth-child(2) > strong:nth-child(1)";
//    private final static String AMOUNT_XPATH = "//*[@id=\"content\"]/div[2]/div/table/tbody/tr[6]/td";
//    private final static String DATE_XPATH = "//*[@id=\"content\"]/div[2]/div/table/tbody/tr[7]/td";

    private Pattern LINK_CRAWL_PATTERN;
    private Pattern LINK_EXTRACT_PATTERN;

    private CourtService courtService;

    public HanmarkCrawler(final CourtService courtService) {
        this.courtService = courtService;
        this.mapperService = new HanmarkAuctionMapperServiceImpl();
    }

    @Override
    HanmarkAuctionModel parseAuction(final String url, final InputStream pageData) throws IOException {

        final HanmarkAuctionModel auction = new HanmarkAuctionModel();
        Document doc = Jsoup.parse(pageData, "UTF-8", url);

        auction.setCourtName(getByPath(doc, COURT_XPATH));
        auction.setFileNumber(getByPath(doc, FILE_CSS_PATH));
        auction.setPropertyTypeName(getByPath(doc, TYPE_CSS_PATH));

//        auction.setCourt(courtService.getByName(getByPath(doc, COURT_XPATH)));
//        auction.setPropertyType(PropertyType.getByName(getByPath(doc, TYPE_CSS_PATH)));



        return auction;
    }

    String getByPath(Document document, String pathQuery) {
        Elements elements = document.select(pathQuery);
        if (!elements.isEmpty()) {
            return elements.get(0).ownText();
        }

        return "";
    }

    @Override
    String getStartURL() {
        return START_URL;
    }

    @Override
    int getMaxCrawlDepth() {
        return 2;
    }

    @Override
    Pattern getExtractLinkPattern() {
        if (isNull(LINK_EXTRACT_PATTERN)) {
            LINK_EXTRACT_PATTERN = Pattern.compile(LINK_EXTRACT_REGEXP);
        }
        return LINK_EXTRACT_PATTERN;
    }

    @Override
    Pattern getCrawlLinkPattern() {
        if (isNull(LINK_CRAWL_PATTERN)) {
            LINK_CRAWL_PATTERN = Pattern.compile(LINK_CRAWL_REGEXP);
        }
        return LINK_CRAWL_PATTERN;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        process();
    }
}
