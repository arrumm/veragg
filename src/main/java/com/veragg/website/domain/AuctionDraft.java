package com.veragg.website.domain;

import javax.persistence.ManyToOne;

public class AuctionDraft extends Auction {

    @ManyToOne
    private Auction conflictAuction;

    //TODO: some timestamp auto set



}
