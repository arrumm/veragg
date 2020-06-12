package com.veragg.website.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AuctionDraft extends Auction {

    @ManyToOne
    private Auction conflictAuction;

    //TODO: some timestamp auto set

}
