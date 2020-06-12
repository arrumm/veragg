package com.veragg.website.domain;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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

    @ElementCollection
    @LazyCollection(value = LazyCollectionOption.TRUE)
    @CollectionTable(name = "state_zip_ranges", joinColumns = @JoinColumn(name = "state_id"))
    private Set<ZipCodeRange> zipCodeRanges;

    @Embeddable
    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor
    @RequiredArgsConstructor
    private static class ZipCodeRange {
        @Id
        String from;
        @NonNull
        String to;
    }



}
