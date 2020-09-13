package com.veragg.website.crawler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.Series.REDIRECTION;
import static org.springframework.http.HttpStatus.Series.SUCCESSFUL;

@Getter
@Setter
public class PageData {

    private Logger LOGGER = LoggerFactory.getLogger(PageData.class);

    private String url;

    private String content;

    private Boolean available = Boolean.FALSE;

    public PageData(@NonNull String url) {
        this.url = url;
    }

    public PageData fetch() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpClient.execute(new HttpGet(this.url))) {
                int status = response.getStatusLine().getStatusCode();
                updateAvailability(status);
                if (Boolean.TRUE.equals(isAvailable())) {
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

    public PageData ping() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpClient.execute(new HttpGet(this.url))) {
                updateAvailability(response.getStatusLine().getStatusCode());
            }
        }
        return this;
    }

    private void updateAvailability(int status) {
        HttpStatus.Series series = HttpStatus.Series.resolve(status);
        this.available = Arrays.asList(SUCCESSFUL, REDIRECTION).contains(series) ? Boolean.TRUE : this.available;
    }

    public Boolean isAvailable() {
        return available;
    }
}
