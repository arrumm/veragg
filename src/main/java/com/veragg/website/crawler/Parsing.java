package com.veragg.website.crawler;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.veragg.website.crawler.model.BaseAuctionDTO;

import static java.util.Objects.nonNull;

public interface Parsing<T extends BaseAuctionDTO> {

    T parse(Document document);

    default BaseAuctionDTO parseAuctionBase(Document htmlDocument) {

        final BaseAuctionDTO auctionDTO = new BaseAuctionDTO();
        auctionDTO.setCourtName(getElementTextByPath(htmlDocument, getCourtPath()));
        auctionDTO.setFileNumber(getElementTextByPath(htmlDocument, getFileNumberPath()));
        auctionDTO.setPropertyTypeName(getElementTextByPath(htmlDocument, getTypePath()));
        auctionDTO.setStreetAddress(getElementTextByPath(htmlDocument, getStreetAddressPath()));
        auctionDTO.setCityAddress(getElementTextByPath(htmlDocument, getCityAddressPath()));
        auctionDTO.setAmount(getElementTextByPath(htmlDocument, getAmountPath()));
        auctionDTO.setAppointmentDate(getElementTextByPath(htmlDocument, getAppointmentDatePath()));
        auctionDTO.setLimitDescription(getElementTextByPath(htmlDocument, getLimitPath()));
        auctionDTO.setSourceUrl(htmlDocument.location());

        return auctionDTO;
    }

    String getCourtPath();

    String getFileNumberPath();

    String getTypePath();

    String getStreetAddressPath();

    String getCityAddressPath();

    String getAmountPath();

    String getAppointmentDatePath();

    String getLimitPath();

    private String getElementTextByPath(Document htmlDocument, String pathQuery) {
        Element elementFound = htmlDocument.selectFirst(pathQuery);
        return nonNull(elementFound) ? elementFound.ownText() : StringUtils.EMPTY;
    }

    private String getElementSourceByPath(Document htmlDocument, String pathQuery) {
        return getAttributeValueFromPath(htmlDocument, pathQuery, "src");
    }

    private String getElementLinkByPath(Document htmlDocument, String pathQuery) {
        return getAttributeValueFromPath(htmlDocument, pathQuery, "href");
    }

    private String getAttributeValueFromPath(Document htmlDocument, String pathQuery, String href) {
        Element elementFound = htmlDocument.selectFirst(pathQuery);
        return nonNull(elementFound) ? elementFound.attr(href) : StringUtils.EMPTY;
    }

}
