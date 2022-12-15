package com.example.fabric_chaincode.model.order;

import com.example.fabric_chaincode.model.TxRecord;
import com.example.fabric_chaincode.model.supplies.SuppliesUnit;

import java.util.List;

public class Order {

    private String orderId;

    private String requestDate;

    private Long distributorId;

    private String distributorName;

    private Long manufacturerId;

    private String manufacturerName;

    private SuppliesUnit suppliesUnit;

    private String status;

    private String licenseId;

    private String transferDate;

    private String carrier;

    private String contact;

    private String receiveDate;

    private List<TxRecord> txRecords;

    public Order(String orderId, String requestDate, Long distributorId, String distributorName, Long manufacturerId, String manufacturerName, SuppliesUnit suppliesUnit, String status, String licenseId, String transferDate, String carrier, String contact, String receiveDate, List<TxRecord> txRecords) {
        this.orderId = orderId;
        this.requestDate = requestDate;
        this.distributorId = distributorId;
        this.distributorName = distributorName;
        this.manufacturerId = manufacturerId;
        this.manufacturerName = manufacturerName;
        this.suppliesUnit = suppliesUnit;
        this.status = status;
        this.licenseId = licenseId;
        this.transferDate = transferDate;
        this.carrier = carrier;
        this.contact = contact;
        this.receiveDate = receiveDate;
        this.txRecords = txRecords;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public Long getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
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

    public SuppliesUnit getSuppliesUnit() {
        return suppliesUnit;
    }

    public void setSuppliesUnit(SuppliesUnit suppliesUnit) {
        this.suppliesUnit = suppliesUnit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public List<TxRecord> getTxRecords() {
        return txRecords;
    }

    public void setTxRecords(List<TxRecord> txRecords) {
        this.txRecords = txRecords;
    }
}