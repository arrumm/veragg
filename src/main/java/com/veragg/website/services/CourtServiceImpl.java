package com.veragg.website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Court;
import com.veragg.website.repository.CourtRepo;

import static java.util.Objects.nonNull;

@Service
public class CourtServiceImpl implements CourtService {

    private CourtRepo courtRepo;

    @Autowired
    public CourtServiceImpl(final CourtRepo courtRepo) {
        this.courtRepo = courtRepo;
    }

    @Override
    public Court getByName(final String name) {

        if (nonNull(courtRepo.findByName(name))) {
            return courtRepo.findByName(name);
        }

        Court court = new Court(name);
        return courtRepo.save(court);
    }
}
