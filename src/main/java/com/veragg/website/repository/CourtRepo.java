package com.veragg.website.repository;

import org.springframework.data.repository.CrudRepository;

import com.veragg.website.domain.Court;
import com.veragg.website.domain.State;

public interface CourtRepo extends CrudRepository<Court, Long> {

    Court findByNameAndState(String name, State state);

}
