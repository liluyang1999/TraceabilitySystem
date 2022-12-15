package com.example.blockchain_gateway.model.regulator;

import com.example.blockchain_gateway.domain.TxRecord;

public class License {

    private String licenseId;

    private Long regulatorId;

    private String regulatorName;

    private Long manufacturerId;

    private String manufacturerName;

    private String suppliesId;

    private String suppliesName;

    private String issueDate;

    private String expireDate;

    private TxRecord txRecord;

    public License(String licenseId, Long regulatorId, String regulatorName, Long manufacturerId, String manufacturerName, String suppliesId, String suppliesName, String issueDate, String expireDate, TxRecord txRecord) {
        this.licenseId = licenseId;
        this.regulatorId = regulatorId;
        this.regulatorName = regulatorName;
        this.manufacturerId = manufacturerId;
        this.manufacturerName = manufacturerName;
        this.suppliesId = suppliesId;
        this.suppliesName = suppliesName;
        this.issueDate = issueDate;
        this.expireDate = expireDate;
        this.txRecord = txRecord;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public Long getRegulatorId() {
        return regulatorId;
    }

    public void setRegulatorId(Long regulatorId) {
        this.regulatorId = regulatorId;
    }

    public String getRegulatorName() {
        return regulatorName;
    }

    public void setRegulatorName(String regulatorName) {
        this.regulatorName = regulatorName;
    }

    public Long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getSuppliesId() {
        return suppliesId;
    }

    public void setSuppliesId(String suppliesId) {
        this.suppliesId = suppliesId;
    }

    public String getSuppliesName() {
        return suppliesName;
    }

    public void setSuppliesName(String suppliesName) {
        this.suppliesName = suppliesName;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public TxRecord getTxRecord() {
        return txRecord;
    }

    public void setTxRecord(TxRecord txRecord) {
        this.txRecord = txRecord;
    }

    @Override
    public String toString() {
        return "License{" +
                "licenseId='" + licenseId + '\'' +
                ", regulatorId=" + regulatorId +
                ", regulatorName='" + regulatorName + '\'' +
                ", manufacturerId=" + manufacturerId +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", suppliesId='" + suppliesId + '\'' +
                ", suppliesName='" + suppliesName + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", txRecord=" + txRecord +
                '}';
    }
}
