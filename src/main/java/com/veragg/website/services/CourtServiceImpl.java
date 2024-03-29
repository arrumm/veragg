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

    private final CourtRepo courtRepo;
    private final StateRepo stateRepo;

    @Autowired
    public CourtServiceImpl(final CourtRepo courtRepo, final StateRepo stateRepo) {
        this.courtRepo = courtRepo;
        this.stateRepo = stateRepo;
    }

    @Override
    public Court findBy(@NonNull String name, @NonNull State state) {
        return courtRepo.findByNameAndState(name, state);
    }

    @Override
    public Court findBy(String courtLocation, String zipCode) {
        List<Court> courts = courtRepo.findAllByName(courtLocation);
        if (!courts.isEmpty()) {
            if (courts.size() == 1) {
                return courts.get(0);
            } else {
                State state = findState(zipCode, courtLocation);
                if (nonNull(state)) {
                    Court courtFound = courtRepo.findByNameAndState(courtLocation, state);
                    return nonNull(courtFound) ? courtFound : createCourt(courtLocation, findState(zipCode, courtLocation));
                } else {
                    return null;
                }
            }
        }

        return createCourt(courtLocation, findState(zipCode, courtLocation));
    }

    private State findState(String zipCode, String location) {
        State state = stateRepo.findByZipCodeLocations_zipCode(zipCode);
        if (isNull(state)) {
            State stateByFullLocation = stateRepo.findByFullLocation(location);
            if (isNull(stateByFullLocation)) {
                return stateRepo.findByLocation(location);
            }
            return stateByFullLocation;
        }
        return state;
    }

    private Court createCourt(@NonNull String name, @NonNull State state) {
        Court court = new Court(name, state);
        return courtRepo.save(court);
    }

}
