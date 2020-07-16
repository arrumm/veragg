package com.veragg.website.services;

import com.veragg.website.domain.Court;
import com.veragg.website.domain.State;

import lombok.NonNull;

public interface CourtService {

    Court findBy(@NonNull String name, @NonNull State state);

    Court findBy(@NonNull String name, @NonNull String zipCode);

}
