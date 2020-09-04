package com.veragg.website.domain;

import javax.persistence.Column;
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
@Table(name = "state_zip_codes")
public class ZipCodeLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(name = "zip_code")
    private String zipCode;

    private String location;

    @Column(name = "location_addition")
    private String locationAddition;

    @Column(name = "location_with_addition")
    private String locationWithAddition;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

}
