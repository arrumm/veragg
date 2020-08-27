package com.veragg.website.services;

import org.springframework.stereotype.Service;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionDraft;

import lombok.NonNull;

@Service
public class AuctionMapperImpl implements AuctionMapper {

    @Override
    public Auction getAuction(@NonNull AuctionDraft auctionDraft) {
        //@formatter:off
        return Auction
                .builder()
                .court(auctionDraft.getCourt())
                .fileNumber(auctionDraft.getFileNumber())
                .propertyTypes(auctionDraft.getPropertyTypes())
                .address(auctionDraft.getAddress())
                .amount(auctionDraft.getAmount())
                .buyLimit(auctionDraft.getBuyLimit())
                .appointment(auctionDraft.getAppointment())
                .outdoorDescription(auctionDraft.getOutdoorDescription())
                .propertyPlotDescription(auctionDraft.getPropertyPlotDescription())
                .expertiseDescription(auctionDraft.getExpertiseDescription())
                .propertyBuildingDescription(auctionDraft.getPropertyBuildingDescription())
                .imageLinks(auctionDraft.getImageLinks())
                .expertiseReportLinks(auctionDraft.getExpertiseLinks())
                .otherFileLinks(auctionDraft.getOtherDocumentLinks())
                .build();
        //@formatter:on
    }

}
