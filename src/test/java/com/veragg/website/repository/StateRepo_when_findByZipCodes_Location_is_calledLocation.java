package com.veragg.website.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import com.veragg.website.domain.State;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StateRepo_when_findByZipCodes_Location_is_calledLocation {

    @Autowired
    StateRepo stateRepo;

    @Test
    public void given_null_location_then_return_null() {

        //Arrange

        //Act
        State result = stateRepo.findByZipCodeLocations_location(null);

        //Assert
        assertNull(result);

    }

    public void given_location_with_no_states_then_return_null() {

        //Arrange

        //Act
        State result = stateRepo.findByZipCodeLocations_location("Nowhere");

        //Assert
        assertNull(result);

    }

    @Test
    public void given_valid_location_then_state_should_return() {

        //Arrange

        //Act
        State result = stateRepo.findByZipCodeLocations_location("Fredenbeck");

        //Assert
        assertNotNull(result);
        assertEquals("NI", result.getId());

    }

    @Test(expected = IncorrectResultSizeDataAccessException.class)
    public void given_valid_location_with_2_states_then_exception_expected() {

        //Arrange

        //Act
        stateRepo.findByZipCodeLocations_location("Freiburg");

        //Assert

    }

}
