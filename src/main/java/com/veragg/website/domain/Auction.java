package com.veragg.website.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
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
    private String outdoorDescription;

    @Lob
    private String propertyBuildingDescription;

    @Lob
    private String propertyPlotDescription;

    @Lob
    private String expertiseDescription;

    @Column(name = "created_on")
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ElementCollection
    private List<String> imageLinks = new ArrayList<>();

    @ElementCollection
    private List<String> expertiseLinks = new ArrayList<>();

    @ElementCollection
    private Set<String> otherDocumentLinks = new HashSet<>();

    //    @OneToMany
    //    private List<Document> pictures = new ArrayList<>();
    //
    //    @OneToMany
    //    private List<Document> tilePictures = new ArrayList<>();
    //
    //    @OneToMany
    //    private List<Document> expertiseReports = new ArrayList<>();

    @Builder
    public Auction(@NonNull final Court court, final Set<AuctionDraft> drafts, @NonNull final String fileNumber, @NonNull final Set<PropertyType> propertyTypes, @NonNull final Address address,
            final LocalDateTime appointment, @NonNull final Integer amount, @NonNull final BuyLimit buyLimit, final String outdoorDescription, final String propertyBuildingDescription,
            final String propertyPlotDescription, final String expertiseDescription, final List<String> imageLinks, final List<String> expertiseLinks, final Set<String> otherDocumentLinks) {
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
        this.drafts = drafts;
        this.imageLinks = imageLinks;
        this.expertiseLinks = expertiseLinks;
        this.otherDocumentLinks = otherDocumentLinks;
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
