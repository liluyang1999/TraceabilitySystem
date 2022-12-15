package com.example.blockchain_gateway.model.provider;

import com.example.blockchain_gateway.domain.TxRecord;
import com.example.blockchain_gateway.model.supplies.SuppliesUnit;

import java.util.List;

public class Sale {

    private String saleId;

    private String username;

    private SuppliesUnit suppliesUnit;

    private Long providerId;

    private String providerName;

    private String purchaseDate;

    private String distributionId;

    private String orderId;

    private List<TxRecord> txRecords;

    public Sale(String saleId, String username, SuppliesUnit suppliesUnit, Long providerId, String providerName, String purchaseDate, String distributionId, String orderId, List<TxRecord> txRecords) {
        this.saleId = saleId;
        this.username = username;
        this.suppliesUnit = suppliesUnit;
        this.providerId = providerId;
        this.providerName = providerName;
        this.purchaseDate = purchaseDate;
        this.distributionId = distributionId;
        this.orderId = orderId;
        this.txRecords = txRecords;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SuppliesUnit getSuppliesUnit() {
        return suppliesUnit;
    }

    public void setSuppliesUnit(SuppliesUnit suppliesUnit) {
        this.suppliesUnit = suppliesUnit;
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

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public List<TxRecord> getTxRecords() {
        return txRecords;
    }

    public void setTxRecords(List<TxRecord> txRecords) {
        this.txRecords = txRecords;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleId='" + saleId + '\'' +
                ", username='" + username + '\'' +
                ", suppliesUnit=" + suppliesUnit +
                ", providerId=" + providerId +
                ", providerName='" + providerName + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", distributionId='" + distributionId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", txRecords=" + txRecords +
                '}';
    }
}
