package com.veragg.website.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AuctionDraft extends BaseAuction {

    @Builder(builderMethodName = "draftBuilder")
    public AuctionDraft(@NonNull Court court, @NonNull String fileNumber, @NonNull Set<PropertyType> propertyTypes, @NonNull Address address, LocalDateTime appointment, @NonNull Integer amount,
            @NonNull BuyLimit buyLimit, String outdoorDescription, String propertyBuildingDescription, String propertyPlotDescription, String expertiseDescription, String sourceUrl,
            List<Document<?>> documents) {
        super(court, fileNumber, propertyTypes, address, appointment, amount, buyLimit, outdoorDescription, propertyBuildingDescription, propertyPlotDescription, expertiseDescription, sourceUrl, null,
                documents);
    }

}
