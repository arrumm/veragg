package com.veragg.website.crawler.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class BaseAuctionModel {
    private String fileNumber;
    private String courtName;
    private String propertyTypeName;
    private String streetAddress;
    private String cityAddress;
    private String amount;
    private String appointmentDate;
    private String expertDescription;
    private String plotDescription;
    private String buildingDescription;
    private String outdoorDescription;
    private List<String> tileImages;
    private String courtAppointmentDescriptionUrl;
    private String limitDescription;

    public BaseAuctionModel(final String fileNumber, final String courtName, final String propertyTypeName, final String streetAddress, final String cityAddress, final String amount,
            final String appointmentDate, final String expertDescription, final String plotDescription, final String buildingDescription, final String outdoorDescription,
            final List<String> tileImages, final String courtAppointmentDescriptionUrl, final String limitDescription) {
        this.fileNumber = fileNumber;
        this.courtName = courtName;
        this.propertyTypeName = propertyTypeName;
        this.streetAddress = streetAddress;
        this.cityAddress = cityAddress;
        this.amount = amount;
        this.appointmentDate = appointmentDate;
        this.expertDescription = expertDescription;
        this.plotDescription = plotDescription;
        this.buildingDescription = buildingDescription;
        this.outdoorDescription = outdoorDescription;
        this.tileImages = tileImages;
        this.courtAppointmentDescriptionUrl = courtAppointmentDescriptionUrl;
        this.limitDescription = limitDescription;
    }
}
