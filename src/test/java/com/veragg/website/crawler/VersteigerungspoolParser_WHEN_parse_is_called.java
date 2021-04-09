package com.veragg.website.crawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.veragg.website.crawler.model.VersteigerungspoolAuctionDTO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class VersteigerungspoolParser_WHEN_parse_is_called {

    private VersteigerungspoolParser sut;

    @Before
    public void setUp() throws Exception {
        sut = new VersteigerungspoolParser();
    }

    @Test
    public void GIVEN_valid_pageData_is_passed_THEN_auction_returned() throws IOException {

        // Arrange
        InputStream wohnungInputStream = getClass().getClassLoader().getResourceAsStream("versteigerungspool-flat.html");
        final Document htmlDocumentFlat = Jsoup.parse(new ByteArrayInputStream(IOUtils.toString(wohnungInputStream, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8)), "UTF-8",
                "https://versteigerungspool.de/immobilie/3-zimmerwohnung.305277");

        // Act
        VersteigerungspoolAuctionDTO result = sut.parse(htmlDocumentFlat);

        // Assert
        assertNotNull(result);
        assertEquals("9 K 7/19", result.getFileNumber());
        assertEquals("Amtsgericht Albstadt", result.getCourtName());
        assertEquals("Dreizimmerwohnung", result.getPropertyTypeName());
        assertEquals("68.000,00 €", result.getAmount());
        assertEquals("11.02.2021 um 08:30 Uhr", result.getAppointmentDate());
        assertEquals("gelten", result.getLimitDescription());
        assertEquals("https://versteigerungspool.de/immobilie/3-zimmerwohnung.305277", result.getSourceUrl());

        assertEquals("Johannes-Schmid-Straße 136 72461 Albstadt-Tailfingen", result.getStreetAddress());
        assertEquals("Johannes-Schmid-Straße 136 72461 Albstadt-Tailfingen", result.getCityAddress());

        assertEquals("Bei dem Versteigerungsobjekt handelt es sich um die 3-Zimmerwohnung Nr. 2 im EG rechts eines 4-geschossigen Mehrfamilienhauses\n" + "(Johannes-Schmid-Straße 136).\n" + "\n" +
                "Die Räumlichkeiten mit einer Gesamtwohnfläche von ca. 73,85 m² teilen sich auf in drei Zimmer, Küche, Bad mit WC, Abstellraum und Loggia. Die Grundrissgestaltung stellt sich " +
                "zweckmäßig dar.\n" +
                "Zur Wohnung gehört der Keller Nr. 2. Der bauliche Zustand des Sondereigentums ist befriedigend. Es besteht ein erheblicher Unterhaltungsstau und allgemeiner Renovierungsbedarf,\n" +
                "Modernisierungsbedarf besteht wegen überalterter Sanitärausstattung und Bodenbeläge.\n" + "\n" + "Zum Zeitpunkt der Wertermittlung war das Objekt leer stehend.\n" + "\n" +
                "Gebäudebeschreibung:\n" + "\n" +
                "Das Grundstück ist mit 5 Mehrfamilienhäuser bebaut, aufgeteilt in 5 Wohnteile mit 5 Hauseingängen a´ 8 Wohnungen. Die Gebäude sind ca. 1968/70 mit Unterkellerung und Flachdächern " +
                "in massiver\n" +
                "Bauweise errichtet worden. Der bauliche Zustand ist normal. Es besteht Unterhaltungsstau und allgemeiner Renovierungsbedarf. Die Anschriften lauten Johannes-Schmid-Straße 128, 130," +
                " 132, 134\n" + ",136.\n" + "\n" + "© immobilienpool.de Media GmbH &amp; Co. KG | 2020", result.getExpertDescription());

        assertEquals("Das Versteigerungsobjekt befindet sich in Albstadt, im Stadtteil Tailfingen.\n" + "\n" +
                "Das Grundstück liegt am Stadtrand an einer verkehrsberuhigt ausgebauten Wohnstraße mit mäßigem Verkehr, Gehwege sind beiderseitig vorhanden. Die Nachbarschaftsbebauung besteht " +
                "überwiegend\n" + "aus wohnbaulichen Nutzungen. Die Wohnlage wird als mittel eingestuft.\n" + "\n" +
                "Die Entfernung zum Stadtzentrum, den Geschäfte des täglichen Bedarfs sowie Schulen und Ärzten beträgt ca. 2 km. Öffentliche Verkehrsmittel (Bushaltestelle) können in unmittelbarer " +
                "Nähe\n" + "erreicht werden.\n" + "\n" + "© immobilienpool.de Media GmbH &amp; Co. KG | 2020", result.getPlotDescription());

        assertEquals(
                "Fenster: aus Kunststoff mit Isolierverglasung; Rollläden aus Kunststoff.\n" + "Bodenbeläge: PVC, Parkett.\n" + "Heizung: Zentralheizung.\n" + "Warmwasser: zentral über Heizung.\n" +
                        "Elektroinstallation: durchschnittliche Ausstattung; Türöffner, Klingelanlage, Telefonanschluss.\n" + "Sanitäre Installation: eingebaute Wanne, WC und Waschbecken.\n" +
                        "Besondere Bauteile: Loggia (überbaut).\n" + "\n" + "© immobilienpool.de Media GmbH &amp; Co. KG | 2020", result.getBuildingDescription());

        assertNull(result.getAdditionalInfoDescription());

    }

    @Test
    public void GIVEN_valid_pageData_AND_additional_info_filled_is_passed_THEN_auction_returned() throws IOException {

        // Arrange
        InputStream hausInputStream = getClass().getClassLoader().getResourceAsStream("versteigerungspool-zwei-einfamilienhauuser.html");
        final Document htmlDocumentFlat = Jsoup.parse(new ByteArrayInputStream(IOUtils.toString(hausInputStream, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8)), "UTF-8",
                "https://versteigerungspool.de/immobilie/zwei-einfamilienhaeuser.325227");

        // Act
        VersteigerungspoolAuctionDTO result = sut.parse(htmlDocumentFlat);

        //Assert
        assertEquals("Diese Zwangsversteigerung weist folgende Besonderheit auf: Das oben beschriebene Objekt wird gemeinsam mit einem oder mehreren anderen Objekten versteigert. Dies ist " +
                "ersichtlich aus den Versteigerungsdaten / Terminsbestimmung (siehe oben). Dabei besteht die Möglichkeit, die Versteigerungsobjekte einzeln oder in einem Gesamtgebot zu ersteigern. " +
                "Das wird sich jedoch erst beim Versteigerungstermin ergeben und vom Rechtspfleger bekannt gegeben. Nähere Informationen hierzu erfragen Sie bitte bei Gericht, dem Gläubiger oder " +
                "dem vom Gläubiger beauftragten Vertreter/Makler.\n" + "\n" +
                "Wenn hier kein bzw. nur eines der vorhandenen Gutachten zum Download zur Verfügung gestellt wurde, können alle Gutachten unter dem jeweiligen Aktenzeichen oder beim Amtsgericht " +
                "eingesehen werden.", result.getAdditionalInfoDescription());

    }

    @Test
    public void GIVEN_valid_pageData_AND_links_are_filled_is_passed_THEN_auction_returned() throws IOException {

        // Arrange
        InputStream hausInputStream = getClass().getClassLoader().getResourceAsStream("versteigerungspool-zwei-einfamilienhauuser.html");
        final Document htmlDocumentFlat = Jsoup.parse(new ByteArrayInputStream(IOUtils.toString(hausInputStream, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8)), "UTF-8",
                "https://versteigerungspool.de/immobilie/zwei-einfamilienhaeuser.325227");

        // Act
        VersteigerungspoolAuctionDTO result = sut.parse(htmlDocumentFlat);

        //Assert
        assertNotNull(result);

        assertFalse(result.getExpertiseLinks().isEmpty());
        //        assertEquals(1, result.getExpertiseLinks().size());
        //        assertEquals("https://www.hanmark.de/wertgutachten-29362.pdf", result.getExpertiseLinks().get(0));
        //
        //        assertFalse(result.getOtherDocumentLinks().isEmpty());
        //        assertEquals(1, result.getOtherDocumentLinks().size());
        //        assertTrue(result.getOtherDocumentLinks().contains("https://www.hanmark.de/termin-29362.pdf"));
        //
        //        assertFalse(result.getImageLinks().isEmpty());
        //        assertEquals(6, result.getImageLinks().size());
        //        assertEquals("https://www.hanmark.de/titelbild-29362.jpg", result.getImageLinks().get(0));
        //        assertEquals("https://www.hanmark.de/abbildung-198080.jpg", result.getImageLinks().get(1));
        //        assertEquals("https://www.hanmark.de/abbildung-198081.jpg", result.getImageLinks().get(2));
        //        assertEquals("https://www.hanmark.de/abbildung-198082.jpg", result.getImageLinks().get(3));
        //        assertEquals("https://www.hanmark.de/abbildung-198083.jpg", result.getImageLinks().get(4));
        //        assertEquals("https://www.hanmark.de/abbildung-198084.jpg", result.getImageLinks().get(5));

    }

}
