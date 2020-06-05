package com.veragg.website.crawler.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
