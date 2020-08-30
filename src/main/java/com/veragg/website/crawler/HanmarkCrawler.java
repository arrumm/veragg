package com.veragg.website.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.veragg.website.crawler.mapping.AuctionMapperService;
import com.veragg.website.crawler.model.HanmarkAuctionDTO;
import com.veragg.website.services.AuctionService;
import com.veragg.website.services.AuctionSourceService;

import static java.util.Objects.nonNull;

@Component
public class HanmarkCrawler extends AbstractCrawler {

    private static final String SOURCE_NAME = "HANMARK";
    private static final String START_URL = "https://www.hanmark.de/amtsgerichte.html";
    private static final String LINK_CRAWL_REGEXP = "(https://www.hanmark.de/).+(.html)";
    private static final String LINK_EXTRACT_REGEXP = "(https://www.hanmark.de/wertgutachten-)([0-9])+(.html)";

    private final Pattern LINK_CRAWL_PATTERN = Pattern.compile(LINK_CRAWL_REGEXP);
    private final Pattern LINK_EXTRACT_PATTERN = Pattern.compile(LINK_EXTRACT_REGEXP);

    private static final String TBODY_OVERVIEW = ".block > tbody:nth-child(1)";
    private static final String COURT_XPATH = TBODY_OVERVIEW + " > tr:nth-child(1) > td:nth-child(2) > strong";
    private static final String FILE_NUMBER_CSS_PATH = TBODY_OVERVIEW + " > tr:nth-child(2) > td:nth-child(2) > strong";
    private static final String TYPE_CSS_PATH = TBODY_OVERVIEW + " > tr:nth-child(3) > td:nth-child(2) > strong";
    private static final String STREET_CSS_PATH = TBODY_OVERVIEW + " > tr:nth-child(4) > td:nth-child(2) > strong";
    private static final String CITY_CSS_PATH = TBODY_OVERVIEW + " > tr:nth-child(5) > td:nth-child(2) > strong";
    private static final String AMOUNT_CSS_PATH = TBODY_OVERVIEW + " > tr:nth-child(6) > td:nth-child(2) > strong";
    private static final String APPOINTMENT_DATE_CSS_PATH = TBODY_OVERVIEW + " > tr:nth-child(7) > td:nth-child(2) > strong";
    private static final String LIMIT_CSS_PATH = TBODY_OVERVIEW + " > tr:nth-child(8) > td:nth-child(2) > strong";

    private static final String TILE_IMAGE_CSS_PATH = "#image > img";
    private static final String IMAGE_CSS_PATH = ".images > li:nth-child(%s) > div > img";

    private static final String DOWNLOAD_FILES_CSS_PATH = "a.button:nth-child(%s)";

    private static final String EXPERTISE_FILE_EXTRACT_REGEXP = "(https://www.hanmark.de/wertgutachten-)([0-9])+(.pdf)";
    private final Pattern EXPERTISE_FILE_LINK_PATTERN = Pattern.compile(EXPERTISE_FILE_EXTRACT_REGEXP, Pattern.CASE_INSENSITIVE);

    private static final String OTHER_FILES_EXTRACT_REGEXP = "(https:\\/\\/www.hanmark.de\\/)((?!wertgutachten-).)*(.pdf)";
    private final Pattern OTHER_FILE_LINK_PATTERN = Pattern.compile(OTHER_FILES_EXTRACT_REGEXP, Pattern.CASE_INSENSITIVE);

    private static final String DESCRIPTION_BLOCK_CSS_PATH = "#excerpt";

    private static final String EXPERTISE_DESCRIPTION_NAME = "Gutachten";
    private static final String PLOT_DESCRIPTION_NAME = "Grundstücksbeschreibung";
    private static final String BUILDING_DESCRIPTION_NAME = "Gebäudebeschreibung";
    private static final String OUTDOOR_DESCRIPTION_NAME = "Außenanlagen";

    private static final String PARAGRAPH_TAG_NAME = "p";

    private static final Set<String> DESCRIPTION_KEYWORDS = new HashSet<>(Arrays.asList(EXPERTISE_DESCRIPTION_NAME, PLOT_DESCRIPTION_NAME, BUILDING_DESCRIPTION_NAME, OUTDOOR_DESCRIPTION_NAME));

    @Autowired
    public HanmarkCrawler(AuctionMapperService<HanmarkAuctionDTO> mapperService, AuctionService auctionService, AuctionSourceService auctionSourceService) {
        this.auctionMapper = mapperService;
        this.auctionService = auctionService;
        this.auctionSourceService = auctionSourceService;
    }

    @Override
    HanmarkAuctionDTO fetchAuction(final InputStream pageData, final String baseUri) throws IOException {

        Document doc = Jsoup.parse(pageData, "UTF-8", baseUri);

        //@formatter:off
        final HanmarkAuctionDTO auctionDTO = HanmarkAuctionDTO.builder()
                .courtName(getElementTextByPath(doc, COURT_XPATH))
                .fileNumber(getElementTextByPath(doc, FILE_NUMBER_CSS_PATH))
                .propertyTypeName(getElementTextByPath(doc, TYPE_CSS_PATH))
                .streetAddress(getElementTextByPath(doc, STREET_CSS_PATH))
                .cityAddress(getElementTextByPath(doc, CITY_CSS_PATH))
                .amount(getElementTextByPath(doc, AMOUNT_CSS_PATH))
                .appointmentDate(getElementTextByPath(doc, APPOINTMENT_DATE_CSS_PATH))
                .limitDescription(getElementTextByPath(doc, LIMIT_CSS_PATH))
                .sourceUrl(baseUri).build();
        //@formatter:on

        Element description = getDescription(doc);
        auctionDTO.setExpertDescription(collectDescription(description, EXPERTISE_DESCRIPTION_NAME));
        auctionDTO.setPlotDescription(collectDescription(description, PLOT_DESCRIPTION_NAME));
        auctionDTO.setBuildingDescription(collectDescription(description, BUILDING_DESCRIPTION_NAME));
        auctionDTO.setOutdoorDescription(collectDescription(description, OUTDOOR_DESCRIPTION_NAME));

        auctionDTO.setExpertiseLinks(getFilesLinks(doc, EXPERTISE_FILE_LINK_PATTERN));
        auctionDTO.setOtherDocumentLinks(new HashSet<>(getFilesLinks(doc, OTHER_FILE_LINK_PATTERN)));
        auctionDTO.setImageLinks(getImageLinks(doc));

        return auctionDTO;
    }

    private List<String> getImageLinks(Document doc) {
        List<String> imageLinks = new ArrayList<>();

        String tileImageLink = getElementSourceByPath(doc, TILE_IMAGE_CSS_PATH);
        if (StringUtils.isNotEmpty(tileImageLink)) {
            imageLinks.add(tileImageLink);
        }

        int liIterator = 1;
        String nextImageLink = getElementSourceByPath(doc, String.format(IMAGE_CSS_PATH, liIterator));
        while (StringUtils.isNotEmpty(nextImageLink)) {
            imageLinks.add(nextImageLink);
            nextImageLink = getElementSourceByPath(doc, String.format(IMAGE_CSS_PATH, ++liIterator));
        }
        return imageLinks;
    }

    String getElementSourceByPath(Document document, String pathQuery) {
        return getAttributeValueFromPath(document, pathQuery, "src");
    }

    private List<String> getFilesLinks(Document doc, Pattern extractLinkPattern) {
        List<String> fileLinks = new ArrayList<>();

        int liIterator = 2;
        String nextFileLink = getElementLinkByPath(doc, String.format(DOWNLOAD_FILES_CSS_PATH, liIterator));

        while (StringUtils.isNotEmpty(nextFileLink)) {
            Matcher m = extractLinkPattern.matcher(nextFileLink);
            if (m.find()) {
                fileLinks.add(m.group());
            }
            nextFileLink = getElementLinkByPath(doc, String.format(DOWNLOAD_FILES_CSS_PATH, ++liIterator));
        }

        return fileLinks;
    }

    String getElementLinkByPath(Document document, String pathQuery) {
        return getAttributeValueFromPath(document, pathQuery, "href");
    }

    private String getAttributeValueFromPath(Document document, String pathQuery, String href) {
        Element elementFound = document.selectFirst(pathQuery);
        return nonNull(elementFound) ? elementFound.attr(href) : StringUtils.EMPTY;
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
        Element elementFound = document.selectFirst(pathQuery);
        return nonNull(elementFound) ? elementFound.ownText() : StringUtils.EMPTY;
    }

    Element getDescription(Document document) {
        Element elementFound = document.selectFirst(DESCRIPTION_BLOCK_CSS_PATH);
        return nonNull(elementFound) ? elementFound : new Element("empty tag");
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
    Pattern getAuctionUrlPattern() {
        return LINK_EXTRACT_PATTERN;
    }

    @Override
    Pattern getContainerPageUrlPattern() {
        return LINK_CRAWL_PATTERN;
    }

    @Override
    String getSourceName() {
        return SOURCE_NAME;
    }
}
