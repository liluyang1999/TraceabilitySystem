package com.example.fabric_chaincode.model.distributor;

import com.example.fabric_chaincode.model.TxRecord;
import com.example.fabric_chaincode.model.supplies.SuppliesUnit;

import java.util.List;

public class Distribution {

    private String distributionId;

    private String orderId;

    private Long distributorId;

    private String distributorName;

    private Long providerId;

    private String providerName;

    private SuppliesUnit suppliesUnit;

    private String status;

    private String deliverDate;

    private String driver;

    private String contact;

    private String receiveDate;

    private List<TxRecord> txRecords;

    public Distribution(String distributionId, String orderId, Long distributorId, String distributorName, Long providerId, String providerName, SuppliesUnit suppliesUnit, String status, String deliverDate, String driver, String contact, String receiveDate, List<TxRecord> txRecords) {
        this.distributionId = distributionId;
        this.orderId = orderId;
        this.distributorId = distributorId;
        this.distributorName = distributorName;
        this.providerId = providerId;
        this.providerName = providerName;
        this.suppliesUnit = suppliesUnit;
        this.status = status;
        this.deliverDate = deliverDate;
        this.driver = driver;
        this.contact = contact;
        this.receiveDate = receiveDate;
        this.txRecords = txRecords;
    }

    public String getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(String distributionId) {
        this.distributionId = distributionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
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
