package com.veragg.website.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veragg.website.domain.Document;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        DownloadServiceImpl.class,
        LoggerFactory.class
})
public class DownloadServiceImpl_when_downloadFile_is_called {

    private static final String SAVED_FILE_PATH = "savedFilePath";
    private static final String FILE_NAME_UUID = "99990000-1111-2222-3333-444455556666";
    private static final String FILE_URL_STRING = "https://some.com/file.pdf";
    private static final String FILE_MALFORMED_URL_STRING = "https://malformed.url/file.pdf";
    private DownloadServiceImpl downloadService;

    @Mock
    FileManager fileManager;

    @Mock
    Document document;

    @Mock
    URL downloadUrl;

    @Mock
    InputStream inputStream;

    @Mock
    ReadableByteChannel readableByteChannel;

    @Mock
    Logger logger;

    @Before
    public void setup() throws Exception {
        downloadService = new DownloadServiceImpl(fileManager);
        when(fileManager.transferToFile(anyString(), any())).thenReturn(SAVED_FILE_PATH);
        when(document.getUrl()).thenReturn(FILE_URL_STRING);
        when(document.getStoreName()).thenReturn(FILE_NAME_UUID);
        whenNew(URL.class).withAnyArguments().thenReturn(downloadUrl);
        when(downloadUrl.openStream()).thenReturn(inputStream);
        mockStatic(Channels.class);
        when(Channels.newChannel(any(InputStream.class))).thenReturn(readableByteChannel);

        mockStatic(LoggerFactory.class);
        PowerMockito.when(LoggerFactory.getLogger(DownloadServiceImpl.class)).thenReturn(logger);
        Whitebox.setInternalState(DownloadServiceImpl.class, logger);
    }

    @Test
    public void given_document_transferToFile_throw_FileManagementException_then_error_logged() {

        //Arrange
        FileManagementException exception = mock(FileManagementException.class);
        when(fileManager.transferToFile(anyString(), any())).thenThrow(exception);

        //Act
        downloadService.downloadFile(document);

        //Assert
        verify(logger).error(eq("Unable to save save the file from url {}"), eq(FILE_URL_STRING), eq(exception));

    }

    @Test
    public void given_document_transferToFile_throw_NPE_then_NPE_expected() {

        //Arrange
        NullPointerException exception = mock(NullPointerException.class);
        when(fileManager.transferToFile(anyString(), any())).thenThrow(exception);

        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> downloadService.downloadFile(document));
    }

    @Test
    public void given_document_openStream_throw_exception_then_no_interaction_with_fileManager_and_error_logged() throws IOException {

        //Arrange
        IOException exception = mock(IOException.class);
        when(downloadUrl.openStream()).thenThrow(exception);

        //Act
        downloadService.downloadFile(document);

        //Assert
        verify(logger).error(eq("Unable to download file from url {}"), eq(FILE_URL_STRING), eq(exception));
        verifyNoInteractions(fileManager);
    }

    @Test
    public void given_document_URL_null_then_no_interaction_with_fileManager() throws Exception {

        //Arrange
        whenNew(URL.class).withParameterTypes(String.class).withArguments(FILE_URL_STRING).thenReturn(null);

        //Act
        downloadService.downloadFile(document);

        //Assert
        verifyNoInteractions(fileManager);
    }

    @Test
    public void given_document_null_then_NPE_expected() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> downloadService.downloadFile(null));
    }

    @Test
    public void given_document_and_file_saved_then_transferToFile_called() {

        //Arrange
        //Act
        downloadService.downloadFile(document);

        //Assert
        verify(fileManager).transferToFile(eq(FILE_NAME_UUID), eq(readableByteChannel));

    }

    @Test
    public void given_document_and_URL_malformed_then_error_logged_and_no_interaction_with_fileManager() throws Exception {

        //Arrange
        MalformedURLException exception = mock(MalformedURLException.class);
        when(document.getUrl()).thenReturn(FILE_MALFORMED_URL_STRING);
        whenNew(URL.class).withParameterTypes(String.class).withArguments(FILE_MALFORMED_URL_STRING).thenThrow(exception);

        //Act
        downloadService.downloadFile(document);

        //Assert
        verify(logger).error(eq("URL is wrong and cannot be fetched: {}"), eq(FILE_MALFORMED_URL_STRING), eq(exception));
        verifyNoInteractions(fileManager);
    }

}
