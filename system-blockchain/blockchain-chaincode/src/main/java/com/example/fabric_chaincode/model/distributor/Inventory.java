package com.example.fabric_chaincode.model.distributor;

import java.util.Map;

public class Inventory {

    private Long distributorId;

    private String distributorName;

    // supplies statistics
    // supplies id: number
    private Map<String, String> stock;

    public Inventory(Long distributorId, String distributorName, Map<String, String> stock) {
        this.distributorId = distributorId;
        this.distributorName = distributorName;
        this.stock = stock;
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

    public Map<String, String> getStock() {
        return stock;
    }

    public void setStock(Map<String, String> stock) {
        this.stock = stock;
    }
}
