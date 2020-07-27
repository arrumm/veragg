package com.veragg.website.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.util.Objects.isNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Document {

    //TODO: should be base entity without table

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String path;
    private String url;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private String originalName;
    private String storeName;

    private Integer sortOrder;

    public Document(String url, DocumentType documentType) {

        //TODO: extract original name
        this.originalName = String.valueOf(url.charAt(0));
        //TODO: extract type
        this.documentType = documentType;

        this.fileType = FileType.valueOf(String.valueOf(url.charAt(0)));

        if (isNull(storeName)) {
            this.storeName = UUID.randomUUID().toString();
        }

    }

}
