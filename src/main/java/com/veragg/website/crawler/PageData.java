package com.veragg.website.crawler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
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

    private String referer;

    private String content;

    private Boolean available = Boolean.FALSE;

    public PageData(@NonNull String url) {
        this.url = url;
    }

    public PageData(@NonNull String url, String referer) {
        this.url = url;
        this.referer = referer;
    }

    public PageData fetch() throws IOException {
        HttpGet request = new HttpGet(this.url);
        if (nonNull(referer)) {
            request.setHeader("Referer", referer);
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(request)) {
            readResponse(response);
        }
        return this;
    }

    public PageData post(Map<String, String> formParams) throws IOException {
        HttpPost request = new HttpPost(this.url);
        if (nonNull(referer)) {
            request.setHeader("Referer", referer);
        }
        List<NameValuePair> params = new ArrayList<>();
        formParams.forEach((key, value) -> params.add(new BasicNameValuePair(key, value)));
        request.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(request)) {
            readResponse(response);
        }
        return this;
    }

    private void readResponse(CloseableHttpResponse response) throws IOException {
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
