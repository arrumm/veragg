package com.veragg.website.crawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.veragg.website.crawler.model.ZvgLotAmountDTO;
import com.veragg.website.crawler.model.ZvgPortalAuctionDTO;
import com.veragg.website.services.AuctionService;
import com.veragg.website.services.AuctionSourceService;

import static java.util.Objects.nonNull;

@Component
public class ZvgPortalCrawler extends AbstractCrawler {

    private static final String SOURCE_NAME = "ZVG_PORTAL";
    private static final String BASE_URL = "https://www.zvg-portal.de/";
    private static final String SEARCH_FORM_URL = BASE_URL + "index.php?button=Termine+suchen";
    private static final String SEARCH_URL = BASE_URL + "index.php?button=Suchen&all=1";

    //every state offered by the site's own "Land" dropdown
    private static final List<String> STATE_CODES = Arrays.asList("bw", "by", "be", "br", "hb", "hh", "he", "mv", "ni", "nw", "rp", "sl", "sn", "st", "sh", "th");

    private static final String AUCTION_URL_REGEXP = "index\\.php\\?button=showZvg&zvg_id=\\d+&land_abk=\\w+";
    private final Pattern AUCTION_URL_PATTERN = Pattern.compile(AUCTION_URL_REGEXP);

    private static final String ANZEIGE_TABLE_ROWS_CSS_PATH = "table#anzeige tr";
    private static final String MICRONAVI_CSS_PATH = "#micronavi ul";

    private static final String OBJEKT_LAGE_LABEL = "Objekt/Lage";
    private static final String BESCHREIBUNG_LABEL = "Beschreibung";
    private static final String VERKEHRSWERT_LABEL_PREFIX = "Verkehrswert";
    private static final String TERMIN_LABEL = "Termin";
    private static final String ANHANG_ARIA_LABEL_CSS_PATH = "a[aria-label=Anhang]";

    private static final Pattern COURT_NAME_PATTERN = Pattern.compile("Amtsgericht:\\s*(.+?)\\s+in\\s+.+");
    private static final Pattern LOT_AMOUNT_PATTERN = Pattern.compile("Lfd\\.\\s*Nr\\.\\s*(\\d+)\\s*:\\s*(.+)");

    @Autowired
    public ZvgPortalCrawler(AuctionMapperService<ZvgPortalAuctionDTO> mapperService, AuctionService auctionService, AuctionSourceService auctionSourceService) {
        this.auctionMapper = mapperService;
        this.auctionService = auctionService;
        this.auctionSourceService = auctionSourceService;
    }

    @Override
    protected Set<String> collectAuctionUrls() {
        Set<String> auctionUrls = new HashSet<>();
        for (String stateCode : STATE_CODES) {
            try {
                PageData listingPage = new PageData(SEARCH_URL, getReferer()).post(buildSearchParams(stateCode));
                fetchUrls(getAuctionUrlPattern(), listingPage.getContent()).forEach(match -> auctionUrls.add(BASE_URL + match));
            } catch (IOException e) {
                LOGGER.error("Listing fetch for state [{}] failed", stateCode, e);
            }
        }
        return auctionUrls;
    }

    private Map<String, String> buildSearchParams(String stateCode) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("ger_name", "-- Alle Amtsgerichte --");
        params.put("order_by", "2");
        params.put("land_abk", stateCode);
        params.put("ger_id", "0");
        params.put("az1", StringUtils.EMPTY);
        params.put("az2", StringUtils.EMPTY);
        params.put("az3", StringUtils.EMPTY);
        params.put("az4", StringUtils.EMPTY);
        params.put("art", StringUtils.EMPTY);
        params.put("obj", StringUtils.EMPTY);
        params.put("str", StringUtils.EMPTY);
        params.put("hnr", StringUtils.EMPTY);
        params.put("plz", StringUtils.EMPTY);
        params.put("ort", StringUtils.EMPTY);
        params.put("ortsteil", StringUtils.EMPTY);
        params.put("vtermin", StringUtils.EMPTY);
        params.put("btermin", StringUtils.EMPTY);
        return params;
    }

    @Override
    ZvgPortalAuctionDTO fetchAuction(PageData pageData) throws IOException {

        String baseUri = pageData.getUrl();
        Document doc = Jsoup.parse(new ByteArrayInputStream(pageData.getContent().getBytes(StandardCharsets.UTF_8)), "UTF-8", baseUri);

        Map<String, Element> rowsByLabel = new HashMap<>();
        for (Element row : doc.select(ANZEIGE_TABLE_ROWS_CSS_PATH)) {
            Elements cells = row.select("> td");
            if (cells.size() >= 2) {
                rowsByLabel.put(normalizeText(cells.get(0).text()).replace(":", StringUtils.EMPTY).trim(), cells.get(1));
            }
        }

        Element objektLageCell = getRowByLabel(rowsByLabel, OBJEKT_LAGE_LABEL);
        String[] address = splitObjektLage(objektLageCell);

        //@formatter:off
        ZvgPortalAuctionDTO auctionDTO = ZvgPortalAuctionDTO.builder()
                .fileNumber(getFileNumber(doc))
                .courtName(getCourtName(doc))
                .propertyTypeName(getPropertyTypeName(objektLageCell))
                .streetAddress(address[0])
                .cityAddress(address[1])
                .expertDescription(getRowText(rowsByLabel, BESCHREIBUNG_LABEL))
                .appointmentDate(getRowText(rowsByLabel, TERMIN_LABEL))
                .lots(getLots(rowsByLabel))
                .sourceUrl(baseUri)
                .build();
        //@formatter:on

        collectAttachments(doc, auctionDTO);

        return auctionDTO;
    }

    private String[] splitObjektLage(Element objektLageCell) {
        if (nonNull(objektLageCell)) {
            String addressRaw = normalizeText(objektLageCell.ownText()).trim();
            int splitIndex = addressRaw.indexOf(',');
            if (splitIndex > -1) {
                return new String[] { addressRaw.substring(0, splitIndex).trim(), addressRaw.substring(splitIndex + 1).trim() };
            }
            return new String[] { addressRaw, StringUtils.EMPTY };
        }
        return new String[] { StringUtils.EMPTY, StringUtils.EMPTY };
    }

    private String getPropertyTypeName(Element objektLageCell) {
        if (nonNull(objektLageCell)) {
            Element typeElement = objektLageCell.selectFirst("strong");
            if (nonNull(typeElement)) {
                return normalizeText(typeElement.text()).replaceAll(":\\s*$", StringUtils.EMPTY).trim();
            }
        }
        return StringUtils.EMPTY;
    }

    private String getFileNumber(Document doc) {
        Element fileNumberElement = doc.selectFirst(ANZEIGE_TABLE_ROWS_CSS_PATH + ":eq(0) strong");
        return nonNull(fileNumberElement) ? normalizeText(fileNumberElement.text()).replaceAll("\\s+", " ").trim() : StringUtils.EMPTY;
    }

    private String getCourtName(Document doc) {
        Element micronavi = doc.selectFirst(MICRONAVI_CSS_PATH);
        if (nonNull(micronavi)) {
            Matcher m = COURT_NAME_PATTERN.matcher(normalizeText(micronavi.text()));
            if (m.find()) {
                return m.group(1).trim();
            }
        }
        return StringUtils.EMPTY;
    }

    private String getRowText(Map<String, Element> rowsByLabel, String label) {
        Element valueCell = getRowByLabel(rowsByLabel, label);
        return nonNull(valueCell) ? normalizeText(valueCell.text()).trim() : StringUtils.EMPTY;
    }

    private Element getRowByLabel(Map<String, Element> rowsByLabel, String label) {
        return rowsByLabel.entrySet().stream()//
                .filter(entry -> entry.getKey().startsWith(label))//
                .map(Map.Entry::getValue)//
                .findFirst()//
                .orElse(null);
    }

    private List<ZvgLotAmountDTO> getLots(Map<String, Element> rowsByLabel) {
        List<ZvgLotAmountDTO> lots = new ArrayList<>();
        Element verkehrswertCell = getRowByLabel(rowsByLabel, VERKEHRSWERT_LABEL_PREFIX);
        if (nonNull(verkehrswertCell)) {
            for (Element paragraph : verkehrswertCell.select("p")) {
                String text = normalizeText(paragraph.text()).trim();
                if (text.isEmpty()) {
                    continue;
                }
                Matcher m = LOT_AMOUNT_PATTERN.matcher(text);
                lots.add(m.find() ? new ZvgLotAmountDTO(m.group(1), m.group(2).trim()) : new ZvgLotAmountDTO(null, text));
            }
        }
        return lots;
    }

    private void collectAttachments(Document doc, ZvgPortalAuctionDTO auctionDTO) {
        for (Element row : doc.select(ANZEIGE_TABLE_ROWS_CSS_PATH)) {
            Elements cells = row.select("> td");
            if (cells.size() < 2) {
                continue;
            }
            Element link = cells.get(1).selectFirst(ANHANG_ARIA_LABEL_CSS_PATH);
            if (nonNull(link)) {
                String href = link.absUrl("href").trim();
                String label = normalizeText(cells.get(0).text()).toLowerCase();
                if (label.contains("gutachten")) {
                    auctionDTO.getExpertiseLinks().add(href);
                } else if (label.contains("foto")) {
                    auctionDTO.getImageLinks().add(href);
                } else {
                    auctionDTO.getOtherDocumentLinks().add(href);
                }
            }
        }
    }

    private String normalizeText(String text) {
        return text.replace('\u00A0', ' ');
    }

    @Override
    Pattern getAuctionUrlPattern() {
        return AUCTION_URL_PATTERN;
    }

    @Override
    protected String getReferer() {
        return SEARCH_FORM_URL;
    }

    @Override
    String getSourceName() {
        return SOURCE_NAME;
    }
}
