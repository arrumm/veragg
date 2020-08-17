package com.veragg.website.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AuctionDraft extends Auction {

    @ManyToOne
    private Auction conflictAuction;

    @Builder(builderMethodName = "draftBuilder")
    public AuctionDraft(@NonNull Court court, Set<AuctionDraft> drafts, @NonNull String fileNumber, @NonNull Set<PropertyType> propertyTypes, @NonNull Address address, LocalDateTime appointment,
            @NonNull Integer amount, @NonNull BuyLimit buyLimit, String outdoorDescription, String propertyBuildingDescription, String propertyPlotDescription, String expertiseDescription,
            List<String> imageLinks, List<String> expertiseReportLinks, Set<String> otherFileLinks, Auction conflictAuction, String sourceUrl) {
        super(court, drafts, fileNumber, propertyTypes, address, appointment, amount, buyLimit, outdoorDescription, propertyBuildingDescription, propertyPlotDescription, expertiseDescription,
                imageLinks, expertiseReportLinks, otherFileLinks, sourceUrl);
        this.conflictAuction = conflictAuction;
    }

}
