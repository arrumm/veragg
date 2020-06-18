package com.veragg.website.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Getter
@Table(name = "states")
public class State {
    //    BW("Baden-Württemberg"),
    //    BE("Berlin"),
    //    BY("Bayern"),
    //    RP("Rheinland-Pfalz");

    @Id
    private String id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "state_zip_ranges", joinColumns = @JoinColumn(name = "state_id"))
    private Set<ZipCodeRange> zipCodeRanges = new HashSet<>();

}
