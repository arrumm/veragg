package com.veragg.website.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue(generator = "hibernate-uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String path;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    private String name;

    private Integer order;

}
