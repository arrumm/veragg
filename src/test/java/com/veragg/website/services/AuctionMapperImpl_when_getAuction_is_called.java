package com.veragg.website.services;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.veragg.website.domain.Address;
import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionSourceType;
import com.veragg.website.domain.BuyLimit;
import com.veragg.website.domain.Court;
import com.veragg.website.domain.Document;
import com.veragg.website.domain.DocumentType;

import static com.veragg.website.domain.PropertyType.COMMERCIAL_PROPERTY;
import static com.veragg.website.domain.PropertyType.ONE_FAMILY_HOUSE;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

public class AuctionMapperImpl_when_getAuction_is_called {

    AuctionMapperImpl sut;

    @Before
    public void setUp() {
        sut = new AuctionMapperImpl();
    }

    @Test
    public void given_null_draft_then_NPE_expected() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.getAuction(null));

    }

    @Test
    public void given_draft_with_data_then_return_saved_auction_with_properties_set() {

        //Arrange

        AuctionDraft draft = new AuctionDraft();
        Court court = mock(Court.class);
        draft.setCourt(court);
        draft.setFileNumber("AZ 12/43");
        Address address = mock(Address.class);
        draft.setAddress(address);
        draft.setAmount(1);
        draft.setBuyLimit(BuyLimit.NA);
        draft.setPropertyTypes(new HashSet<>(Arrays.asList(ONE_FAMILY_HOUSE, COMMERCIAL_PROPERTY)));
        LocalDateTime appointment = LocalDateTime.of(2020, Month.AUGUST, 8, 12, 30);
        draft.setAppointment(appointment);
        draft.setOutdoorDescription("outdoor description");
        draft.setPropertyBuildingDescription("building description");
        draft.setPropertyPlotDescription("plot description");
        draft.setExpertiseDescription("expertise description");

        Document docImage1 = new Document("url/image1Url.jpg", DocumentType.IMAGE);
        Document docImage2 = new Document("url/image2Url.jpg", DocumentType.IMAGE);
        Document docExpertise1 = new Document("url/expertiseUrl.pdf", DocumentType.EXPERTISE);
        Document docExpertise2 = new Document("url/anotherExpUrl.pfd", DocumentType.EXPERTISE);
        Document docOther1 = new Document("url/otherDocUrl1.pdf", DocumentType.OTHER);
        Document docOther2 = new Document("url/otherDocUrl2.jpg", DocumentType.OTHER);
        Document docOther3 = new Document("url/otherDocUrl3.png", DocumentType.OTHER);
        draft.setDocuments(Arrays.asList(docImage2, docImage1, docExpertise2, docExpertise1, docOther1, docOther2, docOther3));

        AuctionSource source = new AuctionSource();
        source.setAuctionSourceType(AuctionSourceType.WEBSITE);
        source.setName("sourceName");
        draft.setSource(source);

        //Act
        Auction resultAuction = sut.getAuction(draft);

        //Assert
        assertNotNull(resultAuction);
        assertEquals(court, resultAuction.getCourt());
        assertEquals("AZ 12/43", resultAuction.getFileNumber());
        assertEquals(address, resultAuction.getAddress());
        assertEquals(Integer.valueOf(1), resultAuction.getAmount());
        assertEquals(BuyLimit.NA, resultAuction.getBuyLimit());
        assertThat(resultAuction.getPropertyTypes(), hasItems(COMMERCIAL_PROPERTY, ONE_FAMILY_HOUSE));
        assertEquals(appointment, resultAuction.getAppointment());
        assertEquals("outdoor description", resultAuction.getOutdoorDescription());
        assertEquals("plot description", resultAuction.getPropertyPlotDescription());
        assertEquals("expertise description", resultAuction.getExpertiseDescription());
        assertEquals("building description", resultAuction.getPropertyBuildingDescription());

        assertEquals(7, resultAuction.getDocuments().size());
        //@formatter:off
        assertThat(resultAuction.getDocuments(), hasItem(allOf(
                        Matchers.<Document>hasProperty("url", is("url/image1Url.jpg")),
                        Matchers.<Document>hasProperty("documentType", is(DocumentType.IMAGE)))));
        assertThat(resultAuction.getDocuments(), hasItem(allOf(
                        Matchers.<Document>hasProperty("url", is("url/otherDocUrl1.pdf")),
                        Matchers.<Document>hasProperty("documentType", is(DocumentType.OTHER)))));
        assertThat(resultAuction.getDocuments(), hasItems(docExpertise1, docExpertise2, docImage1, docImage2, docOther3, docOther2, docOther1));
        //@formatter:on

        assertEquals(resultAuction.getSource(), source);

    }

    @Test
    public void given_draft_with_nonNull_property_try_to_set_null_then_NPE_expected() {

        //Arrange

        AuctionDraft draft = new AuctionDraft();

        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> draft.setCourt(null));

    }

}
