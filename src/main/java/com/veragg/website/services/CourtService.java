package com.veragg.website.services;

import com.veragg.website.domain.Court;
import com.veragg.website.domain.State;

public interface CourtService {

    Court findBy(String name, State state);

    Court findBy(String name, String zipCode);

}
