package com.veragg.website.crawler.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class VersteigerungspoolAuctionDTO extends BaseAuctionDTO {
    public VersteigerungspoolAuctionDTO(final BaseAuctionDTO baseAuctionDTO) {
        super();
        setCourtName(baseAuctionDTO.getCourtName());
        setFileNumber(baseAuctionDTO.getFileNumber());
        setPropertyTypeName(baseAuctionDTO.getPropertyTypeName());
        setStreetAddress(baseAuctionDTO.getStreetAddress());
        setCityAddress(baseAuctionDTO.getCityAddress());
        setAmount(baseAuctionDTO.getAmount());
        setAppointmentDate(baseAuctionDTO.getAppointmentDate());
        setLimitDescription(baseAuctionDTO.getLimitDescription());
        setSourceUrl(baseAuctionDTO.getSourceUrl());
    }
}
