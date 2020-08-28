package com.veragg.website.services;

import com.veragg.website.domain.BaseAuction;
import com.veragg.website.domain.Court;
import com.veragg.website.domain.State;

import lombok.NonNull;

public interface CourtService<T extends BaseAuction> {

    Court<T> findBy(@NonNull String name, @NonNull State state);

    Court<T> findBy(@NonNull String name, @NonNull String zipCode);

}
