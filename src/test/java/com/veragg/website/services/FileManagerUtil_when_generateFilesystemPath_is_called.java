package com.veragg.website.services;

import java.io.File;
import java.nio.file.Path;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class FileManagerUtil_when_generateFilesystemPath_is_called {

    @Test
    public void given_valid_parameters_then_path_return() {

        //Arrange
        String storageRootPath = File.separator + "storage" + File.separator + "rootPath" + File.separator;

        //Act
        Path result = FileManagerUtil.generateFilesystemPath(storageRootPath, "storageFileName");

        //Assert
        assertNotNull(result);
        assertEquals(5, result.getNameCount());
        assertEquals("storageFileName", result.getName(4).toString());
        assertEquals("t", result.getName(3).toString());
        assertEquals("s", result.getName(2).toString());
        assertEquals("rootPath", result.getName(1).toString());
        assertEquals("storage", result.getName(0).toString());

    }

    @Test
    public void given_storageRootPath_no_end_separator_then_separator_added_return() {

        //Arrange
        String storageRootPathEndNoSeparator = File.separator + "storage" + File.separator + "rootPath";

        //Act
        Path result = FileManagerUtil.generateFilesystemPath(storageRootPathEndNoSeparator, "storageFileName");

        //Assert
        assertNotNull(result);
        assertEquals(5, result.getNameCount());
        assertEquals("storageFileName", result.getName(4).toString());
        assertEquals("t", result.getName(3).toString());
        assertEquals("s", result.getName(2).toString());
        assertEquals("rootPath", result.getName(1).toString());
        assertEquals("storage", result.getName(0).toString());

    }

    @Test
    public void given_storageRootPath_null_then_NPE_thrown() {

        //Arrange
        //Act

        //Assert
        assertThrows(NullPointerException.class, () -> FileManagerUtil.generateFilesystemPath(null, "storageName"));

    }

    @Test
    public void given_storageName_null_then_NPE_thrown() {

        //Arrange
        //Act

        //Assert
        assertThrows(NullPointerException.class, () -> FileManagerUtil.generateFilesystemPath("storageRootPath", null));

    }

}
