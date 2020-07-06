package com.veragg.website.crawler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.logging.log4j.util.Strings;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.LoggerFactory;

import com.veragg.website.crawler.mapping.AuctionMapperService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

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
    private AbstractCrawler sut;
    private HanmarkCrawler hanmarkCrawler;

    @Mock
    AuctionMapperService mapper;

    @Before
    public void setUp() {
        initMocks(this);
        hanmarkCrawler = new HanmarkCrawler(mapper);
        sut = Mockito.spy(hanmarkCrawler);
        PowerMockito.mockStatic(InternetUtils.class);
    }

    //exception is thrown
    //some urls repeated

    @Ignore
    @Test
    public void given_urls_collected_some_repeated_then_return_urls_set() throws IOException {

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
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, 2, crawlPattern, collectPattern);

        //Assert
        assertNotNull(result);
        assertEquals(0, result.size());

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
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, 2, crawlPattern, collectPattern);

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
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, 2, crawlPattern, collectPattern);

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
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, 2, EMPTY_PATTERN, EMPTY_PATTERN);

        //Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(sut, times(1)).fetchUrls(any(), anyString());

    }

    @Test
    public void given_no_urls_on_last_level_then_empty_set_returned() throws IOException {

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
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, 2, crawlPattern, collectPattern);

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
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, 2, EMPTY_PATTERN, EMPTY_PATTERN);

        //Assert
        assertNotNull(result);
        assertEquals(3, result.size());

    }

    @Test(expected = NullPointerException.class)
    public void given_crawlPattern_is_null_then_NPE_expected() {
        //Arrange
        //Act
        sut.collectAuctionUrls("url", 1, 3, null, EMPTY_PATTERN);
        //Assert
    }

    @Test(expected = NullPointerException.class)
    public void given_collectPattern_is_null_then_NPE_expected() {
        //Arrange
        //Act
        sut.collectAuctionUrls("url", 1, 3, EMPTY_PATTERN, null);
        //Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_currentDepth_more_maxDepth_then_IAE_expected() {
        //Arrange
        //Act
        sut.collectAuctionUrls("url", 2, 1, EMPTY_PATTERN, EMPTY_PATTERN);
        //Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_currentDepth_negative_then_IAE_expected() {
        //Arrange
        //Act
        sut.collectAuctionUrls("url", -12, 1, EMPTY_PATTERN, EMPTY_PATTERN);
        //Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_maxDepth_negative_then_IAE_expected() {
        //Arrange
        //Act
        sut.collectAuctionUrls("url", -12, -11, EMPTY_PATTERN, EMPTY_PATTERN);
        //Assert
    }

}
