package com.veragg.website.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuctionPreviewDTO {

    private Long id;
    private String fileNumber;
    private String propertyType;
    private String city;
    private String zipCode;
    private Long appointment;
    private Integer amount;
    //TODO: need to implement tiles converter
    private String tileImage;

}
