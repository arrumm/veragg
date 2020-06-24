package com.veragg.website.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "state_zip_ranges")
public class ZipCodeRange {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String start;
    private String end;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

}
