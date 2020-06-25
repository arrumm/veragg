package com.veragg.website.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.veragg.website.domain.State;

public interface StateRepo extends CrudRepository<State, String> {

    @Query(value = "SELECT s from State s " +//
            "INNER JOIN s.zipCodeRanges zr " +//
            "WHERE ((:zip >= zr.start AND :zip <= zr.end) OR (:zip = zr.start AND zr.end IS NULL)) " + "ORDER BY s.id")
    State findByZip(@Param("zip") String zip);

}
