package com.veragg.website.crawler.mapping;

import java.text.ParseException;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.veragg.website.crawler.model.HanmarkAuctionDTO;
import com.veragg.website.domain.Auction;
import com.veragg.website.domain.BuyLimit;
import com.veragg.website.domain.Court;
import com.veragg.website.domain.Document;
import com.veragg.website.domain.DocumentType;
import com.veragg.website.domain.PropertyType;
import com.veragg.website.domain.State;
import com.veragg.website.services.CourtService;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class HanmarkAuctionMapperServiceImpl_when_map_is_called {

    private HanmarkAuctionMapperServiceImpl sut;

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
        sut = new HanmarkAuctionMapperServiceImpl(courtService, normalizationService);
        when(normalizationService.normalizeCity(anyString())).thenCallRealMethod();
        when(court.getName()).thenReturn("Wittlich");
        when(court.getState()).thenReturn(state);
        when(state.getId()).thenReturn("RP");
        when(courtService.findBy(eq("Wittlich"), eq("54538"))).thenReturn(court);
    }

    @Test
    public void and_valid_model_passed_then_draft_should_return() throws ParseException {
        // Arrange
        // @formatter:off
        HanmarkAuctionDTO auctionDTO = HanmarkAuctionDTO.builder()
                .fileNumber("12b K 3/19")
                .amount("161.700,00 EUR")
                .appointmentDate("16.06.2020 14:00 Uhr")
                .courtName("Wittlich")
                .propertyTypeName("Einfamilienhaus")
                .cityAddress("54538 Hontheim")
                .streetAddress("Bergweg 7")
                .sourceUrl("https://hanmark.de/einfamhausurl.html")
                .build();
        // @formatter:on
        auctionDTO.setLimitDescription("keine Angabe");

        auctionDTO.setImageLinks(Arrays.asList("url/image2.jpg", "url/image1.png"));
        auctionDTO.setOtherDocumentLinks(new HashSet<>(Arrays.asList("url/otherDoc.pdf", "url/anotherOtherDoc.pdf")));
        auctionDTO.setExpertiseLinks(Collections.singletonList("url/expertise.pdf"));

        // Act
        final Auction result = sut.map(auctionDTO);

        // Assert
        assertEquals("Wittlich", result.getCourt().getName());
        assertEquals("RP", result.getCourt().getState().getId());
        assertEquals("12b K 3/19", result.getFileNumber());
        assertEquals(1, result.getPropertyTypes().size());
        assertTrue(result.getPropertyTypes().contains(PropertyType.ONE_FAMILY_HOUSE));
        assertEquals("Hontheim", result.getAddress().getCity());
        assertEquals("54538", result.getAddress().getZipCode());
        assertEquals("7", result.getAddress().getNumber());
        assertEquals("Bergweg", result.getAddress().getStreet());
        assertEquals(161700, result.getAmount());
        assertEquals(2020, result.getAppointment().getYear());
        assertEquals(Month.JUNE.getValue(), result.getAppointment().getMonthValue());
        assertEquals(16, result.getAppointment().getDayOfMonth());
        assertEquals(14, result.getAppointment().getHour());
        assertEquals(BuyLimit.NA, result.getBuyLimit());
        assertEquals("https://hanmark.de/einfamhausurl.html", result.getSourceUrl());

        assertEquals(5, result.getDocuments().size());
        //@formatter:off
        assertThat(result.getDocuments(), hasItem(allOf(
                Matchers.<Document>hasProperty("url", is("url/image2.jpg")),
                Matchers.<Document>hasProperty("documentType", is(DocumentType.IMAGE)))));
        assertThat(result.getDocuments(), hasItem(allOf(
                Matchers.<Document>hasProperty("url", is("url/image1.png")),
                Matchers.<Document>hasProperty("documentType", is(DocumentType.IMAGE)))));
        assertThat(result.getDocuments(), hasItem(allOf(
                Matchers.<Document>hasProperty("url", is("url/otherDoc.pdf")),
                Matchers.<Document>hasProperty("documentType", is(DocumentType.OTHER)))));
        assertThat(result.getDocuments(), hasItem(allOf(
                Matchers.<Document>hasProperty("url", is("url/expertise.pdf")),
                Matchers.<Document>hasProperty("documentType", is(DocumentType.EXPERTISE)))));
        //@formatter:on

    }
}
