package com.veragg.website.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
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
@MappedSuperclass
public class BaseAuction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    //TODO: refactor to normal date/time
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

    @ManyToOne
    @JoinColumn(name = "source_id")
    private AuctionSource source;

    @Column(name = "source_url")
    private String sourceUrl;

    @OneToMany
    private List<Document<? extends BaseAuction>> documents = new ArrayList<>();

    //    @OneToMany
    //    private List<Document> tilePictures = new ArrayList<>();

    @Builder(builderMethodName = "baseAuctionBuilder")
    public BaseAuction(@NonNull Court<?> court, @NonNull String fileNumber, @NonNull Set<PropertyType> propertyTypes, @NonNull Address address, LocalDateTime appointment, @NonNull Integer amount,
            @NonNull BuyLimit buyLimit, String outdoorDescription, String propertyBuildingDescription, String propertyPlotDescription, String expertiseDescription, String sourceUrl,
            AuctionSource source, List<Document<?>> documents) {
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
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseAuction)) {
            return false;
        }

        final BaseAuction auction = (BaseAuction) o;

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
