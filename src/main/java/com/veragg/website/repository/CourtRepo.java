package com.veragg.website.repository;

import org.springframework.data.repository.CrudRepository;

import com.veragg.website.domain.Court;

public interface CourtRepo extends CrudRepository<Court, Long> {

    Court findByName(String name);

}
