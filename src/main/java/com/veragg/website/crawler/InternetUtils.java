package com.veragg.website.crawler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.NonNull;

import static java.util.Objects.nonNull;

//todo: refactor to service
public class InternetUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternetUtils.class);

    public static String getPageContent(@NonNull String url) throws IOException {

        LOGGER.info("Get page content from {}", url);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    if (nonNull(entity)) {
                        return EntityUtils.toString(entity);
                    }
                } else {
                    LOGGER.warn("Page data from url [{}] cannot be fetched, response status [{}]", url, status);
                }
            }
        }
        return Strings.EMPTY;
    }

}
