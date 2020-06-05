package com.veragg.website.domain;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Address {

    private String street;
    private String number;
    private String zipCode;
    private String city;
    private String flat;

    public Address(final String street, final String no, final String zipCode, final String city) {
        this.street = street;
        this.number = no;
        this.zipCode = zipCode;
        this.city = city;
    }

}
