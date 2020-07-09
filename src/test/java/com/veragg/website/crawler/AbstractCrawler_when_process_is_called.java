package com.veragg.website.crawler;

import java.io.IOException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        InternetUtils.class,
        LoggerFactory.class
})
public class AbstractCrawler_when_process_is_called {

    private AbstractCrawler sut;
    private HanmarkCrawler hanmarkCrawler;

    @Mock
    AuctionMapperService mapper;

    @Mock
    Logger logger;

    @Mock
    IOException ioException;

    @Before
    public void setUp() {
        initMocks(this);
        hanmarkCrawler = new HanmarkCrawler(mapper);
        sut = Mockito.spy(hanmarkCrawler);
        when(sut.getMaxCrawlDepth()).thenReturn(2);
        PowerMockito.mockStatic(InternetUtils.class);
        mockStatic(LoggerFactory.class);
        PowerMockito.when(LoggerFactory.getLogger(HanmarkCrawler.class)).thenReturn(logger);
        Whitebox.setInternalState(sut, logger);
    }

    @Test(expected = NullPointerException.class)
    public void given_null_urls_then_NPE_expected() throws IOException {

        //Arrange
        doReturn(new HashSet<String>(Collections.singletonList(null))).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        //Act
        sut.process();

        //Assert
        PowerMockito.verifyStatic(InternetUtils.class, times(1));
        verify(sut, never()).parseAuction(anyString(), any());

    }

    @Test
    public void given_no_auction_urls_then_no_further_actions() throws IOException {

        //Arrange
        doReturn(Collections.EMPTY_SET).when(sut).collectAuctionUrls(anyString(), anyInt(), any(), any());

        //Act
        sut.process();

        //Assert
        PowerMockito.verifyStatic(InternetUtils.class, never());
        InternetUtils.getPageContent(anyString());

    }

}
