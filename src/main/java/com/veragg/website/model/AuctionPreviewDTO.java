package com.veragg.website.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuctionPreviewDTO {

    private Long id;
    @JsonProperty("file_number")
    private String fileNumber;
    @JsonProperty("property_type")
    private String propertyType;
    private String city;
    private String street;
    private String number;
    @JsonProperty("zip_code")
    private String zipCode;
    private Long appointment;
    private Integer amount;
    //TODO: need to implement tiles converter
    @JsonProperty("tile_image")
    private String tileImage;

}
