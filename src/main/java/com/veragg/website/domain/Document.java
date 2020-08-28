package com.veragg.website.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.veragg.website.services.FileUrlHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.util.Objects.isNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Document<T extends BaseAuction> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String filePath;
    private String url;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private String originalFileName;
    private String storeName;

    private Integer sortOrder;

    private T owner;

    public Document(String url, DocumentType documentType) {
        this.url = url;
        String fileName = FileUrlHelper.getFileName(url);
        this.originalFileName = fileName;
        this.documentType = documentType;

        String fileExtension = FileUrlHelper.getExtension(fileName);
        this.fileType = FileType.getByExtension(fileExtension);

        if (isNull(storeName)) {
            this.storeName = UUID.randomUUID().toString();
        }
    }

    public Document(String url, DocumentType documentType, Integer sortOrder) {
        this(url, documentType);
        this.sortOrder = sortOrder;
    }

}
