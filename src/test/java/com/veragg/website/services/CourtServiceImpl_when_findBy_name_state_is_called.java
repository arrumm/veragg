package com.veragg.website.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.veragg.website.domain.Court;
import com.veragg.website.domain.State;
import com.veragg.website.repository.CourtRepo;
import com.veragg.website.repository.StateRepo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class CourtServiceImpl_when_findBy_name_state_is_called {

    private CourtService sut;

    @Mock
    private CourtRepo courtRepo;

    @Mock
    private StateRepo stateRepo;

    @Mock
    private Court court;

    @Mock
    private State state;

    @Before
    public void setUp() {
        sut = new CourtServiceImpl(courtRepo, stateRepo);
    }

    @Test(expected = NullPointerException.class)
    public void and_name_null_then_return_null() {

        // Arrange
        when(courtRepo.findByNameAndState(eq(null), eq(state))).thenReturn(null);

        // Act
        sut.findBy(null, state);

        // Assert

    }

    //    TODO: exception thrown
    @Test
    public void and_state_null_then_return_null() {

        // Arrange
        when(courtRepo.findByNameAndState(eq("name"), any(State.class))).thenReturn(null);

        // Act
        Court courtFound = sut.findBy("name", (State) null);

        // Assert
        assertNull(courtFound);

    }

    //TODO: exception thrown
    @Test
    public void and_both_state_name_null_then_return_null() {

        // Arrange
        when(courtRepo.findByNameAndState(eq("name"), eq(null))).thenReturn(null);

        // Act
        Court courtFound = sut.findBy("name", (State) null);

        // Assert
        assertNull(courtFound);

    }

    @Test
    public void and_both_state_name_not_null_then_return_court_found() {

        // Arrange
        when(courtRepo.findByNameAndState(eq("name"), eq(state))).thenReturn(court);

        // Act
        Court courtFound = sut.findBy("name", state);

        // Assert
        assertNotNull(courtFound);
        assertEquals(court, courtFound);

    }
}
