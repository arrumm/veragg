package com.veragg.website.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PropertyType_when_getByName_is_called {

    @Test
    public void given_null_name_then_return_null() {

        //Arrange

        //Act
        PropertyType result = PropertyType.getByName(null);

        //Assert
        assertNull(result);

    }

    @Test
    public void given_not_found_name_then_return_null() {

        //Arrange

        //Act
        PropertyType result = PropertyType.getByName("notFoundName");

        //Assert
        assertNull(result);

    }

    @Test
    public void given_found_name_then_return_property_type() {

        //Arrange

        //Act
        PropertyType result = PropertyType.getByName("Baugrundstück");

        //Assert
        assertNotNull(result);
        assertEquals("Baugrundstück", result.getName());

    }

}
