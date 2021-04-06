package com.veragg.website.crawler;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyZeroInteractions;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        PageData.class,
        AbstractCrawler.class,
        LoggerFactory.class
})
public class AbstractCrawler_WHEN_crawl_is_called {

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
    Document jsoupDocument;

    @Mock
    ParseException parseException;

    @Mock
    HanmarkParser hanmarkParser;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        HanmarkCrawler hanmarkCrawler = new HanmarkCrawler(mapperService, auctionService, auctionSourceService, hanmarkParser);
        sut = Mockito.spy(hanmarkCrawler);
        when(sut.getMaxCrawlDepth()).thenReturn(2);
        mockStatic(LoggerFactory.class);
        PowerMockito.when(LoggerFactory.getLogger(HanmarkCrawler.class)).thenReturn(logger);
        Whitebox.setInternalState(sut, logger);

        PowerMockito.whenNew(PageData.class).withArguments(eq(AUCTION_URL)).thenReturn(pageData);
        when(pageData.getUrl()).thenReturn("url");
        when(pageData.fetch()).thenReturn(pageData);
        doReturn(jsoupDocument).when(sut).getDocumentFromContent(eq(pageData), anyString());
        when(pageData.getContent()).thenReturn(AUCTION_URL_CONTENT);
    }

    // TODO Roman: 03-Apr-21 test the process is running

    @Test
    public void GIVEN_source_is_not_null_urls_fetched_mapped_THEN_auction_saved_with_source() throws Exception {

        //Arrange
        doReturn(new HashSet<String>() {{
            add(AUCTION_URL);
        }}).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        HanmarkAuctionDTO oneAuctionDTO = mock(HanmarkAuctionDTO.class);
        doReturn(oneAuctionDTO).when(hanmarkParser).parse((jsoupDocument));
        Auction oneAuction = mock(Auction.class);
        doReturn(oneAuction).when(mapperService).map((oneAuctionDTO));
        doReturn(auctionSource).when(auctionSourceService).findByName(anyString());

        //Act
        sut.crawl();

        //Assert
        verify(oneAuction).setSource(eq(auctionSource));

    }

    @Test
    public void GIVEN_source_is_null_urls_fetched_mapped_THEN_auction_draft_saved_null_source() throws IOException, ParseException {

        //Arrange
        doReturn(new HashSet<String>() {{
            add(AUCTION_URL);
        }}).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        HanmarkAuctionDTO oneAuctionDTO = mock(HanmarkAuctionDTO.class);
        doReturn(oneAuctionDTO).when(hanmarkParser).parse(eq(jsoupDocument));
        Auction oneAuction = mock(Auction.class);
        doReturn(oneAuction).when(mapperService).map(eq(oneAuctionDTO));
        doReturn(null).when(auctionSourceService).findByName(anyString());

        //Act
        sut.crawl();

        //Assert
        verify(oneAuction).setSource(eq(null));

    }

    @Test
    public void GIVEN_urls_fetched_mapped_THEN_auction_draft_saved() throws Exception {

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
        when(pageData.getUrl()).thenReturn("baseUrl");
        when(pageDataFromAnotherUrl.getUrl()).thenReturn("baseUrl");
        when(pageDataFromAnotherUrl.fetch()).thenReturn(pageDataFromAnotherUrl);

        Document jsoupDocumentFromAnotherUrl = mock(Document.class);
        doReturn(jsoupDocumentFromAnotherUrl).when(sut).getDocumentFromContent(eq(pageDataFromAnotherUrl), anyString());
        when(pageDataFromAnotherUrl.getContent()).thenReturn(ANOTHER_AUCTION_URL_CONTENT);

        HanmarkAuctionDTO oneAuctionDTO = mock(HanmarkAuctionDTO.class);
        HanmarkAuctionDTO anotherAuctionDTO = mock(HanmarkAuctionDTO.class);

        doReturn(oneAuctionDTO).when(hanmarkParser).parse(eq(jsoupDocument));
        doReturn(anotherAuctionDTO).when(hanmarkParser).parse(eq(jsoupDocumentFromAnotherUrl));

        Auction oneAuction = mock(Auction.class);
        Auction anotherAuction = mock(Auction.class);

        doReturn(oneAuction).when(mapperService).map(eq(oneAuctionDTO));
        doReturn(anotherAuction).when(mapperService).map(eq(anotherAuctionDTO));

        //Act
        sut.crawl();

        //Assert
        verify(auctionService, times(1)).save(eq(oneAuction));
        verify(auctionService, times(1)).save(eq(anotherAuction));

    }

    @Test
    public void GIVEN_fetch_auction_throw_exception_for_one_url_THEN_exception_caught_and_process_continue() throws Exception {

        //Arrange
        doReturn(new HashSet<String>() {{
            add("auctionUrlException");
            add(AUCTION_URL);
        }}).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        PowerMockito.whenNew(PageData.class).withArguments(eq(AUCTION_URL)).thenReturn(pageData);
        PageData pageDataException = mock(PageData.class);
        PowerMockito.whenNew(PageData.class).withArguments(eq("auctionUrlException")).thenReturn(pageDataException);

        when(pageData.fetch()).thenReturn(pageData);
        when(pageData.getContent()).thenReturn(AUCTION_URL_CONTENT);
        doThrow(ioException).when(pageDataException).fetch();

        doReturn(jsoupDocument).when(sut).getDocumentFromContent(eq(pageData), anyString());
        doReturn(auctionDTO).when(hanmarkParser).parse(eq(jsoupDocument));

        doReturn(auction).when(mapperService).map(eq(auctionDTO));

        doReturn(auctionSource).when(auctionSourceService).findByName(anyString());
        Court court = mock(Court.class);
        when(auction.getFileNumber()).thenReturn(StringUtils.EMPTY);
        when(auction.getCourt()).thenReturn(court);

        //Act
        sut.crawl();

        //Assert
        verify(mapperService).map(eq(auctionDTO));
        verify(auctionService).findBy(anyString(), any(Court.class), any(AuctionSource.class));
        verify(auctionService).save(eq(auction));
        verifyNoMoreInteractions(auctionService, mapperService);

    }

    @Test
    public void GIVEN_map_auction_throw_exception_THEN_exception_caught_and_logged() throws ParseException {

        //Arrange
        doReturn(new HashSet<>(Collections.singletonList(AUCTION_URL))).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());
        doReturn(auctionDTO).when(hanmarkParser).parse(any(Document.class));
        doThrow(parseException).when(mapperService).map(eq(auctionDTO));

        //Act
        sut.crawl();

        //Assert
        verifyNoMoreInteractions(auctionService);
        verify(logger).error(eq("Auction draft parse from [{}] failed"), eq(auctionDTO), eq(parseException));

    }

    @Test
    public void GIVEN_fetch_auction_throw_exception_THEN_exception_caught_and_logged() throws Exception {

        //Arrange
        doReturn(new HashSet<>(Collections.singletonList("auctionUrlException"))).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());
        PageData pageDataException = mock(PageData.class);
        PowerMockito.whenNew(PageData.class).withArguments(eq("auctionUrlException")).thenReturn(pageDataException);
        doThrow(ioException).when(pageDataException).fetch();

        //Act
        sut.crawl();

        //Assert
        verifyNoInteractions(mapperService);
        verify(logger).error(eq("Page data fetch from [{}] failed"), eq("auctionUrlException"), eq(ioException));

    }

    @Test(expected = NullPointerException.class)
    public void GIVEN_null_urls_THEN_NPE_expected() {

        //Arrange
        doReturn(new HashSet<String>(Collections.singletonList(null))).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        //Act
        sut.crawl();

        //Assert
        verifyNoMoreInteractions(sut);

    }

    @Test
    public void GIVEN_no_auction_urls_THEN_no_actions_performed() {

        //Arrange
        doReturn(Collections.EMPTY_SET).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        //Act
        sut.crawl();

        //Assert
        verifyZeroInteractions(auctionSourceService);
        verifyZeroInteractions(pageData);

    }

}
