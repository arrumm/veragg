package com.veragg.website.crawler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.nonNull;

@Getter
@Setter
public class PageData {

    private Logger LOGGER = LoggerFactory.getLogger(PageData.class);

    private String url;

    private String content;

    public PageData(String url) {
        this.url = url;
    }

    public PageData getData() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpClient.execute(new HttpGet(this.url))) {
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    if (nonNull(entity)) {
                        this.content = IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
                    }
                } else {
                    LOGGER.warn("Page data from url [{}] cannot be fetched, response status is [{}]", this.url, status);
                }
            }
        }
        return this;
    }

}
