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
public class StateRepo_when_findByZip_is_called {

    @Autowired
    StateRepo stateRepo;

    @Test
    public void given_valid_zip_code_then_state_should_return() {

        //Arrange

        //Act
        State result = stateRepo.findByZip("12439");

        //Assert
        assertNotNull(result);
        assertEquals("BE", result.getId());

    }

    @Test
    public void given_invalid_zip_code_then_null_should_return() {

        //Arrange

        //Act
        State result = stateRepo.findByZip("999999");

        //Assert
        assertNull(result);

    }

    @Test
    public void given_null_zip_code_then_NPE_should_thrown() {

        //Arrange

        //Act
        State result = stateRepo.findByZip(null);

        //Assert
        assertNull(result);

    }

}
