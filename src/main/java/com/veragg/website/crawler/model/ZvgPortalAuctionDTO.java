package com.veragg.website.crawler.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ZvgPortalAuctionDTO extends BaseAuctionDTO {

    @Builder.Default
    private List<ZvgLotAmountDTO> lots = new ArrayList<>();

}
