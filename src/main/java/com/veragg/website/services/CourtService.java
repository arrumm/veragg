package com.veragg.website.services;

import com.veragg.website.domain.Court;

public interface CourtService {

    Court getByName(String name);

}
