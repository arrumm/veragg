package com.veragg.website.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "auctions")
public class Auction {

    //TODO: create a special id for requesting the resource?

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "court_id")
    private Court court;

    //aktenzeichen
    @NonNull
    private String fileNumber;

    //objekttyp
    @NonNull
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<PropertyType> propertyTypes = new HashSet<>();

    @Embedded
    @NonNull
    private Address address;

    //termin
    @NonNull
    private LocalDateTime appointment;

    //Verkehrswert
    @NonNull
    private Integer amount;

    //zuschlag
    @Enumerated(EnumType.STRING)
    @NonNull
    private BuyLimit buyLimit;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String outdoorDescription;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String propertyBuildingDescription;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String propertyPlotDescription;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String expertiseDescription;

    @Column(name = "created_on")
    @CreationTimestamp
    private Instant createdOn;

    @Column(name = "updated_on")
    @UpdateTimestamp
    private Instant updatedOn;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private AuctionSource source;

    @Column(name = "source_url")
    private String sourceUrl;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Document> documents = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @NonNull
    private AuctionStatus auctionStatus;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Document> tilePictures = new ArrayList<>();

    //TODO: additional info like in https://www.hanmark.de/wertgutachten-29300.html

    @Builder
    public Auction(@NonNull Court court, @NonNull String fileNumber, @NonNull Set<PropertyType> propertyTypes, @NonNull Address address, LocalDateTime appointment, @NonNull Integer amount,
            @NonNull BuyLimit buyLimit, String outdoorDescription, String propertyBuildingDescription, String propertyPlotDescription, String expertiseDescription, String sourceUrl,
            AuctionSource source, List<Document> documents, @NonNull AuctionStatus auctionStatus) {
        this.court = court;
        this.fileNumber = fileNumber;
        this.propertyTypes = propertyTypes;
        this.address = address;
        this.appointment = appointment;
        this.amount = amount;
        this.buyLimit = buyLimit;
        this.outdoorDescription = outdoorDescription;
        this.propertyBuildingDescription = propertyBuildingDescription;
        this.propertyPlotDescription = propertyPlotDescription;
        this.expertiseDescription = expertiseDescription;
        this.sourceUrl = sourceUrl;
        this.source = source;
        this.documents = documents;
        this.auctionStatus = auctionStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Auction)) {
            return false;
        }

        final Auction auction = (Auction) o;

        if (getId() != null ? !getId().equals(auction.getId()) : auction.getId() != null) {
            return false;
        }
        return getFileNumber() != null ? getFileNumber().equals(auction.getFileNumber()) : auction.getFileNumber() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getFileNumber() != null ? getFileNumber().hashCode() : 0);
        return result;
    }
}
