package com.veragg.website.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    public Long getId() {
        return id;
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

    public Court getCourt() {
        return court;
    }

    public void setCourt(final Court court) {
        this.court = court;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(final String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public LocalDateTime getAppointment() {
        return appointment;
    }

    public void setAppointment(final LocalDateTime appointment) {
        this.appointment = appointment;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(final Long amount) {
        this.amount = amount;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(final Limit limit) {
        this.limit = limit;
    }

    public String getLimitInfo() {
        return limitInfo;
    }

    public void setLimitInfo(final String limitInfo) {
        this.limitInfo = limitInfo;
    }

    public String getInfoPlace() {
        return infoPlace;
    }

    public void setInfoPlace(final String infoPlace) {
        this.infoPlace = infoPlace;
    }

    public String getInfoPropertyBuilding() {
        return infoPropertyBuilding;
    }

    public void setInfoPropertyBuilding(final String infoPropertyBuilding) {
        this.infoPropertyBuilding = infoPropertyBuilding;
    }

    public String getInfoPropertyLand() {
        return infoPropertyLand;
    }

    public void setInfoPropertyLand(final String infoPropertyLand) {
        this.infoPropertyLand = infoPropertyLand;
    }

    public String getInfoEvaluation() {
        return infoEvaluation;
    }

    public void setInfoEvaluation(final String infoEvaluation) {
        this.infoEvaluation = infoEvaluation;
    }
}
