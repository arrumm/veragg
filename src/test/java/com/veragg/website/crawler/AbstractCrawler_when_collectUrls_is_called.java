package com.veragg.website.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        InternetUtils.class,
        LoggerFactory.class
})
public class AbstractCrawler_when_collectUrls_is_called {

    private AbstractCrawler sut;

    @Mock
    Pattern crawlPattern;

    @Mock
    Pattern collectUrlPattern;

    @Mock
    Matcher crawlMatcher;

    @Mock
    Matcher collectUrlMatcher;

    @Before
    public void setUp() throws Exception {
        sut = PowerMockito.mock(AbstractCrawler.class, CALLS_REAL_METHODS);
        PowerMockito.mockStatic(InternetUtils.class);

        //        sut = PowerMockito.mock(AbstractCrawler.class);
        //        PowerMockito.doCallRealMethod().when(sut).collectUrls(anyString(), anyInt(), anyInt(), any(Pattern.class), any(Pattern.class));
        //        PowerMockito.doReturn("").when(sut, "fetchUrls", any(), any());

        when(crawlPattern.matcher(anyString())).thenReturn(crawlMatcher);
        when(collectUrlPattern.matcher(anyString())).thenReturn(collectUrlMatcher);
    }

    //urls to collect not found

    //max depth is reached but we didn't collect

    @Test(expected = NullPointerException.class)
    public void given_crawlPattern_is_null_then_NPE_expected() {
        //Arrange
        //Act
        sut.collectAuctionUrls("url", 1, 3, null, collectUrlPattern);
        //Assert
    }

    @Test(expected = NullPointerException.class)
    public void given_collectPattern_is_null_then_NPE_expected() {
        //Arrange
        //Act
        sut.collectAuctionUrls("url", 1, 3, crawlPattern, null);
        //Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_currentDepth_more_maxDepth_then_IAE_expected() {
        //Arrange
        //Act
        sut.collectAuctionUrls("url", 2, 1, crawlPattern, collectUrlPattern);
        //Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_currentDepth_negative_then_IAE_expected() {
        //Arrange
        //Act
        sut.collectAuctionUrls("url", -12, 1, crawlPattern, collectUrlPattern);
        //Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_maxDepth_negative_then_IAE_expected() {
        //Arrange
        //Act
        sut.collectAuctionUrls("url", -12, -11, crawlPattern, collectUrlPattern);
        //Assert
    }

    //    @Ignore
    //    @Test
    //    public void given_smth_then_smth_should_happen() throws IOException {
    //
    //        //Arrange
    //        //depends on url return different output
    //        PowerMockito.when(InternetUtils.getPageContent(anyString())).thenReturn("");
    //
    //        //Act
    //        Set<String> resultUrls = sut.collectUrls("url", 1, 3, crawlPattern, collectUrlPattern);
    //
    //        //Assert
    //        assertNotNull(resultUrls);
    //        assertEquals(2, resultUrls.size());
    //
    //    }

}
