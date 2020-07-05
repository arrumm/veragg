package com.veragg.website.crawler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

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

    //max depth reached and urls collected

    //some urls repeated
    @Ignore
    @Test
    public void given_urls_collected_on_second_attempt_then_return_set_of_urls_and_fetchUrls_called_four_times_and_repeated_urls_excluded() throws IOException {

        //Arrange
        Pattern crawlPattern = Pattern.compile("crawl");
        Pattern collectPattern = Pattern.compile("collect");

        String firstLevelPageContent = "nextUrl startUrl anotherNextUrl";
        PowerMockito.when(InternetUtils.getPageContent(eq("startUrl"))).thenReturn(firstLevelPageContent);//done

        String secondLevelPageContent1 = "nextNextUrl nextUrl anotherNextUrl";
        PowerMockito.when(InternetUtils.getPageContent(eq("nextUrl"))).thenReturn(secondLevelPageContent1);
        String secondLevelPageContent2 = "anotherNextUrlNextUrl anotherNextUrl";
        PowerMockito.when(InternetUtils.getPageContent(eq("anotherNextUrl"))).thenReturn(secondLevelPageContent2);

        String lastLevelPageContent1 = "anotherUrlNot anotherNotToCollect";
        PowerMockito.when(InternetUtils.getPageContent(eq("nextNextUrl"))).thenReturn(lastLevelPageContent1);
        String lastLevelPageContent2 = "noUrls";
        PowerMockito.when(InternetUtils.getPageContent(eq("anotherNextUrlNextUrl"))).thenReturn(lastLevelPageContent2);

        doReturn(Collections.EMPTY_SET).when(sut).fetchUrls(eq(collectPattern), eq(firstLevelPageContent));
        Set<String> urls1Level = new HashSet<String>() {{
            add("nextUrl");
            add("startUrl");
            add("anotherNextUrl");
        }};
        doReturn(urls1Level).when(sut).fetchUrls(eq(crawlPattern), eq(firstLevelPageContent));

        doReturn(Collections.EMPTY_SET).when(sut).fetchUrls(eq(collectPattern), eq(secondLevelPageContent1));
        Set<String> urls2Level1 = new HashSet<String>() {{
            add("nextNextUrl");
            add("nextUrl");
            add("anotherNextUrl");
        }};
        doReturn(urls2Level1).when(sut).fetchUrls(eq(crawlPattern), eq(secondLevelPageContent1));

        doReturn(Collections.EMPTY_SET).when(sut).fetchUrls(eq(collectPattern), eq(secondLevelPageContent2));
        Set<String> urls2Level2 = new HashSet<String>() {{
            add("anotherNextUrlNextUrl");
            add("anotherNextUrl");
        }};
        doReturn(urls2Level2).when(sut).fetchUrls(eq(crawlPattern), eq(secondLevelPageContent2));

        doReturn(Collections.EMPTY_SET).when(sut).fetchUrls(eq(collectPattern), eq(lastLevelPageContent1));
        Set<String> urls3Level1 = new HashSet<String>() {{
            add("anotherUrlNot");
            add("anotherNotToCollect");
        }};
        doReturn(urls3Level1).when(sut).fetchUrls(eq(crawlPattern), eq(lastLevelPageContent1));

        doReturn(Collections.EMPTY_SET).when(sut).fetchUrls(eq(collectPattern), eq(lastLevelPageContent2));
        Set<String> urls3Level2 = new HashSet<String>() {{
            add("noUrls");
        }};
        doReturn(urls3Level2).when(sut).fetchUrls(eq(crawlPattern), eq(lastLevelPageContent2));

        //Act
        Set<String> result = sut.collectAuctionUrls("startUrl", 0, 2, crawlPattern, collectPattern);

        //Assert
        assertNotNull(result);
        assertEquals(0, result.size());

    }

    @Test
    public void given_urls_collected_on_second_attempt_then_return_set_of_urls_and_fetchUrls_called_four_times() throws IOException {

        //Arrange
        Pattern crawlPattern = Pattern.compile("crawl");
        Pattern collectPattern = Pattern.compile("collect");

        PowerMockito.when(InternetUtils.getPageContent(eq("startUrl"))).thenReturn("startUrlContent");
        Set<String> urls = new HashSet<String>() {{
            add("urlSecond");
            add("urlFirst");
        }};
        doReturn(Collections.EMPTY_SET).when(sut).fetchUrls(eq(collectPattern), eq("startUrlContent"));
        doReturn(urls).when(sut).fetchUrls(eq(crawlPattern), eq("startUrlContent"));

        Set<String> urlsFromFirst = new HashSet<String>() {{
            add("urlFirst2");
            add("urlFirst1");
        }};
        PowerMockito.when(InternetUtils.getPageContent(eq("urlFirst"))).thenReturn("urlFirstContent");
        doReturn(urlsFromFirst).when(sut).fetchUrls(eq(collectPattern), eq("urlFirstContent"));
        Set<String> urlsFromSecond = new HashSet<String>() {{
            add("urlSecond2");
            add("urlSecond1");
            add("urlSecond3");
        }};
        PowerMockito.when(InternetUtils.getPageContent(eq("urlSecond"))).thenReturn("urlSecondContent");
        doReturn(urlsFromSecond).when(sut).fetchUrls(eq(collectPattern), eq("urlSecondContent"));

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
        PowerMockito.when(InternetUtils.getPageContent(anyString())).thenReturn("");
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
        Pattern crawlPattern = Pattern.compile("crawl");
        Pattern collectPattern = Pattern.compile("collect");

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
