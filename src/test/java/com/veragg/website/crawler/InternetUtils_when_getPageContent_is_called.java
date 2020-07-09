package com.veragg.website.crawler;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyZeroInteractions;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        HttpClients.class,
        EntityUtils.class,
        LoggerFactory.class
})
public class InternetUtils_when_getPageContent_is_called {

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private CloseableHttpResponse httpResponse;

    @Mock
    private StatusLine statusLine;

    @Mock
    private HttpEntity httpEntity;

    @Mock
    IOException ioException;

    @Mock
    Logger logger;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(httpClient);
        PowerMockito.mockStatic(EntityUtils.class);

        mockStatic(LoggerFactory.class);
        PowerMockito.when(LoggerFactory.getLogger(InternetUtils.class)).thenReturn(logger);
        Whitebox.setInternalState(InternetUtils.class, logger);
    }

    @Test(expected = IOException.class)
    public void given_create_client_throw_exception_then_exception_is_thrown() throws IOException {

        //Arrange
        PowerMockito.when(HttpClients.createDefault()).then(throwIOException());

        //Act
        InternetUtils.getPageContent("url");

        //Assert
    }

    private Answer throwIOException() {
        return invocation -> {
            throw ioException;
        };
    }

    @Test(expected = IOException.class)
    public void given_client_execute_throw_exception_then_exception_is_thrown() throws IOException {

        //Arrange
        when(httpClient.execute(any(HttpGet.class))).thenThrow(ioException);

        //Act
        InternetUtils.getPageContent("url");

        //Assert
    }

    @Test
    public void given_not_successful_get_request_then_empty_string_returned_and_warn_logged() throws IOException {

        //Arrange
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(403);

        //Act
        String result = InternetUtils.getPageContent("forbiddenUrl");

        //Assert
        assertNotNull(result);
        assertEquals(StringUtils.EMPTY, result);

        verify(logger).warn(eq("Page data from url [{}] cannot be fetched, response status [{}]"), eq("forbiddenUrl"), eq(403));
        verifyZeroInteractions(EntityUtils.class);

    }

    @Test
    public void given_successful_get_request_then_entity_body_returned() throws IOException {

        //Arrange
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        PowerMockito.when(EntityUtils.toString(eq(httpEntity))).thenReturn("entityBody");

        //Act
        String result = InternetUtils.getPageContent("url");

        //Assert
        assertNotNull(result);
        assertEquals("entityBody", result);

    }

    @Test
    public void given_null_entity_then_empty_string_returned() throws IOException {

        //Arrange
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(null);

        //Act
        String result = InternetUtils.getPageContent("url");

        //Assert
        assertNotNull(result);
        assertEquals(StringUtils.EMPTY, result);

    }

    @Test(expected = NullPointerException.class)
    public void given_null_url_then_IllegalArgumentException_expected() throws IOException {

        //Arrange

        //Act
        InternetUtils.getPageContent(null);

        //Assert

    }

}
