package com.veragg.website.crawler;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.veragg.website.crawler.mapping.AuctionMapperService;
import com.veragg.website.crawler.model.ZvgLotAmountDTO;
import com.veragg.website.crawler.model.ZvgPortalAuctionDTO;
import com.veragg.website.services.AuctionService;
import com.veragg.website.services.AuctionSourceService;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ZvgPortalCrawler_when_fetchAuction_is_called {

    private ZvgPortalCrawler sut;

    @Mock
    private AuctionMapperService<ZvgPortalAuctionDTO> mapperService;

    @Mock
    AuctionService auctionService;

    @Mock
    AuctionSourceService auctionSourceService;

    @Mock
    PageData pageData;

    @Before
    public void setup() {
        sut = new ZvgPortalCrawler(mapperService, auctionService, auctionSourceService);
    }

    @Test
    public void given_single_lot_page_then_auction_dto_with_one_lot_returned() throws Exception {

        // Arrange
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("zvg-portal-single-lot.html");
        when(pageData.getUrl()).thenReturn("https://www.zvg-portal.de/index.php?button=showZvg&zvg_id=48751&land_abk=by");
        when(pageData.getContent()).thenReturn(IOUtils.toString(inputStream, StandardCharsets.UTF_8));

        // Act
        ZvgPortalAuctionDTO result = sut.fetchAuction(pageData);

        // Assert
        assertNotNull(result);
        assertEquals("0001 K 0021/ 2025", result.getFileNumber());
        assertEquals("Landshut", result.getCourtName());
        assertEquals("Einfamilienhaus, Garage", result.getPropertyTypeName());
        assertEquals("Closenstraße 4", result.getStreetAddress());
        assertEquals("94405 Landau a.d. Isar, Zeholfing", result.getCityAddress());
        assertEquals("Dienstag, 25. August 2026, 09:00 Uhr", result.getAppointmentDate());
        assertEquals("https://www.zvg-portal.de/index.php?button=showZvg&zvg_id=48751&land_abk=by", result.getSourceUrl());
        assertThat(result.getExpertDescription(), containsString("Einfamilienhaus mit Garage"));

        assertEquals(1, result.getLots().size());
        ZvgLotAmountDTO lot = result.getLots().get(0);
        assertEquals(null, lot.getLotNumber());
        assertEquals("181000,00", lot.getAmount());

        assertEquals(2, result.getExpertiseLinks().size());
        assertTrue(result.getExpertiseLinks().contains("https://www.zvg-portal.de/index.php?button=showAnhang&land_abk=by&file_id=125379&zvg_id=48751"));
        assertTrue(result.getExpertiseLinks().contains("https://www.zvg-portal.de/index.php?button=showAnhang&land_abk=by&file_id=125381&zvg_id=48751"));

        assertEquals(1, result.getOtherDocumentLinks().size());
        assertTrue(result.getOtherDocumentLinks().contains("https://www.zvg-portal.de/index.php?button=showAnhang&land_abk=by&file_id=125383&zvg_id=48751"));

        assertTrue(result.getImageLinks().isEmpty());

    }

    @Test
    public void given_multi_lot_page_then_auction_dto_with_two_lots_returned() throws Exception {

        // Arrange
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("zvg-portal-multi-lot.html");
        when(pageData.getUrl()).thenReturn("https://www.zvg-portal.de/index.php?button=showZvg&zvg_id=48463&land_abk=by");
        when(pageData.getContent()).thenReturn(IOUtils.toString(inputStream, StandardCharsets.UTF_8));

        // Act
        ZvgPortalAuctionDTO result = sut.fetchAuction(pageData);

        // Assert
        assertNotNull(result);
        assertEquals("0061 K 0006/ 2025", result.getFileNumber());
        assertEquals("Bayreuth", result.getCourtName());
        assertEquals("Einfamilienhaus", result.getPropertyTypeName());
        assertEquals("91344 Waischenfeld", result.getCityAddress());
        assertEquals("Donnerstag, 27. August 2026, 10:00 Uhr", result.getAppointmentDate());

        assertEquals(2, result.getLots().size());
        ZvgLotAmountDTO firstLot = result.getLots().get(0);
        ZvgLotAmountDTO secondLot = result.getLots().get(1);
        assertEquals("1", firstLot.getLotNumber());
        assertEquals("1,00 €", firstLot.getAmount());
        assertEquals("2", secondLot.getLotNumber());
        assertEquals("740,00 €", secondLot.getAmount());

    }

    @Test
    public void given_page_with_foto_attachment_then_image_link_categorized() throws Exception {

        // Arrange
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("zvg-portal-with-foto.html");
        when(pageData.getUrl()).thenReturn("https://www.zvg-portal.de/index.php?button=showZvg&zvg_id=48838&land_abk=by");
        when(pageData.getContent()).thenReturn(IOUtils.toString(inputStream, StandardCharsets.UTF_8));

        // Act
        ZvgPortalAuctionDTO result = sut.fetchAuction(pageData);

        // Assert
        assertNotNull(result);
        assertEquals("0002 K 0059/ 2025", result.getFileNumber());
        assertEquals("Fürth", result.getCourtName());
        assertEquals("Eigentumswohnung (3 bis 4 Zimmer), sonstiges Teileigentum (z.B. Keller, Hobbyraum)", result.getPropertyTypeName());
        assertEquals("Falkenstraße 10", result.getStreetAddress());
        assertEquals("91056 Erlangen", result.getCityAddress());

        assertEquals(1, result.getLots().size());
        assertEquals("170.000,-- Euro", result.getLots().get(0).getAmount());

        assertEquals(1, result.getImageLinks().size());
        assertTrue(result.getImageLinks().contains("https://www.zvg-portal.de/index.php?button=showAnhang&land_abk=by&file_id=125689&zvg_id=48838"));

        assertEquals(1, result.getExpertiseLinks().size());
        assertEquals(1, result.getOtherDocumentLinks().size());

    }

}
