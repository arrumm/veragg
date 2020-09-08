package com.veragg.website.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.Document;
import com.veragg.website.domain.DocumentType;
import com.veragg.website.domain.PropertyType;
import com.veragg.website.domain.PropertyTypeComparator;
import com.veragg.website.services.FileUrlHelper;

@Service
public class AuctionPreviewDTOMapperService {

    public AuctionPreviewDTO map(Auction auction) {
        List<PropertyType> propertyTypes = new ArrayList<>(auction.getPropertyTypes());
        propertyTypes.sort(new PropertyTypeComparator());

        Document firstImage = getFirstImage(auction.getDocuments());
        String firstImageFileExtension = FileUrlHelper.getExtension(firstImage.getOriginalFileName());
        String timeImageFileName = firstImage.getStoreName().concat(".").concat(firstImageFileExtension);

        //@formatter:off
        return AuctionPreviewDTO.builder()
                .id(auction.getId())
                .fileNumber(auction.getFileNumber())
                .propertyType(propertyTypes.get(0).getName())
                .city(auction.getAddress().getCity())
                .zipCode(auction.getAddress().getZipCode())
                .appointment(DateConverter.dateToTimestamp(auction.getAppointment()))
                .amount(auction.getAmount())
                .street(auction.getAddress().getStreet())
                .number(auction.getAddress().getNumber())
                //todo: combine a filename by id with some predefined static storage
                .tileImage(timeImageFileName)
                .build();
        //@formatter:on

    }

    private Document getFirstImage(List<Document> documents) {
        return documents.stream().filter(document -> document.getDocumentType().equals(DocumentType.IMAGE)).sorted().findFirst().orElse(new Document(DocumentType.IMAGE, "fakeName"));
    }

}
