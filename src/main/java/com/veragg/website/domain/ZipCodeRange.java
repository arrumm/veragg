package com.veragg.website.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
public class ZipCodeRange {
    @Id
    String start;

    @NonNull String end;

}
