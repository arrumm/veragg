package com.veragg.website.crawler.mapping;

import com.veragg.website.domain.AuctionDraft;

public interface AuctionMapperService<T> {

    AuctionDraft map(T auctionModel);

}
