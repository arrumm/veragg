package com.veragg.website.domain;

import java.util.Date;
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
    public AuctionDraft(final @NonNull Court court, final Set<AuctionDraft> drafts, final @NonNull String fileNumber, final @NonNull PropertyType propertyType, final @NonNull Address address,
            final @NonNull Date appointment, final @NonNull Integer amount, final @NonNull BuyLimit buyLimit, final String outdoorDescription, final String propertyBuildingDescription,
            final String propertyPlotDescription, final String expertiseDescription, final List<Document> pictures, final List<Document> tilePictures, final List<Document> expertiseReports,
            final Auction conflictAuction) {
        super(court, drafts, fileNumber, propertyType, address, appointment, amount, buyLimit, outdoorDescription, propertyBuildingDescription, propertyPlotDescription, expertiseDescription, pictures,
                tilePictures, expertiseReports);
        this.conflictAuction = conflictAuction;
    }

    //TODO: some timestamp auto set

}
