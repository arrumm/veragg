package com.veragg.website.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.veragg.website.crawler.mapping.AuctionMapperService;
import com.veragg.website.crawler.model.HanmarkAuctionDTO;

import static java.util.Objects.isNull;

@Component
public class HanmarkCrawler extends AbstractCrawler {

    private Pattern LINK_CRAWL_PATTERN;
    private Pattern LINK_EXTRACT_PATTERN;

    private static final String START_URL = "https://www.hanmark.de/amtsgerichte.html";
    private static final String LINK_CRAWL_REGEXP = "(https://www.hanmark.de/).+(.html)";
    private static final String LINK_EXTRACT_REGEXP = "(https://www.hanmark.de/wertgutachten-)([0-9])+(.html)";

    private static final String COURT_XPATH = ".block > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > strong:nth-child(1)";
    private static final String FILE_CSS_PATH = ".block > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2) > strong:nth-child(1)";
    private static final String TYPE_CSS_PATH = ".block > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(2) > strong:nth-child(1)";
    private static final String STREET_CSS_PATH = ".block > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(2) > strong:nth-child(1)";
    private static final String CITY_CSS_PATH = ".block > tbody:nth-child(1) > tr:nth-child(5) > td:nth-child(2) > strong:nth-child(1)";
    private static final String AMOUNT_CSS_PATH = ".block > tbody:nth-child(1) > tr:nth-child(6) > td:nth-child(2) > strong:nth-child(1)";
    private static final String APPOINTMENT_DATE_CSS_PATH = ".block > tbody:nth-child(1) > tr:nth-child(7) > td:nth-child(2) > strong:nth-child(1)";

    private static final String DESCRIPTION_BLOCK_CSS_PATH = "#excerpt";

    private static final String EXPERTISE_DESCRIPTION_NAME = "Gutachten";
    private static final String PLOT_DESCRIPTION_NAME = "Grundstücksbeschreibung";
    private static final String BUILDING_DESCRIPTION_NAME = "Gebäudebeschreibung";
    private static final String OUTDOOR_DESCRIPTION_NAME = "Außenanlagen";

    private static final String PARAGRAPH_TAG_NAME = "p";

    private static final Set<String> DESCRIPTION_KEYWORDS = new HashSet<>(Arrays.asList(EXPERTISE_DESCRIPTION_NAME, PLOT_DESCRIPTION_NAME, BUILDING_DESCRIPTION_NAME, OUTDOOR_DESCRIPTION_NAME));

    @Autowired
    public HanmarkCrawler(AuctionMapperService<HanmarkAuctionDTO> mapperService) {
        this.auctionMapper = mapperService;
    }

    @Scheduled(fixedDelay = 10 * 60 * 1_000)
    public void start() {
        process();
    }

    @Override
    HanmarkAuctionDTO parseAuction(final String url, final InputStream pageData) throws IOException {

        Document doc = Jsoup.parse(pageData, "UTF-8", url);

        //@formatter:off
        final HanmarkAuctionDTO auction = HanmarkAuctionDTO.builder()
                .courtName(getElementTextByPath(doc, COURT_XPATH))
                .fileNumber(getElementTextByPath(doc, FILE_CSS_PATH))
                .propertyTypeName(getElementTextByPath(doc, TYPE_CSS_PATH))
                .streetAddress(getElementTextByPath(doc, STREET_CSS_PATH))
                .cityAddress(getElementTextByPath(doc, CITY_CSS_PATH))
                .amount(getElementTextByPath(doc, AMOUNT_CSS_PATH))
                .appointmentDate(getElementTextByPath(doc, APPOINTMENT_DATE_CSS_PATH)).build();
        //@formatter:on

        Element description = getDescription(doc);
        auction.setExpertDescription(collectDescription(description, EXPERTISE_DESCRIPTION_NAME));
        auction.setPlotDescription(collectDescription(description, PLOT_DESCRIPTION_NAME));
        auction.setBuildingDescription(collectDescription(description, BUILDING_DESCRIPTION_NAME));
        auction.setOutdoorDescription(collectDescription(description, OUTDOOR_DESCRIPTION_NAME));

        return auction;
    }

    private String collectDescription(final Element description, final String descriptionName) {
        Elements descriptionHeaderList = description.getElementsContainingOwnText(descriptionName);
        if (!descriptionHeaderList.isEmpty()) {

            Element descriptionHeader = descriptionHeaderList.get(0).parent();
            int headerPosition = descriptionHeader.elementSiblingIndex();
            Elements elementsBelowDescriptionHeader = description.getElementsByIndexGreaterThan(headerPosition);

            StringBuilder descriptionBuilder = new StringBuilder();

            for (Element descriptionElementCandidate : elementsBelowDescriptionHeader) {
                if (!descriptionElementCandidate.text().isEmpty()) {

                    Elements descriptionElements = descriptionElementCandidate.getElementsByTag(PARAGRAPH_TAG_NAME);

                    final Optional<Element> headerElement = descriptionElementCandidate.getAllElements().stream()//
                            .filter(element -> DESCRIPTION_KEYWORDS.contains(element.ownText()))//
                            .findFirst();//

                    if (headerElement.isPresent()) {
                        //next description found
                        return descriptionBuilder.toString();
                    }

                    for (Element descriptionElement : descriptionElements) {
                        descriptionBuilder.append(descriptionElement.text());
                    }
                    descriptionBuilder.append("\n");

                }

            }
            return descriptionBuilder.toString();
        }
        return StringUtils.EMPTY;
    }

    String getElementTextByPath(Document document, String pathQuery) {
        Elements elements = document.select(pathQuery);
        if (!elements.isEmpty()) {
            return elements.get(0).ownText();
        }

        return "";
    }

    Element getDescription(Document document) {
        Elements elements = document.select(DESCRIPTION_BLOCK_CSS_PATH);
        if (!elements.isEmpty()) {
            return elements.get(0);
        }

        return new Element("empty tag");
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
}
