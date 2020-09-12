package com.veragg.website.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        HttpClients.class,
        IOUtils.class,
        LoggerFactory.class
})
public class PageData_when_getData_is_called {

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

    @Mock
    InputStream inputStream;

    PageData sut;

    @Before
    public void setUp() {
        mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(httpClient);
        mockStatic(IOUtils.class);

        sut = new PageData("url");

        mockStatic(LoggerFactory.class);
        PowerMockito.when(LoggerFactory.getLogger(PageData.class)).thenReturn(logger);
        Whitebox.setInternalState(sut, logger);
    }

    @Test(expected = IOException.class)
    public void given_create_client_throw_exception_then_exception_is_thrown() throws IOException {

        //Arrange
        PowerMockito.when(HttpClients.createDefault()).then(throwIOException());

        //Act
        sut.getData();

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
        sut.getData();

        //Assert
    }

    @Test
    public void given_not_successful_get_request_then_empty_string_returned_and_warn_logged() throws IOException {

        //Arrange
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(403);

        //Act
        PageData resultPageData = new PageData("forbiddenUrl").getData();

        //Assert
        assertNotNull(resultPageData);
        assertNull(resultPageData.getContent());

        verify(logger).warn(eq("Page data from url [{}] cannot be fetched, response status is [{}]"), eq("forbiddenUrl"), eq(403));
        verifyNoMoreInteractions(IOUtils.class);
        IOUtils.toString(any(InputStream.class), eq(StandardCharsets.UTF_8));

    }

    @Test
    public void given_successful_get_request_then_entity_body_returned() throws IOException {

        //Arrange
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(inputStream);
        PowerMockito.when(IOUtils.toString(eq(inputStream), eq(StandardCharsets.UTF_8))).thenReturn("entityBody");

        //Act
        PageData result = new PageData("url").getData();

        //Assert
        assertNotNull(result);
        assertEquals("entityBody", result.getContent());

    }

    @Test
    public void given_null_entity_then_empty_string_returned() throws IOException {

        //Arrange
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(null);

        //Act
        PageData result = new PageData("url").getData();

        //Assert
        assertNotNull(result);
        assertNull(result.getContent());

    }

    @Test(expected = NullPointerException.class)
    public void given_null_url_then_IllegalArgumentException_expected() throws IOException {

        //Arrange

        //Act
        new PageData(null);

        //Assert

    }

}
