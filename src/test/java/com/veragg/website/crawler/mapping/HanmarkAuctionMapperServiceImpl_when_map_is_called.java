package com.veragg.website.crawler.mapping;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.veragg.website.crawler.model.HanmarkAuctionModel;
import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.domain.Limit;
import com.veragg.website.domain.PropertyType;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
class HanmarkAuctionMapperServiceImpl_when_map_is_called {

    private HanmarkAuctionMapperServiceImpl sut;

    @Before
    void setUp() {
        sut = new HanmarkAuctionMapperServiceImpl();
    }

    @Test
    public void map() {
        // Arrange
        // @formatter:off
        final HanmarkAuctionModel auctionModel = HanmarkAuctionModel.builder()
                .amount("161.700,00 EUR")
                .appointmentDate("16.06.2020 14:00 Uhr")
                .courtName("Wittlich")
                .fileNumber("12b K 3/19")
                .propertyTypeName("Einfamilienhaus")
                .cityAddress("54538 Hontheim")
                .streetAddress("Bergweg 7")
                .build();
        // @formatter:on

        auctionModel.setLimitDescription("keine Angabe");

        Date dateJune16 = new GregorianCalendar(2020, Calendar.JUNE, 16, 14, 0).getTime();

        // Act
        final AuctionDraft result = sut.map(auctionModel);

        // Assert
        assertEquals("Wittlich", result.getCourt().getName());
        //TODO add when State by zip code service will be implemented
//        assertEquals(State.RP, result.getCourt().getState());
        assertEquals("12b K 3/19", result.getFileNumber());
        assertEquals(PropertyType.ONE_FAMILY_HOUSE, result.getPropertyType());
        assertEquals("Hontheim", result.getAddress().getCity());
        assertEquals("54538", result.getAddress().getZipCode());
        assertEquals("7", result.getAddress().getNumber());
        assertEquals("Bergweg", result.getAddress().getStreet());
        assertEquals(161700, result.getAmount());
        assertEquals(dateJune16.getTime(), result.getAppointment().getTime());
        assertEquals(Limit.L100, result.getLimit());

    }
}
