package com.veragg.website.model;

import java.util.List;

import com.veragg.website.domain.Address;
import com.veragg.website.domain.BuyLimit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuctionDTO {

    private Long id;
    private Long courtId;
    private String fileNumber;
    private String propertyTypes;
    private Address address;
    private Long appointment;
    private Integer amount;
    private BuyLimit buyLimit;
    private String outdoorDescription;
    private String propertyBuildingDescription;
    private String propertyPlotDescription;
    private String expertiseDescription;
    private String sourceUrl;
    //TODO: generate name from storage name and mask
    //TODO: create a redirect to static folder
    private List<String> images;
    private List<String> expertise;
    private List<String> otherDocuments;

}
