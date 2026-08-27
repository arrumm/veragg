package com.veragg.website.crawler.mapping;

import java.text.ParseException;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.veragg.website.crawler.model.ZvgLotAmountDTO;
import com.veragg.website.crawler.model.ZvgPortalAuctionDTO;
import com.veragg.website.domain.Auction;
import com.veragg.website.domain.BuyLimit;
import com.veragg.website.domain.Court;
import com.veragg.website.domain.PropertyType;
import com.veragg.website.domain.State;
import com.veragg.website.services.CourtService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class ZvgPortalAuctionMapperServiceImpl_when_map_is_called {

    private ZvgPortalAuctionMapperServiceImpl sut;

    @Mock
    private CourtService courtService;

    @Mock
    private NormalizationService normalizationService;

    @Mock
    private Court court;

    @Mock
    private State state;

    @Before
    public void setUp() {
        initMocks(this);
        sut = new ZvgPortalAuctionMapperServiceImpl(courtService, normalizationService);
        when(normalizationService.normalizeCity(anyString())).thenCallRealMethod();
        when(normalizationService.normalizeAmount(anyString())).thenCallRealMethod();
        when(court.getName()).thenReturn("Landshut");
        when(court.getState()).thenReturn(state);
        when(state.getId()).thenReturn("BY");
        when(courtService.findBy(eq("Landshut"), eq("94405"))).thenReturn(court);
    }

    @Test
    public void given_single_lot_dto_then_one_auction_returned() throws ParseException {

        // Arrange
        //@formatter:off
        ZvgPortalAuctionDTO auctionDTO = ZvgPortalAuctionDTO.builder()
                .fileNumber("0001 K 0021/ 2025")
                .courtName("Landshut")
                .propertyTypeName("Einfamilienhaus, Garage")
                .streetAddress("Closenstraße 4")
                .cityAddress("94405 Landau a.d. Isar, Zeholfing")
                .expertDescription("Einfamilienhaus mit Garage, zweigeschossig, unterkellert")
                .appointmentDate("Dienstag, 25. August 2026, 09:00 Uhr")
                .sourceUrl("https://www.zvg-portal.de/index.php?button=showZvg&zvg_id=48751&land_abk=by")
                .lots(Collections.singletonList(new ZvgLotAmountDTO(null, "181000,00")))
                .build();
        //@formatter:on
        auctionDTO.setExpertiseLinks(Arrays.asList("url/gutachten1.pdf", "url/gutachten2.pdf"));

        // Act
        final Auction result = sut.map(auctionDTO).get(0);

        // Assert
        assertEquals("Landshut", result.getCourt().getName());
        assertEquals("BY", result.getCourt().getState().getId());
        assertEquals("0001 K 0021/ 2025", result.getFileNumber());
        assertNull(result.getLotNumber());
        assertTrue(result.getPropertyTypes().contains(PropertyType.ONE_FAMILY_HOUSE));
        assertEquals("Landau a.d. Isar, Zeholfing", result.getAddress().getCity());
        assertEquals("94405", result.getAddress().getZipCode());
        assertEquals("4", result.getAddress().getNumber());
        assertEquals("Closenstraße", result.getAddress().getStreet());
        assertEquals(Integer.valueOf(181000), result.getAmount());
        assertEquals(2026, result.getAppointment().getYear());
        assertEquals(Month.AUGUST.getValue(), result.getAppointment().getMonthValue());
        assertEquals(25, result.getAppointment().getDayOfMonth());
        assertEquals(9, result.getAppointment().getHour());
        assertEquals(BuyLimit.NA, result.getBuyLimit());
        assertEquals(2, result.getDocuments().size());

    }

    @Test
    public void given_multi_lot_dto_then_one_auction_per_lot_returned() throws ParseException {

        // Arrange
        //@formatter:off
        ZvgPortalAuctionDTO auctionDTO = ZvgPortalAuctionDTO.builder()
                .fileNumber("0061 K 0006/ 2025")
                .courtName("Landshut")
                .propertyTypeName("Einfamilienhaus")
                .streetAddress("Siegritzberg 16")
                .cityAddress("94405 Waischenfeld")
                .expertDescription("Lfd. Nr. 1 ...")
                .appointmentDate("Donnerstag, 27. August 2026, 10:00 Uhr")
                .sourceUrl("https://www.zvg-portal.de/index.php?button=showZvg&zvg_id=48463&land_abk=by")
                .lots(Arrays.asList(new ZvgLotAmountDTO("1", "1,00 €"), new ZvgLotAmountDTO("2", "740,00 €")))
                .build();
        //@formatter:on

        // Act
        final Auction[] results = sut.map(auctionDTO).toArray(new Auction[0]);

        // Assert
        assertEquals(2, results.length);

        assertEquals("1", results[0].getLotNumber());
        assertEquals(Integer.valueOf(1), results[0].getAmount());
        assertEquals("0061 K 0006/ 2025", results[0].getFileNumber());

        assertEquals("2", results[1].getLotNumber());
        assertEquals(Integer.valueOf(740), results[1].getAmount());
        assertEquals("0061 K 0006/ 2025", results[1].getFileNumber());

    }

}
