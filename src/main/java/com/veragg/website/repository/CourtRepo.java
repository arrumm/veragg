package com.veragg.website.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.veragg.website.domain.Court;
import com.veragg.website.domain.State;

@Repository
public interface CourtRepo extends CrudRepository<Court, Long> {

    Court findByNameAndState(String name, State state);

    List<Court> findAllByName(String name);

}
