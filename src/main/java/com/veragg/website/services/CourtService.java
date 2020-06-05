package com.veragg.website.services;

import com.veragg.website.domain.Court;
import com.veragg.website.domain.State;

public interface CourtService {

    Court get(String name, State state);

}
