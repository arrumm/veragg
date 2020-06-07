package com.veragg.website.crawler.model;

import java.util.List;

import lombok.Builder;

public class HanmarkAuctionModel extends BaseAuctionModel {

    public HanmarkAuctionModel() {
        super();
    }

    @Builder
    public HanmarkAuctionModel(final String fileNumber, final String courtName, final String propertyTypeName, final String streetAddress, final String cityAddress, final String amount,
            final String appointmentDate, final String expertDescription, final String plotDescription, final String buildingDescription, final String outdoorDescription,
            final List<String> tileImages, final String courtAppointmentDescriptionUrl, final String limit) {
        super(fileNumber, courtName, propertyTypeName, streetAddress, cityAddress, amount, appointmentDate, expertDescription, plotDescription, buildingDescription, outdoorDescription, tileImages,
                courtAppointmentDescriptionUrl, limit);
    }
}
