package com.veragg.website.crawler;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.veragg.website.crawler.model.HanmarkAuctionModel;

import static com.veragg.website.domain.PropertyType.ONE_FAMILY_HOUSE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
class HanmarkCrawler_when_parseAuction_is_called {

    @Autowired
    private HanmarkCrawler sut;

    @Before
    public void setup() {
    }

    @Test
    public void and_valid_pageData_is_passed_then_auction_returned() throws IOException {
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

        assertEquals("Ort und Einwohnerzahl: Hontheim ist eine Ortsgemeinde in der südlichen Vulkaneifel. Sie gehört zur Verbandsgemeinde Traben- Trarbach. Der Ort liegt am Randes des Kondelwalds " +
                        "und ist umgeben von den Bächen Alf sowie Üss. Hontheim hat ca. 800 Einwohner und verfügt über eine Kindertagesstätte. Eine Grundschule ist in Hontheim nicht vorhanden.\n" +
                        "Überörtliche Anbindung/Entfernungen: Durch Hontheim verläuft die Bundesstraße 421, die von der belgischen Grenze bis in den Hunsrück führt. Zudem hat der Ort eine gute " +
                        "Anbindung an" +
                        " die Autobahn A1 Kelberg - Trier - Saarbrücken. Die Entfernung bis zur Anschlussstelle Hasborn (123) beträgt ca. 13 km. Die Entfernungen zu den nächstgelegenen Städten " +
                        "betragen: ca" +
                        ". 5 km bis nach Bad Bertrich, ca. 18 km bis nach Wittlich, ca. 19 km bis nach Zell, ca. 24 km bis nach Traben- Trarbach, ca. 28 km bis nach Daun, ca. 29 km bis nach Cochem," +
                        " ca. 37 " + "km bis zum Flughafen Frankfurt- Hahn, ca. 42 km bis nach Schweich.\nInnerörtliche Lage: Das zu bewertenden Grundstück liegt im nördlichen Zentrum von Hontheim" + ".\n" +
                        "Art der Bebauung und Nutzungen: gemischte Bauweise; Die Bebauung in der näheren Umgebung besteht überwiegend aus Wohnhäusern in zweigeschossiger Bauweise.\n" +
                        "Straßenart: Ortsstraße\nAnschlüsse an Versorgungsleitungen: Flurstück 36/2: Die Wasser-, Abwasser- und Elektrizitätsversorgung sind an das öffentliche Netz angeschlossen.\n",
                result.getPlotDescription());

        assertEquals("Hinweis: Der Sachverständigen wurde beim Ortstermin nur eine eingeschränkte Besichtigung des Gebäudes gestattet. Es hat eine Führung in Räume des Erd- und Dachgeschosses " +
                        "stattgefunden. Hierbei durften keine Aufzeichnungen angefertigt werden.\n" + "Art des Gebäudes: Wohnhaus, teil unterkellert\n" + "Baujahr: unbekannt, vermutlich vor 1900\n" +
                        "Umbau/Modernisierung: ca. 1999: Erweiterung des Gebäudes durch einen seitlichen Anbau, Anbau eines Treppenhauses an der südlichen Gebäudeseite, Aufstockung des " +
                        "Dachgeschosses, Erneuerung des Dachstuhls mit Dacheindeckung.\n" + "Energieeffizienz: Ein Energieausweis wurde der Sachverständigen nicht vorgelegt.\n" +
                        "Außenansicht: Putz mit Anstrich\n" + "Raumaufteilung\n" + "· Kellergeschoss: Gewölbekeller\n" +
                        "· Erdgeschoss: Treppenhaus, Diele, 2 Büros, Abstellraum, Heizungsraum, Öllager, Küche, Treppenhaus mit Flur, Dusche/WC\n" +
                        "· Obergeschoss: 2 Treppenhäuser, 2 Flure, Wohnzimmer, Küche, Abstellraum, WC, 2 Schlafzimmer, Bad, Abstellraum\n" + "· Dachgeschoss: Treppenhaus, Wohn-/Esszimmer, Küche, " + "Bad\n" +
                        "Gebäudekonstruktion\n" + "Konstruktionsart: Massivbauweise\n" + "Fundamente: unbekannt\n" + "Wände: Außen- und Innenwände: massives Mauerwerk\n" + "Geschossdecken: " +
                        "unbekannt\n" +
                        "Hauseingangs-/Nebeneingangsbereich: nicht überdachter Hauseingang mit einer Eingangsstufe, Oberbelag aus Naturstein; Nebeneingang: nicht überdachter Nebeneingang mit drei " +
                        "Eingangsstufen, Oberbelag aus Fliesen\n" +
                        "Treppen: KG: Treppe aus Beton, Oberbelag aus Werkstein; EG: Spindeltreppe und Geländer in Stahlkonstruktion mit 14 Stufen aus Naturstein; OG: Spindeltreppe und Geländer in " +
                        "Stahlkonstruktion mit 15 Stufen aus Naturstein\n" + "Dach: Dachkonstruktion: Holzkonstruktion; Dachform: Satteldach; Dacheindeckung: Betondachsteine; Kamin: gemauert\n" +
                        "Besondere Bauteile: Hauseingangsstufen; Dachgauben\n" + "Ausstattung\n" + "Bodenbeläge: soweit ersichtlich überwiegend aus Fliesen\n" +
                        "Wandbeläge: soweit ersichtlich überwiegend Strukturputz mit Anstrich, in den Bädern Fliesen\n" + "Deckenbeläge: soweit ersichtlich Putz mit Anstrich\n" +
                        "Fenster: PVC- Fenster mit Isolierverglasung\n" +
                        "Türen: Haustür: Hauseingangstür aus PVC mit Glasausschnitten in Isolierverglasung; Nebeneingangstür: Tür aus PVC; Zimmertüren: soweit ersichtlich überwiegend Holztüren mit " +
                        "Stahl- und Holzzargen, teilweise mit Glasausschnitt, Glastür im Wohnzimmer im DG\n" + "Technische Ausstattung\n" + "Elektroinstallation: mittlere Ausführung mit " +
                        "Sprechanlage\n" +
                        "Heizung: Ölzentralheizung mit Radiatoren im Erdgeschoss, Fußbodenheizung im Ober- und Dachgeschoss; Öllagerung: PVC- Tank mit 3.000 l und 5.000 l Inhalt\n" +
                        "Warmwasserversorgung: zentral über die Heizungsanlage gekoppelt mit einer Solaranlage\n" +
                        "Sanitärinstallation: Keine Angabe möglich, da nur eine eingeschränkte Besichtigung möglich war.\n" +
                        "Brandschutztechnische Anlage: Seit dem 12. Juli 2012 müssen alle Wohnungen in Rheinland-Pfalz mit Rauchmeldern ausgestattet sein. Entsprechend den Vorgaben der " +
                        "Rauchmelderpflicht Rheinland-Pfalz, welche im § 44 der Landesbauordnung (LBauO) Rheinland- Pfalz geregelt ist, müssen alle Schlafzimmer, alle Kinderzimmer und alle Flure, " +
                        "die als Fluchtweg dienen, mit jeweils einem Rauchmelder ausgestattet sein. Ob in allen Räumen die erforderlichen Rauchmelder installiert sind konnte nicht überprüft werden," +
                        " da nur eine eingeschränkte Besichtigung möglich war.\n" + "Zustand\n" + "Belichtung und Besonnung: gut\n" +
                        "Grundrissgestaltung: Keine Angabe möglich, da nur eine eingeschränkte Besichtigung möglich war.\n" +
                        "Bauschäden und Baumängel: Da die Besichtigung des Anwesens nur eingeschränkt möglich war, können über Bauschäden und Baumängel nur bedingt Angaben gemacht werden.\n" +
                        "· Teilweise laienhafter Innenausbau, wie die Verlegung der Fliesen.\n" + "· Eine Scheibe der Haustür ist defekt.\n" +
                        "· Soweit ersichtlich ist der Sockelputz stellenweise gerissen.\n" +
                        "Sonstige Besonderheiten: Da eine Innenbesichtigung nur eingeschränkt möglich war, wird im Gutachten ein Sicherheitsabschlag von 5% angesetzt.\n" +
                        "Allgemeinbeurteilung: Bei dem zu bewertenden Objekt handelt es sich um ein Wohnhaus, welches mit einem Gewölbekeller teilunterkellert ist. Das Wohnhaus wurde ca. 1999 durch" +
                        " einen seitlichen Anbau sowie mit einem neuen Treppenhaus erweitert. Ebenso wurde das Dachgeschoss aufgestockt und der Dachstuhl erneuert. Aufgrund der Gebäudekonzeption " +
                        "entspricht das Gebäude den Anforderungen eines Einfamilienhauses.\n" + "Doppelgarage\n" + "Art des Gebäudes: offene Doppelgarage\n" + "Baujahr: unbekannt\n" +
                        "Außenansicht: Putz mit Anstrich\n" + "Nutzungseinheiten, Raumaufteilung: Erdgeschoss: 2 PKW- Stellplätze\n" + "Gebäudekonstruktion\n" + "Konstruktionsart: Massivbauweise\n" +
                        "Fundamente: Beton\n" + "Wände: Außenwände: massives Mauerwerk\n" + "Dach: Dachkonstruktion: Holz; Dachform: Flachdach; Dacheindeckung: Abdichtungsbahnen\n" +
                        "Bodenbelag: PKW- Stellplätze: Betonsteinpflaster\n" + "Wandbeläge: PKW- Stellplätze: Putz mit Anstrich, Bruchsteinmauerwerk\n" + "Deckenbeläge: keine\n" +
                        "Tore/Fenster: keine vorhanden\n" + "Elektroinstallation: Elektroinstallation den Anforderungen entsprechend mit ausreichender Anzahl von Schalt-, Steckgeräten und " +
                        "Brennstellen.\n" + "Zustand\n" +
                        "Besonderheiten: Die Tore und die vorderseitige Außenwand fehlen. Die rückwärtige Wand der Garage fehlt. Die Garage wird durch die Nachbarwand begrenzt.\n" +
                        "Allgemeinbeurteilung: Bei dem zu bewertenden Objekt handelt es sich um eine Doppelgarage mit 2 PKW- Stellplätzen. Die Funktionalität der Stellplätze ist gegeben.\n" +
                        "Garage\n" +
                        "Art des Gebäudes: offene an die Doppelgarage angebaute Garage\n" + "Baujahr: unbekannt\n" + "Außenansicht: Putz mit Anstrich\n" +
                        "Nutzungseinheiten, Raumaufteilung: Erdgeschoss: PKW- Stellplatz\n" + "Gebäudekonstruktion\n" + "Konstruktionsart: Massivbauweise\n" + "Fundamente: Beton\n" +
                        "Wände: Außenwände: massives Mauerwerk\n" + "Dach: Dachkonstruktion: Holz; Dachform: Flachdach; Dacheindeckung: Abdichtungsbahnen\n" +
                        "Bodenbelag: PKW- Stellplätze: Betonsteinpflaster\n" + "Wandbeläge: PKW- Stellplätze: Putz mit Anstrich\n" + "Deckenbeläge: keine\n" + "Tore/Fenster: keine vorhanden\n" +
                        "Elektroinstallation: Elektroinstallation den Anforderungen entsprechend mit ausreichender Anzahl von Schalt-, Steckgeräten und Brennstellen.\n" + "Zustand\n" +
                        "Besonderheiten: Das Tor und die vorderseitige Außenwand fehlen. Die Garage ist an die Doppelgarage ohne eigene seitliche Außenwand angebaut.\n" +
                        "Allgemeinbeurteilung: Bei dem zu bewertenden Objekt handelt es sich um eine Garage mit einem Stellplatz für einen PKW. Die Funktionalität des Stellplatzes ist gegeben.\n",
                result.getBuildingDescription());

        assertEquals("Versorgungsleitungen, wie Wasser, Abwasser und Strom vom Hausanschluss bis an das öffentliche Netz\n" + "Hofbefestigung vor dem Haus aus Betonsteinpflaster\n" +
                "Terrasse aus Betonplatten\n" + "Gartenmauern bei der Terrasse\n" + "Metallschiebetor\n" + "Anpflanzungen wie Wiese, Bäume und Sträucher\n", result.getOutdoorDescription());

    }

}
