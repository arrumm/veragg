package com.veragg.website.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veragg.website.domain.Court;
import com.veragg.website.domain.State;
import com.veragg.website.repository.CourtRepo;
import com.veragg.website.repository.StateRepo;

import lombok.NonNull;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class CourtServiceImpl implements CourtService {

    private CourtRepo courtRepo;
    private StateRepo stateRepo;

    @Autowired
    public CourtServiceImpl(final CourtRepo courtRepo, final StateRepo stateRepo) {
        this.courtRepo = courtRepo;
        this.stateRepo = stateRepo;
    }

    @Override
    public Court findBy(final String name, final State state) {
        return courtRepo.findByNameAndState(name, state);
    }

    @Override
    public Court findBy(String courtLocation, String zipCode) {
        List<Court> courts = courtRepo.findAllByName(courtLocation);
        if (!courts.isEmpty()) {
            if (courts.size() > 1) {
                State state = findState(zipCode, courtLocation);
                if (nonNull(state)) {
                    Court courtFound = courtRepo.findByNameAndState(courtLocation, state);
                    if (nonNull(courtFound)) {
                        return courtFound;
                    }
                } else {
                    return null;
                }
            } else {
                return courts.get(0);
            }
        }

        return createCourt(courtLocation, findState(zipCode, courtLocation));
    }

    private State findState(String zipCode, String location) {
        State state = stateRepo.findByZipCodeLocations_zipCode(zipCode);
        if (isNull(state)) {
            try {
                //TODO: court location handle, e.g. Sankt -> St., im -> i., Kreis -> Kr, Gemeinde -> Gem
                return stateRepo.findByZipCodeLocations_location(location);
            } catch (Exception e) {
                return null;
            }
        }
        return state;
    }

    private Court createCourt(@NonNull String name, @NonNull State state) {
        Court court = new Court(name, state);
        return courtRepo.save(court);
    }

}
