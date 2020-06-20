package com.veragg.website.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.veragg.website.domain.State;

public interface StateRepo extends CrudRepository<State, String> {

    @Query(value = "SELECT s from State s JOIN state_zip_ranges zr ON s.id = zr.state_id WHERE :zip >= zr.start AND :zip <= zr.end")
    State findByZip(@Param("zip") String zip);

}
