package com.veragg.website.model;

import org.springframework.stereotype.Service;

import com.veragg.website.domain.Auction;

@Service
public class AuctionDTOMapperService {

    public AuctionDTO map(Auction auction) {
        return AuctionDTO.builder().id(auction.getId()).address(auction.getAddress()).amount(auction.getAmount()).fileNumber(auction.getFileNumber())
                .expertiseDescription(auction.getExpertiseDescription()).outdoorDescription(auction.getOutdoorDescription()).propertyBuildingDescription(auction.getPropertyBuildingDescription())
                .propertyPlotDescription(auction.getPropertyPlotDescription()).appointment(DateConverter.dateToTimestamp(auction.getAppointment())).build();
    }

}
