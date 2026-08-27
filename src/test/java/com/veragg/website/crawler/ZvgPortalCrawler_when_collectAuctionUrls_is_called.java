package com.veragg.website.crawler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.veragg.website.crawler.mapping.AuctionMapperService;
import com.veragg.website.crawler.model.ZvgPortalAuctionDTO;
import com.veragg.website.services.AuctionService;
import com.veragg.website.services.AuctionSourceService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        PageData.class,
        ZvgPortalCrawler.class
})
public class ZvgPortalCrawler_when_collectAuctionUrls_is_called {

    private static final String SEARCH_RESPONSE = "before index.php?button=showZvg&zvg_id=1&land_abk=by middle "
            + "index.php?button=showZvg&zvg_id=2&land_abk=by after";
    private static final String SEARCH_URL = "https://www.zvg-portal.de/index.php?button=Suchen&all=1";
    private static final String SEARCH_FORM_URL = "https://www.zvg-portal.de/index.php?button=Termine+suchen";

    private ZvgPortalCrawler sut;

    @Mock
    private AuctionMapperService<ZvgPortalAuctionDTO> mapperService;

    @Mock
    private AuctionService auctionService;

    @Mock
    private AuctionSourceService auctionSourceService;

    @Mock
    private PageData pageData;

    @Before
    public void setUp() throws Exception {
        sut = new ZvgPortalCrawler(mapperService, auctionService, auctionSourceService);
        PowerMockito.whenNew(PageData.class).withArguments(eq(SEARCH_URL), eq(SEARCH_FORM_URL)).thenReturn(pageData);
        when(pageData.post(anyMap())).thenReturn(pageData);
        when(pageData.getContent()).thenReturn(SEARCH_RESPONSE);
    }

    @Test
    public void given_every_state_queried_then_matched_urls_unioned() throws Exception {

        // Act
        Set<String> result = sut.collectAuctionUrls();

        // Assert
        Set<String> expected = new HashSet<String>() {{
            add("https://www.zvg-portal.de/index.php?button=showZvg&zvg_id=1&land_abk=by");
            add("https://www.zvg-portal.de/index.php?button=showZvg&zvg_id=2&land_abk=by");
        }};
        assertEquals(expected, result);

        verify(pageData, times(16)).post(anyMap());

    }

    @Test
    public void given_every_state_queried_then_land_abk_param_set_per_state() throws Exception {

        // Act
        sut.collectAuctionUrls();

        // Assert
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(pageData, times(16)).post(captor.capture());

        Set<String> stateCodesUsed = new HashSet<>();
        captor.getAllValues().forEach(params -> stateCodesUsed.add(params.get("land_abk")));

        assertEquals(16, stateCodesUsed.size());
        assertTrue(stateCodesUsed.containsAll(java.util.Arrays.asList("bw", "by", "be", "br", "hb", "hh", "he", "mv", "ni", "nw", "rp", "sl", "sn", "st", "sh", "th")));

    }

    @Test
    public void given_a_state_fetch_fails_then_other_states_still_processed() throws Exception {

        // Arrange
        PageData failingPageData = org.mockito.Mockito.mock(PageData.class);
        when(failingPageData.post(anyMap())).thenThrow(new java.io.IOException("boom"));
        PowerMockito.whenNew(PageData.class).withArguments(eq(SEARCH_URL), eq(SEARCH_FORM_URL)).thenReturn(failingPageData).thenReturn(pageData);

        // Act
        Set<String> result = sut.collectAuctionUrls();

        // Assert
        assertEquals(2, result.size());

    }

}
