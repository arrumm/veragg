package com.veragg.website.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class AuctionDraft extends Auction {

    @ManyToOne
    private Auction conflictAuction;

    //TODO: some timestamp auto set



}
