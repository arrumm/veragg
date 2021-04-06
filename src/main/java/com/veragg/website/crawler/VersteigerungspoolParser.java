package com.veragg.website.crawler;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.veragg.website.crawler.model.VersteigerungspoolAuctionDTO;

import static java.util.Objects.nonNull;

@Service("versteigerungspoolParser")
public class VersteigerungspoolParser implements Parsing<VersteigerungspoolAuctionDTO> {

    private static final String COURT_CSS_PATH = ".copyThisFromDesktop > div:nth-child(2) > p:nth-child(1) > strong:nth-child(1)";
    private static final String FILE_NUMBER_CSS_PATH = "div.expose:nth-child(2) > dl:nth-child(7) > dd:nth-child(4)";
    private static final String TYPE_CSS_PATH = "div.expose:nth-child(2) > dl:nth-child(7) > dd:nth-child(8)";

    private static final String ADDRESS_CSS_PATH = ".copyThisFromDesktop > div:nth-child(11) > div:nth-child(1) > dl:nth-child(1) > dd:nth-child(2)";
    private static final String AMOUNT_CSS_PATH = "div.expose:nth-child(2) > dl:nth-child(7) > dd:nth-child(10)";
    private static final String APPOINTMENT_DATE_CSS_PATH = "div.expose:nth-child(2) > dl:nth-child(7) > dd:nth-child(6)";
    private static final String LIMIT_CSS_PATH = "div.expose:nth-child(2) > dl:nth-child(7) > dd:nth-child(12)";

    private static final String DESCRIPTION_BLOCK_CSS_PATH = "#layout > div.mainBox";
    private static final String EXPERTISE_DESCRIPTION_NAME = "Objektbeschreibung:";
    private static final String PLOT_DESCRIPTION_NAME = "Lage:";
    private static final String BUILDING_DESCRIPTION_NAME = "Ausstattung:";
    //TODO add a field for it
    private static final String ADDITIONAL_INFO_DESCRIPTION_NAME = "Sonstiges:";

    private Document.OutputSettings outputSettings;

    @Override
    public VersteigerungspoolAuctionDTO parse(Document htmlDocument) {

        outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        htmlDocument.outputSettings(outputSettings);

        final VersteigerungspoolAuctionDTO auctionDTO = new VersteigerungspoolAuctionDTO(parseAuctionBase(htmlDocument));

        Element description = getDescription(htmlDocument);
        auctionDTO.setExpertDescription(extractDescription(description, EXPERTISE_DESCRIPTION_NAME));
        auctionDTO.setPlotDescription(extractDescription(description, PLOT_DESCRIPTION_NAME));
        auctionDTO.setBuildingDescription(extractDescription(description, BUILDING_DESCRIPTION_NAME));
        //        auctionDTO.setOutdoorDescription(extractDescription(description, OUTDOOR_DESCRIPTION_NAME));

        return auctionDTO;
    }

    @Override
    public String getCourtPath() {
        return COURT_CSS_PATH;
    }

    @Override
    public String getFileNumberPath() {
        return FILE_NUMBER_CSS_PATH;
    }

    @Override
    public String getTypePath() {
        return TYPE_CSS_PATH;
    }

    @Override
    public String getStreetAddressPath() {
        return ADDRESS_CSS_PATH;
    }

    @Override
    public String getCityAddressPath() {
        return getStreetAddressPath();
    }

    @Override
    public String getAmountPath() {
        return AMOUNT_CSS_PATH;
    }

    @Override
    public String getAppointmentDatePath() {
        return APPOINTMENT_DATE_CSS_PATH;
    }

    @Override
    public String getLimitPath() {
        return LIMIT_CSS_PATH;
    }

    Element getDescription(final Document htmlDocument) {
        Element elementFound = htmlDocument.selectFirst(DESCRIPTION_BLOCK_CSS_PATH);
        return nonNull(elementFound) ? elementFound : new Element("empty tag");
    }

    private String extractDescription(final Element description, final String descriptionName) {
        Elements descriptionHeaderList = description.getElementsContainingOwnText(descriptionName);
        if (!descriptionHeaderList.isEmpty()) {
            Element descriptionHeader = descriptionHeaderList.get(0);
            Element descriptionElement = descriptionHeader.nextElementSibling();
            descriptionElement.select("br").before("\\n");
            final String descriptionText = descriptionElement.wholeText();
            if (!descriptionText.isEmpty()) {
                String descriptionNewLinesCleaned = descriptionText.replaceAll("\\\\n", "").replaceAll(" {2}", "").trim();
                return Jsoup.clean(descriptionNewLinesCleaned, "", Whitelist.none(), outputSettings);
            }
        }
        return StringUtils.EMPTY;
    }

}
