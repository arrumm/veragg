package com.veragg.website.crawler.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseAuctionDTO {
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
    private List<String> imageLinks = new ArrayList<>();
    private List<String> expertiseLinks = new ArrayList<>();
    private Set<String> otherDocumentLinks = new HashSet<>();
}
