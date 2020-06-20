package com.veragg.website.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StateRepo_when_findByZip_is_called {

    @Autowired
    StateRepo stateRepo;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void given_smth_then_smth_should_happen() {

        //Arrange

        //Act
        Object result = stateRepo.findByZip("");

        //Assert
        assertNotNull(result);

    }

}
