package com.veragg.website.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//TODO: AuctionConflict just extends Auction and has additional field date, probably column name(s) for conflict

@Entity
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String referenceNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "court_id")
    private Court court;

    //aktenzeichen
    private String fileNumber;

    //objekttyp
    private PropertyType propertyType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    //termin
    private LocalDateTime appointment;

    //Verkehrswert
    private Long amount;

    //zuschlag
    private Limit limit;
    //Zuschlag ab: 	keine Grenze
    private String limitInfo;

    private String infoPlace;
    private String infoPropertyBuilding;
    private String infoPropertyLand;
    private String infoEvaluation;

//    private List<Document> pictures;

    public Auction() {
    }

    public Long getId() {
        return id;
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
        return getReferenceNumber() != null ? getReferenceNumber().equals(auction.getReferenceNumber()) : auction.getReferenceNumber() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getReferenceNumber() != null ? getReferenceNumber().hashCode() : 0);
        return result;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}
