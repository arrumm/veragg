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
    public AuctionDraft(final @NonNull Court court, final Set<AuctionDraft> drafts, final @NonNull String fileNumber, final @NonNull Set<PropertyType> propertyTypes, final @NonNull Address address,
            final LocalDateTime appointment, final @NonNull Integer amount, final @NonNull BuyLimit buyLimit, final String outdoorDescription, final String propertyBuildingDescription,
            final String propertyPlotDescription, final String expertiseDescription, final List<String> imageLinks, final List<String> expertiseReportLinks, final Set<String> otherFileLinks,
            final Auction conflictAuction) {
        super(court, drafts, fileNumber, propertyTypes, address, appointment, amount, buyLimit, outdoorDescription, propertyBuildingDescription, propertyPlotDescription, expertiseDescription,
                imageLinks, expertiseReportLinks, otherFileLinks);
        this.conflictAuction = conflictAuction;
    }

}
