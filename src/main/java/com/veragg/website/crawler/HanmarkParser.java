package com.veragg.website.crawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.veragg.website.crawler.model.HanmarkAuctionDTO;

import static java.util.Objects.nonNull;

@Service("hanmarkParser")
@Primary
public class HanmarkParser implements Parsing<HanmarkAuctionDTO> {

    private static final String TBODY_OVERVIEW = ".block > tbody:nth-child(1)";
    private static final String COURT_CSS_PATH = TBODY_OVERVIEW + " > tr:nth-child(1) > td:nth-child(2) > strong";
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

    @Override
    public HanmarkAuctionDTO parse(final Document htmlDocument) {

        final HanmarkAuctionDTO auctionDTO = new HanmarkAuctionDTO(parseAuctionBase(htmlDocument));

        Element description = getDescription(htmlDocument);
        auctionDTO.setExpertDescription(collectDescription(description, EXPERTISE_DESCRIPTION_NAME));
        auctionDTO.setPlotDescription(collectDescription(description, PLOT_DESCRIPTION_NAME));
        auctionDTO.setBuildingDescription(collectDescription(description, BUILDING_DESCRIPTION_NAME));
        auctionDTO.setOutdoorDescription(collectDescription(description, OUTDOOR_DESCRIPTION_NAME));

        // TODO Roman: 06-Apr-21 additional info description

        auctionDTO.setExpertiseLinks(getFilesLinks(htmlDocument, EXPERTISE_FILE_LINK_PATTERN));
        auctionDTO.setOtherDocumentLinks(new HashSet<>(getFilesLinks(htmlDocument, OTHER_FILE_LINK_PATTERN)));
        auctionDTO.setImageLinks(getImageLinks(htmlDocument));

        return auctionDTO;
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

    private List<String> getImageLinks(final Document doc) {
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

    private List<String> getFilesLinks(final Document doc, final Pattern extractLinkPattern) {
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

    Element getDescription(final Document htmlDocument) {
        Element elementFound = htmlDocument.selectFirst(DESCRIPTION_BLOCK_CSS_PATH);
        return nonNull(elementFound) ? elementFound : new Element("empty tag");
    }

    String getElementLinkByPath(final Document htmlDocument, final String pathQuery) {
        return getAttributeValueFromPath(htmlDocument, pathQuery, "href");
    }

    String getElementSourceByPath(final Document htmlDocument, final String pathQuery) {
        return getAttributeValueFromPath(htmlDocument, pathQuery, "src");
    }

    private String getAttributeValueFromPath(final Document htmlDocument, final String pathQuery, final String href) {
        Element elementFound = htmlDocument.selectFirst(pathQuery);
        return nonNull(elementFound) ? elementFound.attr(href) : StringUtils.EMPTY;
    }

    public String getCourtPath() {
        return COURT_CSS_PATH;
    }

    public String getFileNumberPath() {
        return FILE_NUMBER_CSS_PATH;
    }

    public String getTypePath() {
        return TYPE_CSS_PATH;
    }

    public String getStreetAddressPath() {
        return STREET_CSS_PATH;
    }

    public String getCityAddressPath() {
        return CITY_CSS_PATH;
    }

    public String getAmountPath() {
        return AMOUNT_CSS_PATH;
    }

    public String getAppointmentDatePath() {
        return APPOINTMENT_DATE_CSS_PATH;
    }

    public String getLimitPath() {
        return LIMIT_CSS_PATH;
    }

}
