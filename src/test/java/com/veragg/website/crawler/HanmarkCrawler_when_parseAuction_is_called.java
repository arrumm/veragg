package com.veragg.website.crawler;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.veragg.website.crawler.model.HanmarkAuctionModel;

import static com.veragg.website.domain.PropertyType.ONE_FAMILY_HOUSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
class HanmarkCrawler_when_parseAuction_is_called {

    @Autowired
    private HanmarkCrawler sut;

    @Before
    public void setup() {
    }

    @Test
    void and_valid_pageData_is_passed_then_auction_returned() throws IOException {
        // Arrange
        InputStream houseInputStream = getClass().getClassLoader().getResourceAsStream("hanmark-house.html");

        // Act
        HanmarkAuctionModel result = sut.parseAuction("pagePath", houseInputStream);

        // Assert
        assertNotNull(result);
        assertEquals("12b K 3/19", result.getFileNumber());
        assertEquals("Wittlich", result.getCourtName());
        assertEquals(ONE_FAMILY_HOUSE.getName(), result.getPropertyTypeName());
        assertEquals("Bergweg 7", result.getStreetAddress());
        assertEquals("54538 Hontheim", result.getCityAddress());
        assertEquals("161.700,00 EUR", result.getAmount());
        assertEquals("16.06.2020 14:00 Uhr", result.getAppointmentDate());

        assertEquals(
                "der Sachverständigen über den Verkehrswert für das mit einem Wohnhaus mit Garage bebaute Grundstück in 54538 Hontheim, Bergweg 7\n" + "· Grundbuch Hontheim\n" + "· Blatt 2297\n" +
                        "· Gemarkung Hontheim\n" + "· Flur 31\n" + "· BV. Nr. 1, Flurstück 33/2, Gebäude- und Freifläche; Größe 50 m²; Verkehrswert: 4.700,00 €\n" +
                        "· BV. Nr. 2, Flurstück 27/3, Gebäude- und Freifläche; Größe 67 m²; Verkehrswert: 1.800,00 €\n" +
                        "· BV. Nr. 3, Flurstück 36/2, Erholungsfläche, Gebäude- und Freifläche; Größe 1.136 m²; Verkehrswert: 155.200,00 €\n" + "Wertermittlungsstichtag: 30.07.2019\n" +
                        "Gesamtverkehrswert: 161.700,00 €\n", result.getExpertDescription());

    }

}
