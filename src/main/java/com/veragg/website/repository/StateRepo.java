package com.veragg.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veragg.website.domain.State;

@Repository
public interface StateRepo extends JpaRepository<State, String> {

    @Query(value = "SELECT s from State s " +//
            "INNER JOIN s.zipCodeRanges zr " +//
            "WHERE ((:zip >= zr.start AND :zip <= zr.end) OR (:zip = zr.start AND zr.end IS NULL)) ORDER BY s.id")
    State findByZip(@Param("zip") String zip);

}
