package com.veragg.website.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.veragg.website.domain.Court;
import com.veragg.website.domain.State;
import com.veragg.website.repository.CourtRepo;
import com.veragg.website.repository.StateRepo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class CourtServiceImpl_when_findBy_name_zip_is_called {

    private static final String NEW_COURT_NAME = "Freiburg_new";
    private static final String ZIP_CODE = "79098";
    private static final String LOCATION_NAME = "Landshut";
    private static final String COURT_NOT_EXISTED_NAME = "court not existed";
    private static final String COURT_NAME = "Freiburg";

    private CourtService sut;

    @Mock
    private CourtRepo courtRepo;

    @Mock
    private StateRepo stateRepo;

    @Mock
    private Court courtFound;

    @Mock
    private Court newCourt;

    @Mock
    private State state;

    @Before
    public void setup() {
        sut = new CourtServiceImpl(courtRepo, stateRepo);
        when(courtRepo.findByNameAndState(eq(COURT_NAME), eq(state))).thenReturn(newCourt);
        when(courtRepo.findByNameAndState(eq(COURT_NAME), eq(null))).thenReturn(null);
    }

    @Test
    public void and_no_courts_found_by_name_and_state_not_found_by_zip_but_found_by_full_location_then_new_court_should_return() {

        // Arrange
        when(courtRepo.findAllByName(eq(COURT_NOT_EXISTED_NAME))).thenReturn(Collections.emptyList());
        when(courtRepo.save(any(Court.class))).thenReturn(newCourt);
        when(stateRepo.findByZipCodeLocations_zipCode(eq(ZIP_CODE))).thenReturn(null);
        when(stateRepo.findByFullLocation(eq(COURT_NOT_EXISTED_NAME))).thenReturn(state);

        // Act
        Court courtFound = sut.findBy(COURT_NOT_EXISTED_NAME, ZIP_CODE);

        // Assert
        assertNotNull(courtFound);
        assertEquals(newCourt, courtFound);

    }

    @Test
    public void and_several_courts_found_by_name_and_state_not_found_by_zip_and_found_by_full_location_then_court_should_return() {

        // Arrange
        List<Court> courtsFound = Arrays.asList(courtFound, courtFound);
        when(courtRepo.findAllByName(eq(COURT_NAME))).thenReturn(courtsFound);
        when(stateRepo.findByZipCodeLocations_zipCode(eq(ZIP_CODE))).thenReturn(null);
        when(stateRepo.findByFullLocation(eq(COURT_NAME))).thenReturn(state);

        // Act
        Court courtFound = sut.findBy(COURT_NAME, ZIP_CODE);

        // Assert
        assertNotNull(courtFound);
        assertEquals(newCourt, courtFound);

    }

    @Test
    public void and_several_courts_found_by_name_and_state_not_found_by_zip_and_found_several_by_full_location_but_one_by_location_then_return_court() {

        // Arrange
        State stateFoundByFullLocation = mock(State.class);
        List<Court> courtsFound = Arrays.asList(courtFound, courtFound);
        when(courtRepo.findAllByName(eq(COURT_NAME))).thenReturn(courtsFound);
        when(stateRepo.findByZipCodeLocations_zipCode(eq(ZIP_CODE))).thenReturn(null);
        when(stateRepo.findDistinctByZipCodeLocations_locationWithAddition(eq(COURT_NAME))).thenReturn(Arrays.asList(stateFoundByFullLocation, stateFoundByFullLocation));
        when(stateRepo.findByLocation(eq(COURT_NAME))).thenReturn(state);

        // Act
        Court courtFound = sut.findBy(COURT_NAME, ZIP_CODE);

        // Assert
        assertNotNull(courtFound);
        assertEquals(newCourt, courtFound);

    }

    @Test
    public void and_several_courts_found_by_name_and_state_not_found_by_zip_and_found_several_by_both_locations_then_return_null() {

        // Arrange
        State stateFoundByFullLocation = mock(State.class);
        List<Court> courtsFound = Arrays.asList(courtFound, courtFound);
        when(courtRepo.findAllByName(eq(COURT_NAME))).thenReturn(courtsFound);
        when(stateRepo.findByZipCodeLocations_zipCode(eq(ZIP_CODE))).thenReturn(null);
        when(stateRepo.findDistinctByZipCodeLocations_locationWithAddition(eq(COURT_NAME))).thenReturn(Arrays.asList(stateFoundByFullLocation, stateFoundByFullLocation));
        when(stateRepo.findDistinctByZipCodeLocations_location(eq(COURT_NAME))).thenReturn(Arrays.asList(stateFoundByFullLocation, stateFoundByFullLocation));

        // Act
        Court courtFound = sut.findBy(COURT_NAME, ZIP_CODE);

        // Assert
        assertNull(courtFound);

    }

    @Test
    public void and_several_courts_found_by_name_and_state_found_and_court_found_by_name_state_then_court_should_return() {

        // Arrange
        List<Court> courtsFound = Arrays.asList(courtFound, courtFound);
        when(courtRepo.findAllByName(eq(COURT_NAME))).thenReturn(courtsFound);
        when(stateRepo.findByZipCodeLocations_zipCode(eq(ZIP_CODE))).thenReturn(state);

        // Act
        Court courtFound = sut.findBy(COURT_NAME, ZIP_CODE);

        // Assert
        assertNotNull(courtFound);
        assertEquals(newCourt, courtFound);

    }

    @Test
    public void and_several_courts_found_by_name_and_state_found_and_no_court_found_then_new_court_created_and_return() {

        // Arrange
        List<Court> courtsFound = Arrays.asList(courtFound, courtFound);
        when(courtRepo.findAllByName(eq(COURT_NAME))).thenReturn(courtsFound);
        when(stateRepo.findByZipCodeLocations_zipCode(eq(ZIP_CODE))).thenReturn(state);
        when(courtRepo.findByNameAndState(eq(COURT_NAME), eq(state))).thenReturn(null);
        when(courtRepo.save(any(Court.class))).thenReturn(newCourt);

        // Act
        Court courtFound = sut.findBy(COURT_NAME, ZIP_CODE);

        // Assert
        assertNotNull(courtFound);
        assertEquals(newCourt, courtFound);

    }

    @Test
    public void and_several_courts_found_by_name_and_state_not_found_by_any_then_return_null() {

        // Arrange
        List<Court> courtsFound = Arrays.asList(courtFound, courtFound);
        when(courtRepo.findAllByName(eq(COURT_NAME))).thenReturn(courtsFound);
        when(stateRepo.findByZipCodeLocations_zipCode(eq(ZIP_CODE))).thenReturn(null);
        when(stateRepo.findByFullLocation(eq(COURT_NAME))).thenReturn(null);
        when(stateRepo.findByLocation(eq(COURT_NAME))).thenReturn(null);

        // Act
        Court courtFound = sut.findBy(COURT_NAME, ZIP_CODE);

        // Assert
        assertNull(courtFound);

    }

    @Test
    public void and_several_courts_found_by_name_and_no_state_found_then_null_should_return() {

        // Arrange
        List<Court> courtsFound = Arrays.asList(courtFound, courtFound);
        when(courtRepo.findAllByName(eq(COURT_NAME))).thenReturn(courtsFound);
        when(stateRepo.findByZipCodeLocations_zipCode(eq(ZIP_CODE))).thenReturn(null);

        // Act
        Court courtFound = sut.findBy(COURT_NAME, ZIP_CODE);

        // Assert
        assertNull(courtFound);

    }

    @Test
    public void and_only_one_court_found_by_name_then_court_should_return() {

        // Arrange
        List<Court> courtsFound = Collections.singletonList(courtFound);
        when(courtRepo.findAllByName(eq(COURT_NAME))).thenReturn(courtsFound);

        // Act
        Court courtFound = sut.findBy(COURT_NAME, ZIP_CODE);

        // Assert
        assertNotNull(courtFound);

    }

    @Test
    public void and_name_passed_court_not_found_then_new_court_should_return() {

        // Arrange
        when(courtRepo.findAllByName(eq(COURT_NOT_EXISTED_NAME))).thenReturn(Collections.emptyList());
        when(courtRepo.save(any(Court.class))).thenReturn(courtFound);
        when(stateRepo.findByZipCodeLocations_zipCode(eq(ZIP_CODE))).thenReturn(state);

        // Act
        Court courtFound = sut.findBy(COURT_NOT_EXISTED_NAME, ZIP_CODE);

        // Assert
        assertNotNull(courtFound);
        assertEquals(this.courtFound, courtFound);

    }

    @Test
    public void and_name_passed_court_not_found_then_new_court_should_return_with_state_name_set() {

        // Arrange
        ArgumentCaptor<Court> courtArgumentCaptor = ArgumentCaptor.forClass(Court.class);
        when(courtRepo.findAllByName(eq(NEW_COURT_NAME))).thenReturn(Collections.emptyList());
        when(courtRepo.save(any(Court.class))).thenReturn(courtFound);
        when(stateRepo.findByZipCodeLocations_zipCode(eq(ZIP_CODE))).thenReturn(state);

        // Act
        Court courtFound = sut.findBy(NEW_COURT_NAME, ZIP_CODE);

        // Assert
        assertNotNull(courtFound);
        verify(courtRepo).save(courtArgumentCaptor.capture());
        assertEquals(NEW_COURT_NAME, courtArgumentCaptor.getValue().getName());
        assertEquals(state, courtArgumentCaptor.getValue().getState());

    }

    @Test(expected = NullPointerException.class)
    public void and_name_passed_court_not_found_and_state_not_found_then_NPE_expected() {

        // Arrange
        when(courtRepo.findAllByName(eq(COURT_NOT_EXISTED_NAME))).thenReturn(Collections.emptyList());
        when(stateRepo.findByZipCodeLocations_zipCode(eq(ZIP_CODE))).thenReturn(null);

        // Act
        Court courtFound = sut.findBy(COURT_NOT_EXISTED_NAME, ZIP_CODE);

        // Assert
        assertNull(courtFound);

    }

    @Test(expected = NullPointerException.class)
    public void and_null_name_passed_with_only_one_court_found_then_NPE_expected() {

        // Arrange
        List<Court> courtsFound = Collections.singletonList(courtFound);
        when(courtRepo.findAllByName(eq(COURT_NAME))).thenReturn(courtsFound);

        // Act
        Court courtFound = sut.findBy(null, ZIP_CODE);

        // Assert
        assertNull(courtFound);

    }

}
