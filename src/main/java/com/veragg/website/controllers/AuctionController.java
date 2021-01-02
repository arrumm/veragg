package com.veragg.website.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veragg.website.domain.Auction;
import com.veragg.website.model.AuctionDTO;
import com.veragg.website.model.AuctionDTOMapperService;
import com.veragg.website.model.AuctionPreviewDTO;
import com.veragg.website.model.AuctionPreviewDTOMapperService;
import com.veragg.website.services.AuctionService;

@RestController
@RequestMapping("auction")
public class AuctionController {

    private final AuctionService auctionService;
    private final AuctionPreviewDTOMapperService auctionPreviewDTOMapperService;
    private final AuctionDTOMapperService auctionDTOMapperService;

    @Autowired
    public AuctionController(AuctionService auctionService, AuctionPreviewDTOMapperService auctionPreviewDTOMapperService, AuctionDTOMapperService auctionDTOMapperService) {
        this.auctionService = auctionService;
        this.auctionPreviewDTOMapperService = auctionPreviewDTOMapperService;
        this.auctionDTOMapperService = auctionDTOMapperService;
    }

    @GetMapping(value = "/all", produces = "application/json")
    public Map<String, Object> list(@PageableDefault(sort = "appointment") Pageable pageable) {
        Map<String, Object> result = new HashMap<>();

        //@formatter:off
        List<AuctionPreviewDTO> auctionDTOList = auctionService.findAllAvailable(pageable)
                .stream()
                .map(auctionPreviewDTOMapperService::map)
                .collect(Collectors.toList());
        //@formatter:on

        result.put("auction_previews", auctionDTOList);
        return result;
    }

    @GetMapping(value = "{id}", produces = "application/json")
    public Map<String, Object> auction(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();

        Auction auction = auctionService.findById(id);
        AuctionDTO auctionDTO = auctionDTOMapperService.map(auction);

        result.put("auction", auctionDTO);
        return result;
    }

}
