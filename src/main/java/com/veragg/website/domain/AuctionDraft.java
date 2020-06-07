package com.veragg.website.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@Getter
@Setter
public class AuctionDraft extends Auction {

    @ManyToOne
    private Auction conflictAuction;

    @Builder
    public AuctionDraft(@NonNull final Court court, @NonNull final String fileNumber, @NonNull final PropertyType propertyType, @NonNull final Address address, @NonNull final Date appointment,
            @NonNull final Integer amount, @NonNull final Limit limit) {
        super(court, fileNumber, propertyType, address, appointment, amount, limit);
    }

    //TODO: some timestamp auto set

}
