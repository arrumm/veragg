package com.veragg.website.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.veragg.website.services.FileUrlHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.util.Objects.isNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Document implements Comparable<Document> {

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

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;

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

    public Document(DocumentType documentType, String storeName) {
        this.documentType = documentType;
        this.storeName = storeName;
    }

    public Document(String url, DocumentType documentType, Integer sortOrder) {
        this(url, documentType);
        this.sortOrder = sortOrder;
    }

    @Override
    public int compareTo(Document o) {
        if (o.equals(this)) {
            return 0;
        }
        if (o.getSortOrder().equals(this.getSortOrder())) {
            return o.getOriginalFileName().compareTo(this.getOriginalFileName());
        } else {
            return o.getSortOrder() - this.getSortOrder();
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Document document = (Document) o;

        if (getDocumentType() != document.getDocumentType()) {
            return false;
        }
        if (!getSortOrder().equals(document.getSortOrder())) {
            return false;
        }

        return getOriginalFileName().equals(document.getOriginalFileName());
    }

    @Override
    public int hashCode() {
        int result = getDocumentType().hashCode();
        result = 31 * result + getOriginalFileName().hashCode();
        result = 31 * result + getSortOrder().hashCode();
        return result;
    }
}
