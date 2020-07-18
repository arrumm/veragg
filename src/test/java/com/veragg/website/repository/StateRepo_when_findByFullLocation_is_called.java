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
public class StateRepo_when_findByFullLocation_is_called {

    @Autowired
    StateRepo stateRepo;

    @Test
    public void given_null_location_then_return_null() {

        //Arrange

        //Act
        State result = stateRepo.findByFullLocation(null);

        //Assert
        assertNull(result);

    }

    public void given_location_with_no_states_then_return_null() {

        //Arrange

        //Act
        State result = stateRepo.findByFullLocation("Nowhere");

        //Assert
        assertNull(result);

    }

    @Test
    public void given_valid_location_then_state_should_return() {

        //Arrange

        //Act
        State result = stateRepo.findByFullLocation("Auerbach/Vogtl.");

        //Assert
        assertNotNull(result);
        assertEquals("SN", result.getId());

    }

    public void given_valid_location_with_several_states_then_return_null() {

        //Arrange

        //Act
        State result = stateRepo.findByFullLocation("Freiburg");

        //Assert
        assertNull(result);

    }

}
