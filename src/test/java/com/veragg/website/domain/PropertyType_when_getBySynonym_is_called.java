package com.veragg.website.domain;

import java.util.Set;

import org.junit.Test;
import org.springframework.util.CollectionUtils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PropertyType_when_getBySynonym_is_called {

    @Test
    public void given_mixed_properties_synonym_then_return_multiple_properties() {

        //Arrange

        //Act
        Set<PropertyType> result = PropertyType.getBySynonym("Wohn- und Geschäftshaus");

        //Assert
        assertNotNull(result);
        assertTrue(result.contains(PropertyType.COMMERCIAL_PROPERTY));
        assertTrue(result.contains(PropertyType.ONE_FAMILY_HOUSE));

    }

    @Test
    public void given_null_synonym_then_return_empty_properties() {

        //Arrange

        //Act
        Set<PropertyType> result = PropertyType.getBySynonym(null);

        //Assert
        assertNotNull(result);
        assertTrue(CollectionUtils.isEmpty(result));

    }

    @Test
    public void given_synonym_not_found_then_return_empty_properties() {

        //Arrange

        //Act
        Set<PropertyType> result = PropertyType.getBySynonym("Zweifamilienhaus");

        //Assert
        assertNotNull(result);
        assertTrue(CollectionUtils.isEmpty(result));

    }

}
