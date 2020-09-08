package com.veragg.website.domain;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class Address {

    private String street;
    //todo: rename to buildingNumber
    private String number;
    private String zipCode;
    private String city;
    //    todo: flatNumber
    private String flat;

}
