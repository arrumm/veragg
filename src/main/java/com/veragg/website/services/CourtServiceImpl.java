package com.veragg.website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Court;
import com.veragg.website.domain.State;
import com.veragg.website.repository.CourtRepo;

@Service
public class CourtServiceImpl implements CourtService {

    private CourtRepo courtRepo;

    @Autowired
    public CourtServiceImpl(final CourtRepo courtRepo) {
        this.courtRepo = courtRepo;
    }

    @Override
    public Court get(final String name, final State state) {
        return courtRepo.findByNameAndState(name, state);
    }
}
