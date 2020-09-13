package com.veragg.website.crawler;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
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
import com.veragg.website.domain.Court;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        PageData.class,
        AbstractCrawler.class,
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
    PageData pageData;

    @Mock
    ParseException parseException;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        HanmarkCrawler hanmarkCrawler = new HanmarkCrawler(mapperService, auctionService, auctionSourceService);
        sut = Mockito.spy(hanmarkCrawler);
        when(sut.getMaxCrawlDepth()).thenReturn(2);
        mockStatic(LoggerFactory.class);
        PowerMockito.when(LoggerFactory.getLogger(HanmarkCrawler.class)).thenReturn(logger);
        Whitebox.setInternalState(sut, logger);

        PowerMockito.whenNew(PageData.class).withArguments(eq(AUCTION_URL)).thenReturn(pageData);
        when(pageData.fetch()).thenReturn(pageData);
        when(pageData.getContent()).thenReturn(AUCTION_URL_CONTENT);
    }

    @Test
    public void given_source_is_not_null_urls_fetched_mapped_then_auction_saved_with_source() throws Exception {

        //Arrange
        doReturn(new HashSet<String>() {{
            add(AUCTION_URL);
        }}).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        HanmarkAuctionDTO oneAuctionDTO = mock(HanmarkAuctionDTO.class);
        doReturn(oneAuctionDTO).when(sut).fetchAuction(eq(pageData));
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

        HanmarkAuctionDTO oneAuctionDTO = mock(HanmarkAuctionDTO.class);
        doReturn(oneAuctionDTO).when(sut).fetchAuction(eq(pageData));
        Auction oneAuction = mock(Auction.class);
        doReturn(oneAuction).when(mapperService).map(eq(oneAuctionDTO));
        doReturn(null).when(auctionSourceService).findByName(anyString());

        //Act
        sut.crawl();

        //Assert
        verify(oneAuction).setSource(eq(null));

    }

    @Test
    public void given_urls_fetched_mapped_then_auction_draft_saved() throws Exception {

        //Arrange
        doReturn(new HashSet<String>() {{
            add(ANOTHER_AUCTION_URL);
            add(AUCTION_URL);
        }}).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        PowerMockito.whenNew(PageData.class).withArguments(eq(AUCTION_URL)).thenReturn(pageData);
        PageData pageDataFromAnotherUrl = mock(PageData.class);
        PowerMockito.whenNew(PageData.class).withArguments(eq(ANOTHER_AUCTION_URL)).thenReturn(pageDataFromAnotherUrl);

        when(pageData.fetch()).thenReturn(pageData);
        when(pageData.getContent()).thenReturn(AUCTION_URL_CONTENT);
        when(pageDataFromAnotherUrl.fetch()).thenReturn(pageDataFromAnotherUrl);
        when(pageDataFromAnotherUrl.getContent()).thenReturn(ANOTHER_AUCTION_URL_CONTENT);

        HanmarkAuctionDTO oneAuctionDTO = mock(HanmarkAuctionDTO.class);
        HanmarkAuctionDTO anotherAuctionDTO = mock(HanmarkAuctionDTO.class);

        doReturn(oneAuctionDTO).when(sut).fetchAuction(eq(pageData));
        doReturn(anotherAuctionDTO).when(sut).fetchAuction(eq(pageDataFromAnotherUrl));

        Auction oneAuction = mock(Auction.class);
        Auction anotherAuction = mock(Auction.class);

        doReturn(oneAuction).when(mapperService).map(eq(oneAuctionDTO));
        doReturn(anotherAuction).when(mapperService).map(eq(anotherAuctionDTO));

        //Act
        sut.crawl();

        //Assert
        verify(auctionService, times(1)).saveDraft(eq(oneAuction));
        verify(auctionService, times(1)).saveDraft(eq(anotherAuction));

    }

    @Test
    public void given_fetch_auction_throw_exception_for_one_url_then_exception_caught_and_process_continue() throws Exception {

        //Arrange
        doReturn(new HashSet<String>() {{
            add("auctionUrlNPE");
            add(AUCTION_URL);
        }}).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        PowerMockito.whenNew(PageData.class).withArguments(eq(AUCTION_URL)).thenReturn(pageData);
        PageData pageDataNPE = mock(PageData.class);
        PowerMockito.whenNew(PageData.class).withArguments(eq("auctionUrlNPE")).thenReturn(pageDataNPE);

        when(pageData.fetch()).thenReturn(pageData);
        when(pageData.getContent()).thenReturn(AUCTION_URL_CONTENT);
        when(pageDataNPE.fetch()).thenReturn(pageDataNPE);
        when(pageDataNPE.getContent()).thenReturn(AUCTION_URL_CONTENT);

        doThrow(ioException).when(sut).fetchAuction(eq(pageData));
        doReturn(auctionDTO).when(sut).fetchAuction(eq(pageDataNPE));

        doReturn(auction).when(mapperService).map(eq(auctionDTO));

        doReturn(auctionSource).when(auctionSourceService).findByName(anyString());
        Court court = mock(Court.class);
        when(auction.getFileNumber()).thenReturn(StringUtils.EMPTY);
        when(auction.getCourt()).thenReturn(court);

        //Act
        sut.crawl();

        //Assert
        verify(mapperService).map(eq(auctionDTO));
        verify(auctionService).findDraftBy(anyString(), any(Court.class), any(AuctionSource.class));
        verify(auctionService).saveDraft(eq(auction));
        verifyNoMoreInteractions(auctionService, mapperService);

    }

    @Test
    public void given_map_auction_throw_exception_then_exception_caught_and_logged() throws IOException, ParseException {

        //Arrange
        doReturn(new HashSet<>(Collections.singletonList(AUCTION_URL))).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());
        doReturn(auctionDTO).when(sut).fetchAuction(any(PageData.class));
        doThrow(parseException).when(mapperService).map(eq(auctionDTO));

        //Act
        sut.crawl();

        //Assert
        verifyNoMoreInteractions(auctionService);
        verify(logger).error(eq("Auction draft parse from [{}] failed"), eq(auctionDTO), eq(parseException));

    }

    @Test
    public void given_fetch_auction_throw_exception_then_exception_caught_and_logged() throws IOException {

        //Arrange
        doReturn(new HashSet<>(Collections.singletonList(AUCTION_URL))).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());
        doThrow(ioException).when(sut).fetchAuction(any(PageData.class));

        //Act
        sut.crawl();

        //Assert
        verifyNoInteractions(mapperService);
        verify(logger).error(eq("Page data fetch from [{}] failed"), eq(AUCTION_URL), eq(ioException));

    }

    @Test(expected = NullPointerException.class)
    public void given_null_urls_then_NPE_expected() {

        //Arrange
        doReturn(new HashSet<String>(Collections.singletonList(null))).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        //Act
        sut.crawl();

        //Assert
        verifyNoMoreInteractions(sut);

    }

    @Test
    public void given_no_auction_urls_then_no_further_actions() throws IOException {

        //Arrange
        doReturn(Collections.EMPTY_SET).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        //Act
        sut.crawl();

        //Assert
        verify(pageData, never());
        pageData.fetch();

    }

}
