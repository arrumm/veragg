package com.veragg.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veragg.website.domain.State;

@Repository
public interface StateRepo extends JpaRepository<State, String> {

    State findByZipCodeLocations_zipCode(String zip);

    default State findByFullLocation(String location) {
        List<State> states = findDistinctByZipCodeLocations_locationWithAddition(location);
        if (!states.isEmpty()) {
            if (states.size() == 1) {
                return states.get(0);
            }
        }
        return null;
    }

    List<State> findDistinctByZipCodeLocations_locationWithAddition(String location);

    default State findByLocation(String location) {
        List<State> states = findDistinctByZipCodeLocations_location(location);
        if (!states.isEmpty()) {
            if (states.size() == 1) {
                return states.get(0);
            }
        }
        return null;
    }

    List<State> findDistinctByZipCodeLocations_location(String location);

}
