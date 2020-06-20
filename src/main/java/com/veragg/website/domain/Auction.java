package com.veragg.website.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "court_id")
    private Court court;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "auction_drafts", joinColumns = @JoinColumn(name = "auction_id"), inverseJoinColumns = @JoinColumn(name = "auction_draft_id"))
    private Set<AuctionDraft> drafts = new HashSet<>();

    //aktenzeichen
    @NonNull
    private String fileNumber;

    //objekttyp
    @NonNull
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Embedded
    @NonNull
    private Address address;

    //termin
    @NonNull
    private Date appointment;
    //Verkehrswert

    @NonNull
    private Integer amount;
    //zuschlag

    @Enumerated(EnumType.STRING)
    @NonNull
    private Limit limit;

    private String outdoorDescription;
    private String propertyBuildingDescription;
    private String propertyPlotDescription;
    private String expertiseDescription;

    @OneToMany
    private List<Document> pictures = new ArrayList<>();

    @OneToMany
    private List<Document> tilePictures = new ArrayList<>();

    @OneToMany
    private List<Document> expertiseReports = new ArrayList<>();

    @Builder
    public Auction(@NonNull final Court court, final Set<AuctionDraft> drafts, @NonNull final String fileNumber, @NonNull final PropertyType propertyType, @NonNull final Address address,
            @NonNull final Date appointment, @NonNull final Integer amount, @NonNull final Limit limit, final String outdoorDescription, final String propertyBuildingDescription,
            final String propertyPlotDescription, final String expertiseDescription, final List<Document> pictures, final List<Document> tilePictures, final List<Document> expertiseReports) {
        this.court = court;
        this.drafts = drafts;
        this.fileNumber = fileNumber;
        this.propertyType = propertyType;
        this.address = address;
        this.appointment = appointment;
        this.amount = amount;
        this.limit = limit;
        this.outdoorDescription = outdoorDescription;
        this.propertyBuildingDescription = propertyBuildingDescription;
        this.propertyPlotDescription = propertyPlotDescription;
        this.expertiseDescription = expertiseDescription;
        this.pictures = pictures;
        this.tilePictures = tilePictures;
        this.expertiseReports = expertiseReports;
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
