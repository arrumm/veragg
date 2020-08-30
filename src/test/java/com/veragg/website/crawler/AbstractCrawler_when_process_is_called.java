package com.veragg.website.crawler;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veragg.website.crawler.mapping.AuctionMapperService;
import com.veragg.website.crawler.model.HanmarkAuctionDTO;
import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.services.AuctionService;
import com.veragg.website.services.AuctionSourceService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyZeroInteractions;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        InternetUtils.class,
        LoggerFactory.class
})
public class AbstractCrawler_when_process_is_called {

    private static final String AUCTION_URL = "auctionUrl";
    private static final String AUCTION_URL_CONTENT = "auctionUrlContent";
    private static final String ANOTHER_AUCTION_URL = "anotherAuctionUrl";
    private static final String ANOTHER_AUCTION_URL_CONTENT = "anotherAuctionUrlContent";
    private AbstractCrawler sut;

    @Mock
    AuctionMapperService<HanmarkAuctionDTO> mapperService;

    @Mock
    AuctionService auctionService;

    @Mock
    Logger logger;

    @Mock
    HanmarkAuctionDTO auctionDTO;

    @Mock
    Auction auction;

    @Mock
    AuctionSourceService auctionSourceService;

    @Mock
    AuctionSource auctionSource;

    @Mock
    IOException ioException;

    @Mock
    ParseException parseException;

    @Before
    public void setUp() {
        initMocks(this);
        HanmarkCrawler hanmarkCrawler = new HanmarkCrawler(mapperService, auctionService, auctionSourceService);
        sut = Mockito.spy(hanmarkCrawler);
        when(sut.getMaxCrawlDepth()).thenReturn(2);
        PowerMockito.mockStatic(InternetUtils.class);
        mockStatic(LoggerFactory.class);
        PowerMockito.when(LoggerFactory.getLogger(HanmarkCrawler.class)).thenReturn(logger);
        Whitebox.setInternalState(sut, logger);
    }

    @Test
    public void given_source_is_not_null_urls_fetched_mapped_then_auction_saved_with_source() throws IOException, ParseException {

        //Arrange
        doReturn(new HashSet<String>() {{
            add(AUCTION_URL);
        }}).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());
        PowerMockito.when(InternetUtils.getPageContent(eq(AUCTION_URL))).thenReturn(AUCTION_URL_CONTENT);

        HanmarkAuctionDTO oneAuctionDTO = mock(HanmarkAuctionDTO.class);
        doReturn(oneAuctionDTO).when(sut).fetchAuction(any(), eq(AUCTION_URL));
        Auction oneAuction = mock(Auction.class);
        doReturn(oneAuction).when(mapperService).map(eq(oneAuctionDTO));
        doReturn(auctionSource).when(auctionSourceService).findByName(anyString());

        //Act
        sut.crawl();

        //Assert
        verify(oneAuction).setSource(eq(auctionSource));

    }

    @Test
    public void given_source_is_null_urls_fetched_mapped_then_auction_draft_saved_null_source() throws IOException, ParseException {

        //Arrange
        doReturn(new HashSet<String>() {{
            add(AUCTION_URL);
        }}).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());
        PowerMockito.when(InternetUtils.getPageContent(eq(AUCTION_URL))).thenReturn(AUCTION_URL_CONTENT);

        HanmarkAuctionDTO oneAuctionDTO = mock(HanmarkAuctionDTO.class);
        doReturn(oneAuctionDTO).when(sut).fetchAuction(any(), eq(AUCTION_URL));
        Auction oneAuction = mock(Auction.class);
        doReturn(oneAuction).when(mapperService).map(eq(oneAuctionDTO));
        doReturn(null).when(auctionSourceService).findByName(anyString());

        //Act
        sut.crawl();

        //Assert
        verify(oneAuction).setSource(eq(null));

    }

    @Test
    public void given_urls_fetched_mapped_then_auction_draft_saved() throws IOException, ParseException {

        //Arrange
        doReturn(new HashSet<String>() {{
            add(ANOTHER_AUCTION_URL);
            add(AUCTION_URL);
        }}).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());
        PowerMockito.when(InternetUtils.getPageContent(eq(ANOTHER_AUCTION_URL))).thenReturn(ANOTHER_AUCTION_URL_CONTENT);
        PowerMockito.when(InternetUtils.getPageContent(eq(AUCTION_URL))).thenReturn(AUCTION_URL_CONTENT);

        HanmarkAuctionDTO oneAuctionDTO = mock(HanmarkAuctionDTO.class);
        HanmarkAuctionDTO anotherAuctionDTO = mock(HanmarkAuctionDTO.class);

        doReturn(oneAuctionDTO).when(sut).fetchAuction(any(), eq(AUCTION_URL));
        doReturn(anotherAuctionDTO).when(sut).fetchAuction(any(), eq(ANOTHER_AUCTION_URL));

        Auction oneAuction = mock(Auction.class);
        Auction anotherAuction = mock(Auction.class);

        doReturn(oneAuction).when(mapperService).map(eq(oneAuctionDTO));
        doReturn(anotherAuction).when(mapperService).map(eq(anotherAuctionDTO));

        //Act
        sut.crawl();

        //Assert
        verify(auctionService).saveDraft(eq(oneAuction));
        verify(auctionService).saveDraft(eq(anotherAuction));

    }

    @Test
    public void given_fetch_auction_throw_exception_for_one_url_then_exception_caught_and_process_continue() throws IOException, ParseException {

        //Arrange
        doReturn(new HashSet<String>() {{
            add("auctionUrlNPE");
            add(AUCTION_URL);
        }}).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());
        PowerMockito.when(InternetUtils.getPageContent(eq("auctionUrlNPE"))).thenReturn(AUCTION_URL_CONTENT);
        PowerMockito.when(InternetUtils.getPageContent(eq(AUCTION_URL))).thenReturn(AUCTION_URL_CONTENT);
        doThrow(ioException).when(sut).fetchAuction(any(), eq("auctionUrlNPE"));
        doReturn(auctionDTO).when(sut).fetchAuction(any(), eq(AUCTION_URL));
        doReturn(auction).when(mapperService).map(eq(auctionDTO));

        //Act
        sut.crawl();

        //Assert
        verify(mapperService, times(1)).map(eq(auctionDTO));
        verify(auctionService, times(1)).saveDraft(eq(auction));
        verifyNoMoreInteractions(auctionService, mapperService);

    }

    @Test
    public void given_map_auction_throw_exception_then_exception_caught_and_logged() throws IOException, ParseException {

        //Arrange
        doReturn(new HashSet<>(Collections.singletonList(AUCTION_URL))).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());
        PowerMockito.when(InternetUtils.getPageContent(eq(AUCTION_URL))).thenReturn(AUCTION_URL_CONTENT);
        doReturn(auctionDTO).when(sut).fetchAuction(any(), eq(AUCTION_URL));
        doThrow(parseException).when(mapperService).map(eq(auctionDTO));

        //Act
        sut.crawl();

        //Assert
        verifyZeroInteractions(auctionService);
        verify(logger).error(eq("Auction draft parse from [{}] failed"), eq(auctionDTO), eq(parseException));

    }

    @Test
    public void given_fetch_auction_throw_exception_then_exception_caught_and_logged() throws IOException {

        //Arrange
        doReturn(new HashSet<>(Collections.singletonList(AUCTION_URL))).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());
        PowerMockito.when(InternetUtils.getPageContent(eq(AUCTION_URL))).thenReturn(AUCTION_URL_CONTENT);
        doThrow(ioException).when(sut).fetchAuction(any(), anyString());

        //Act
        sut.crawl();

        //Assert
        verifyZeroInteractions(mapperService);
        verify(logger).error(eq("Page data fetch from [{}] failed"), eq(AUCTION_URL), eq(ioException));

    }

    @Test(expected = NullPointerException.class)
    public void given_null_urls_then_NPE_expected() {

        //Arrange
        doReturn(new HashSet<String>(Collections.singletonList(null))).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        //Act
        sut.crawl();

        //Assert
        PowerMockito.verifyStatic(InternetUtils.class, times(1));
        verifyNoMoreInteractions(sut);

    }

    @Test
    public void given_no_auction_urls_then_no_further_actions() throws IOException {

        //Arrange
        doReturn(Collections.EMPTY_SET).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        //Act
        sut.crawl();

        //Assert
        PowerMockito.verifyStatic(InternetUtils.class, never());
        InternetUtils.getPageContent(anyString());

    }

}
