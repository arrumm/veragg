package com.veragg.website.crawler.mapping;

import java.text.ParseException;

import com.veragg.website.crawler.model.BaseAuctionDTO;
import com.veragg.website.domain.AuctionDraft;

public interface AuctionMapperService<S extends BaseAuctionDTO> {

    AuctionDraft map(S auctionDTO) throws ParseException;

}
