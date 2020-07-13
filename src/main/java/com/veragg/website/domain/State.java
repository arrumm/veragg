package com.veragg.website.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Getter
@Table(name = "states")
public class State {

    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<ZipCodeLocation> zipCodeLocations = new HashSet<>();

}
