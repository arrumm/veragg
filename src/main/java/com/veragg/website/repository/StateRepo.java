package com.veragg.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veragg.website.domain.State;

@Repository
public interface StateRepo extends JpaRepository<State, String> {

    State findByZipCodeLocations_zipCode(String zip);

    //todo: add prio by location = 1, and location with addition = 0 and sort by it, take first one

    @Query(value = "SELECT s from State s " +//
            "INNER JOIN s.zipCodeLocations zr " +//
            "WHERE (:location = zr.location OR :location = CONCAT(zr.location, zr.locationAddition)) GROUP BY s")
    State findByZipCodeLocations_location(@Param("location") String location);

}
