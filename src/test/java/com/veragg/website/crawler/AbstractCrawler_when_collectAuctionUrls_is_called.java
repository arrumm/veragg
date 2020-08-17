package com.veragg.website.crawler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.logging.log4j.util.Strings;
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
import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.services.AuctionService;
import com.veragg.website.services.AuctionSourceService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
public class AbstractCrawler_when_collectAuctionUrls_is_called {

    private static final Pattern EMPTY_PATTERN = Pattern.compile("");
    private static final String CRAWL = "crawl";
    private static final String COLLECT = "collect";
    private static final String START_URL_CONTENT = "startUrlContent";
    private static final String URL_FIRST_CONTENT = "urlFirstContent";
    private static final String URL_SECOND_CONTENT = "urlSecondContent";
    private static final String VISITED_URL = "visitedUrl";

    private AbstractCrawler sut;

    @Mock
    AuctionMapperService<HanmarkAuctionDTO> mapperService;

    @Mock
    AuctionService<AuctionDraft> auctionService;

    @Mock
    AuctionSourceService auctionSourceService;

    @Mock
    Logger logger;

    @Mock
    IOException ioException;

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
    public void given_get_page_content_throw_IOException_for_url_then_process_only_others_url() throws IOException {

        //Arrange
        Pattern crawlPattern = Pattern.compile(CRAWL);
        Pattern collectPattern = Pattern.compile(COLLECT);

        PowerMockito.when(InternetUtils.getPageContent(eq("startUrl"))).thenReturn(START_URL_CONTENT);
        Set<String> urls = new HashSet<String>() {{
            add("urlFirst");
            add("urlSecond");
        }};
        doReturn(Collections.EMPTY_SET).when(sut).fetchUrls(eq(collectPattern), eq(START_URL_CONTENT));
        doReturn(urls).when(sut).fetchUrls(eq(crawlPattern), eq(START_URL_CONTENT));

        Set<String> urlsFromFirst = new HashSet<String>() {{
            add("urlFirst2");
            add("urlFirst1");
        }};
        PowerMockito.when(InternetUtils.getPageContent(eq("urlFirst"))).thenReturn(URL_FIRST_CONTENT);
        doReturn(urlsFromFirst).when(sut).fetchUrls(eq(collectPattern), eq(URL_FIRST_CONTENT));
        PowerMockito.when(InternetUtils.getPageContent(eq("urlSecond"))).thenThrow(ioException);

        //Act
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, crawlPattern, collectPattern);

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sut, times(3)).fetchUrls(any(), anyString());
        verify(sut, never()).fetchUrls(eq(collectPattern), eq(URL_SECOND_CONTENT));
        verify(logger).error(eq("Error get content of [{}]"), eq("urlSecond"), eq(ioException));

    }

    @Test
    public void given_get_page_content_throw_IOException_then_exception_logged() throws IOException {

        //Arrange
        Pattern crawlPattern = Pattern.compile(CRAWL);
        Pattern collectPattern = Pattern.compile(COLLECT);

        PowerMockito.when(InternetUtils.getPageContent(eq("startUrl"))).thenThrow(ioException);

        //Act
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, crawlPattern, collectPattern);

        //Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(logger).error(eq("Error get content of [{}]"), eq("startUrl"), eq(ioException));

    }

    @Test
    public void given_already_visited_url_visited_only_once() throws IOException {

        //Arrange
        Pattern crawlPattern = Pattern.compile(CRAWL);
        Pattern collectPattern = Pattern.compile(COLLECT);

        Whitebox.setInternalState(sut, "visitedUrls", new HashSet<String>() {{
            add(VISITED_URL);
        }});

        //Act
        Set<String> result = sut.collectAuctionUrls(VISITED_URL, 0, crawlPattern, collectPattern);

        //Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        PowerMockito.verifyStatic(InternetUtils.class, never());
        InternetUtils.getPageContent(anyString());

    }

    @Test
    public void given_urls_collected_all_repeated_then_return_empty_set() throws IOException {

        //Arrange
        Pattern crawlPattern = Pattern.compile(CRAWL);
        Pattern collectPattern = Pattern.compile(COLLECT);

        PowerMockito.when(InternetUtils.getPageContent(eq("startUrl"))).thenReturn(START_URL_CONTENT);
        Set<String> sameUrls = new HashSet<String>() {{
            add("urlSecond");
            add("urlFirst");
            add("startUrl");
        }};
        doReturn(Collections.EMPTY_SET).when(sut).fetchUrls(eq(collectPattern), eq(START_URL_CONTENT));
        doReturn(sameUrls).when(sut).fetchUrls(eq(crawlPattern), eq(START_URL_CONTENT));

        PowerMockito.when(InternetUtils.getPageContent(eq("urlFirst"))).thenReturn(URL_FIRST_CONTENT);
        doReturn(sameUrls).when(sut).fetchUrls(eq(collectPattern), eq(URL_FIRST_CONTENT));

        PowerMockito.when(InternetUtils.getPageContent(eq("urlSecond"))).thenReturn(URL_SECOND_CONTENT);
        doReturn(sameUrls).when(sut).fetchUrls(eq(collectPattern), eq(URL_SECOND_CONTENT));

        //Act
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, crawlPattern, collectPattern);

        //Assert
        assertNotNull(result);
        assertEquals(0, result.size());

    }

    @Test
    public void given_urls_collected_on_second_attempt_then_return_set_of_urls_and_fetchUrls_called_four_times() throws IOException {

        //Arrange
        Pattern crawlPattern = Pattern.compile(CRAWL);
        Pattern collectPattern = Pattern.compile(COLLECT);

        PowerMockito.when(InternetUtils.getPageContent(eq("startUrl"))).thenReturn(START_URL_CONTENT);
        Set<String> urls = new HashSet<String>() {{
            add("urlSecond");
            add("urlFirst");
        }};
        doReturn(Collections.EMPTY_SET).when(sut).fetchUrls(eq(collectPattern), eq(START_URL_CONTENT));
        doReturn(urls).when(sut).fetchUrls(eq(crawlPattern), eq(START_URL_CONTENT));

        Set<String> urlsFromFirst = new HashSet<String>() {{
            add("urlFirst2");
            add("urlFirst1");
        }};
        PowerMockito.when(InternetUtils.getPageContent(eq("urlFirst"))).thenReturn(URL_FIRST_CONTENT);
        doReturn(urlsFromFirst).when(sut).fetchUrls(eq(collectPattern), eq(URL_FIRST_CONTENT));
        Set<String> urlsFromSecond = new HashSet<String>() {{
            add("urlSecond2");
            add("urlSecond1");
            add("urlSecond3");
        }};
        PowerMockito.when(InternetUtils.getPageContent(eq("urlSecond"))).thenReturn(URL_SECOND_CONTENT);
        doReturn(urlsFromSecond).when(sut).fetchUrls(eq(collectPattern), eq(URL_SECOND_CONTENT));

        //Act
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, crawlPattern, collectPattern);

        //Assert
        assertNotNull(result);
        assertEquals(5, result.size());
        verify(sut, times(4)).fetchUrls(any(), anyString());

    }

    @Test
    public void given_urls_collected_on_first_attempt_then_return_set_of_urls_and_fetchUrls_called_once() throws IOException {

        //Arrange
        PowerMockito.when(InternetUtils.getPageContent(anyString())).thenReturn(Strings.EMPTY);
        Set<String> urls = new HashSet<String>() {{
            add("url1");
            add("url2");
            add("url3");
        }};
        doReturn(urls).when(sut).fetchUrls(any(), anyString());

        //Act
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, EMPTY_PATTERN, EMPTY_PATTERN);

        //Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(sut, times(1)).fetchUrls(any(), anyString());

    }

    @Test
    public void given_no_urls_on_last_level_then_return_empty_set() throws IOException {

        //Arrange
        Pattern crawlPattern = Pattern.compile(CRAWL);
        Pattern collectPattern = Pattern.compile(COLLECT);

        PowerMockito.when(InternetUtils.getPageContent(anyString())).thenReturn("pageContent");

        doReturn(Collections.EMPTY_SET).when(sut).fetchUrls(eq(collectPattern), anyString());
        Set<String> urls = new HashSet<String>() {{
            add("nextUrl");
            add("startUrl");
        }};
        doReturn(urls).when(sut).fetchUrls(eq(crawlPattern), anyString());

        //Act
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, crawlPattern, collectPattern);

        //Assert
        assertNotNull(result);
        assertEquals(0, result.size());

    }

    @Test
    public void given_urls_collected_then_return_set_of_urls_and_initial_url_removed() throws IOException {

        //Arrange
        PowerMockito.when(InternetUtils.getPageContent(anyString())).thenReturn("");
        Set<String> urls = new HashSet<String>() {{
            add("url1");
            add("url2");
            add("startUrl");
            add("url3");
        }};
        doReturn(urls).when(sut).fetchUrls(any(), anyString());

        //Act
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, EMPTY_PATTERN, EMPTY_PATTERN);

        //Assert
        assertNotNull(result);
        assertEquals(3, result.size());

    }

    @Test(expected = NullPointerException.class)
    public void given_crawlPattern_is_null_then_NPE_expected() {
        //Arrange
        when(sut.getMaxCrawlDepth()).thenReturn(3);
        //Act
        sut.collectAuctionUrls("url", 1, null, EMPTY_PATTERN);
        //Assert
    }

    @Test(expected = NullPointerException.class)
    public void given_collectPattern_is_null_then_NPE_expected() {
        //Arrange
        when(sut.getMaxCrawlDepth()).thenReturn(3);
        //Act
        sut.collectAuctionUrls("url", 1, EMPTY_PATTERN, null);
        //Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_currentDepth_more_maxDepth_then_IAE_expected() {
        //Arrange
        when(sut.getMaxCrawlDepth()).thenReturn(1);
        //Act
        sut.collectAuctionUrls("url", 2, EMPTY_PATTERN, EMPTY_PATTERN);
        //Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_currentDepth_negative_then_IAE_expected() {
        //Arrange
        when(sut.getMaxCrawlDepth()).thenReturn(1);
        //Act
        sut.collectAuctionUrls("url", -1, EMPTY_PATTERN, EMPTY_PATTERN);
        //Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_maxDepth_negative_then_IAE_expected() {
        //Arrange
        when(sut.getMaxCrawlDepth()).thenReturn(-11);
        //Act
        sut.collectAuctionUrls("url", -12, EMPTY_PATTERN, EMPTY_PATTERN);
        //Assert
    }

}
