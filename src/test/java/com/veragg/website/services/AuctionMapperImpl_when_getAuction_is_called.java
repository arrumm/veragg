package com.veragg.website.services;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.veragg.website.domain.Address;
import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.domain.BuyLimit;
import com.veragg.website.domain.Court;

import static com.veragg.website.domain.PropertyType.COMMERCIAL_PROPERTY;
import static com.veragg.website.domain.PropertyType.ONE_FAMILY_HOUSE;
import static org.hamcrest.CoreMatchers.hasItems;
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
        draft.setImageLinks(Arrays.asList("image1Url", "image3Url"));
        draft.setExpertiseLinks(Arrays.asList("expertiseUrl", "anotherExpUrl"));
        draft.setOtherDocumentLinks(new HashSet<>(Arrays.asList("otherDocUrl1", "otherDocUrl2", "otherDocUrl3")));

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
        assertEquals(2, resultAuction.getImageLinks().size());
        assertThat(resultAuction.getImageLinks(), hasItems("image1Url", "image3Url"));
        assertEquals(2, resultAuction.getExpertiseLinks().size());
        assertThat(resultAuction.getExpertiseLinks(), hasItems("expertiseUrl", "anotherExpUrl"));
        assertEquals(3, resultAuction.getOtherDocumentLinks().size());
        assertThat(resultAuction.getOtherDocumentLinks(), hasItems("otherDocUrl1", "otherDocUrl2", "otherDocUrl3"));

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
