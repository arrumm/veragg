package com.veragg.website.crawler.mapping;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.veragg.website.crawler.model.HanmarkAuctionModel;
import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.domain.BuyLimit;
import com.veragg.website.domain.Court;
import com.veragg.website.domain.PropertyType;
import com.veragg.website.domain.State;
import com.veragg.website.services.CourtService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class HanmarkAuctionMapperServiceImpl_when_map_is_called {

    private HanmarkAuctionMapperServiceImpl sut;

    @Mock
    private CourtService courtService;

    @Mock
    private Court court;

    @Mock
    private State state;

    @Before
    public void setUp() {
        initMocks(this);
        sut = new HanmarkAuctionMapperServiceImpl(courtService);
        when(court.getName()).thenReturn("Wittlich");
        when(court.getState()).thenReturn(state);
        when(state.getId()).thenReturn("RP");
        when(courtService.findBy(eq("Wittlich"), eq("54538"))).thenReturn(court);
    }

    @Test
    public void and_valid_model_passed_then_draft_should_return() {
        // Arrange
        // @formatter:off
        HanmarkAuctionModel auctionModel = HanmarkAuctionModel.builder()
                .fileNumber("12b K 3/19")
                .amount("161.700,00 EUR")
                .appointmentDate("16.06.2020 14:00 Uhr")
                .courtName("Wittlich")
                .propertyTypeName("Einfamilienhaus")
                .cityAddress("54538 Hontheim")
                .streetAddress("Bergweg 7")
                .build();
        // @formatter:on

        auctionModel.setLimitDescription("keine Angabe");

        Date dateJune16 = new GregorianCalendar(2020, Calendar.JUNE, 16, 14, 0).getTime();

        // Act
        final AuctionDraft result = sut.convert(auctionModel);

        // Assert
        assertEquals("Wittlich", result.getCourt().getName());
        assertEquals("RP", result.getCourt().getState().getId());
        assertEquals("12b K 3/19", result.getFileNumber());
        assertEquals(PropertyType.ONE_FAMILY_HOUSE, result.getPropertyType());
        assertEquals("Hontheim", result.getAddress().getCity());
        assertEquals("54538", result.getAddress().getZipCode());
        assertEquals("7", result.getAddress().getNumber());
        assertEquals("Bergweg", result.getAddress().getStreet());
        assertEquals(161700, result.getAmount());
        assertEquals(dateJune16.getTime(), result.getAppointment().getTime());
        assertEquals(BuyLimit.L100, result.getBuyLimit());

    }
}
