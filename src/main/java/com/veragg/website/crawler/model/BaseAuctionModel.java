package com.veragg.website.crawler.model;

import java.util.List;

public class BaseAuctionModel {
    private String fileNumber;
    private String courtName;
    private String propertyTypeName;
    private String streetAddress;
    private String cityAddress;
    private String amount;
    private String appointmentDate;
    private String expertDescription;
    private String plotDescription;
    private String buildingDescription;
    private String outdoorDescription;
    private List<String> tileImages;
    private String courtAppointmentDescriptionUrl;

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(final String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(final String courtName) {
        this.courtName = courtName;
    }

    public String getPropertyTypeName() {
        return propertyTypeName;
    }

    public void setPropertyTypeName(final String propertyTypeName) {
        this.propertyTypeName = propertyTypeName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(final String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCityAddress() {
        return cityAddress;
    }

    public void setCityAddress(final String cityAddress) {
        this.cityAddress = cityAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(final String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getExpertDescription() {
        return expertDescription;
    }

    public void setExpertDescription(final String expertDescription) {
        this.expertDescription = expertDescription;
    }

    public String getPlotDescription() {
        return plotDescription;
    }

    public void setPlotDescription(final String plotDescription) {
        this.plotDescription = plotDescription;
    }

    public String getBuildingDescription() {
        return buildingDescription;
    }

    public void setBuildingDescription(final String buildingDescription) {
        this.buildingDescription = buildingDescription;
    }

    public String getOutdoorDescription() {
        return outdoorDescription;
    }

    public void setOutdoorDescription(final String outdoorDescription) {
        this.outdoorDescription = outdoorDescription;
    }

    public List<String> getTileImages() {
        return tileImages;
    }

    public void setTileImages(final List<String> tileImages) {
        this.tileImages = tileImages;
    }

    public String getCourtAppointmentDescriptionUrl() {
        return courtAppointmentDescriptionUrl;
    }

    public void setCourtAppointmentDescriptionUrl(final String courtAppointmentDescriptionUrl) {
        this.courtAppointmentDescriptionUrl = courtAppointmentDescriptionUrl;
    }
}
