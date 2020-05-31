package com.veragg.website.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import com.veragg.website.crawler.model.HanmarkAuctionModel;

import static com.veragg.website.domain.PropertyType.ONE_FAMILY_HOUSE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
class HanmarkCrawler_when_parseAuction_is_called {

    @Autowired
    private HanmarkCrawler sut;

    @Before
    public void setup() {
    }

    @Test
    void and_valid_pageData_is_passed_then_auction_returned() throws IOException {
        // Arrange
        InputStream houseInputStream = getClass().getClassLoader().getResourceAsStream("hanmark-house.html");

        // Act
        HanmarkAuctionModel result = sut.parseAuction("pagePath", houseInputStream);

        // Assert
        assertNotNull(result);
        assertEquals("12b K 3/19", result.getFileNumber());
        assertEquals("Wittlich", result.getCourtName());
        assertEquals(ONE_FAMILY_HOUSE.getName(), result.getPropertyTypeName());

    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
