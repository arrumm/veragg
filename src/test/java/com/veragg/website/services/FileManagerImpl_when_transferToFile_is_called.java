package com.veragg.website.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.verification.PrivateMethodVerification;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        StoragePathService.class,
        Files.class,
        FileManagerImpl.class
})
public class FileManagerImpl_when_transferToFile_is_called {

    private static final String FILE_STORE_NAME = "fileStoreName";
    private static final String STORAGE_ROOT_PATH = "storageRootPath";

    @Mock
    ReadableByteChannel readableByteChannel;

    @Mock
    Path storagePath;

    @Mock
    Path savedPath;

    @Mock
    Path storagePathParent;

    @Mock
    FileOutputStream fileOutputStream;

    @Mock
    FileChannel fileChannel;

    @Mock
    File savedFile;

    @Mock
    File storagePathFile;

    @Mock
    File storageParentPathFile;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private FileManagerImpl sut;

    @Before
    public void setup() throws Exception {
        mockStatic(StoragePathService.class);
        when(StoragePathService.generateFilesystemPath(eq(STORAGE_ROOT_PATH), eq(FILE_STORE_NAME))).thenReturn(storagePath);
        when(storagePath.getParent()).thenReturn(storagePathParent);
        when(storagePath.toFile()).thenReturn(storagePathFile);
        when(storagePathParent.toFile()).thenReturn(storageParentPathFile);
        when(storagePathFile.exists()).thenReturn(Boolean.FALSE);
        when(storageParentPathFile.exists()).thenReturn(Boolean.TRUE);

        mockStatic(Files.class);
        when(Files.createFile(eq(storagePath))).thenReturn(savedPath);
        whenNew(FileOutputStream.class).withAnyArguments().thenReturn(fileOutputStream);
        when(fileOutputStream.getChannel()).thenReturn(fileChannel);
        when(fileChannel.transferFrom(eq(readableByteChannel), anyLong(), anyLong())).thenReturn(Long.valueOf(1));

        when(savedPath.toFile()).thenReturn(savedFile);
        when(savedFile.getName()).thenReturn("storagePathFullPath");

        sut = new FileManagerImpl();
        ReflectionTestUtils.setField(sut, "storageRootPath", STORAGE_ROOT_PATH, String.class);
    }

    @Test
    public void given_file_exist_and_deleteFile_throw_exception_then_FileManagementException_expected() throws IOException {

        //Arrange
        when(storagePathFile.exists()).thenReturn(Boolean.TRUE);
        IOException exception = mock(IOException.class);
        PowerMockito.doThrow(exception).when(Files.class);
        Files.delete(eq(storagePath));

        //Act
        //Assert
        assertThrows(FileManagementException.class, () -> sut.transferToFile(FILE_STORE_NAME, readableByteChannel));

    }

    @Test
    public void given_file_exists_then_deleteFile_is_called_and_update_logged() throws Exception {

        //Arrange
        sut = PowerMockito.spy(sut);
        when(storagePathFile.exists()).thenReturn(Boolean.TRUE);

        //Act
        sut.transferToFile(FILE_STORE_NAME, readableByteChannel);

        //Assert
        PrivateMethodVerification privateMethodInvocation = verifyPrivate(sut);
        privateMethodInvocation.invoke("deleteFile", storagePath);

        //TODO: add logging verification

    }

    @Test
    public void given_path_parent_not_exist_and_create_directories_throw_exception_then_FileManagementException_expected() throws IOException {

        //Arrange
        IOException exception = mock(IOException.class);
        when(storageParentPathFile.exists()).thenReturn(Boolean.FALSE);
        when(Files.createDirectories(any(Path.class))).thenThrow(exception);

        //Act
        //Assert
        assertThrows(FileManagementException.class, () -> sut.transferToFile(FILE_STORE_NAME, readableByteChannel));

    }

    @Test
    public void given_path_parent_not_exist_then_create_directories_called() throws Exception {

        //Arrange
        sut = PowerMockito.spy(sut);
        when(storageParentPathFile.exists()).thenReturn(Boolean.FALSE);

        //Act
        sut.transferToFile(FILE_STORE_NAME, readableByteChannel);

        //Assert
        PrivateMethodVerification privateMethodInvocation = verifyPrivate(sut);
        privateMethodInvocation.invoke("createDirectories", storagePathParent);

    }

    @Test
    public void given_fileStoreName_null_then_NPE_expected() {

        //Arrange

        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.transferToFile(null, readableByteChannel));

    }

    @Test
    public void given_byteChannel_null_then_NPE_expected() {

        //Arrange

        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.transferToFile("someName", null));

    }

}
