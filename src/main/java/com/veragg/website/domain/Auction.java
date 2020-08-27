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

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Auction extends BaseAuction {

    @Builder
    public Auction(@NonNull Court court, @NonNull String fileNumber, @NonNull Set<PropertyType> propertyTypes, @NonNull Address address, LocalDateTime appointment, @NonNull Integer amount,
            @NonNull BuyLimit buyLimit, String outdoorDescription, String propertyBuildingDescription, String propertyPlotDescription, String expertiseDescription, List<String> imageLinks,
            List<String> expertiseReportLinks, Set<String> otherFileLinks, String sourceUrl) {
        super(court, fileNumber, propertyTypes, address, appointment, amount, buyLimit, outdoorDescription, propertyBuildingDescription, propertyPlotDescription, expertiseDescription, imageLinks,
                expertiseReportLinks, otherFileLinks, sourceUrl);
    }

}
