package com.veragg.website.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.veragg.website.domain.State;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StateRepo_when_findByZipCodes_zipCode_is_called {

    @Autowired
    StateRepo stateRepo;

    @Test
    public void given_invalid_zip_code_then_null_should_return() {

        //Arrange

        //Act
        State result = stateRepo.findByZipCodeLocations_zipCode("999999");

        //Assert
        assertNull(result);

    }

    @Test
    public void given_null_zip_code_then_return_null() {

        //Arrange

        //Act
        State result = stateRepo.findByZipCodeLocations_zipCode(null);

        //Assert
        assertNull(result);

    }

    public void given_zip_code_no_states_then_return_null() {

        //Arrange

        //Act
        State result = stateRepo.findByZipCodeLocations_zipCode("12345");

        //Assert
        assertNull(result);

    }

    @Test
    public void given_valid_zip_code_then_state_should_return() {

        //Arrange

        //Act
        State result = stateRepo.findByZipCodeLocations_zipCode("12527");

        //Assert
        assertNotNull(result);
        assertEquals("BE", result.getId());

    }

}
