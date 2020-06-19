package com.veragg.website.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.veragg.website.domain.State;

public interface StateRepo extends CrudRepository<State, String> {

    State getById(String id);

    @Query(value = "from State s where :zip >= s.zipCodeRanges.from AND :zip <= s.zipCodeRanges.to")
    State findByZip(@Param("zip") String zip);

}
