package com.example.blockchain_gateway.model.supplies;

public class SuppliesUnit {

    private Supplies supplies;

    private String quantity;

    public SuppliesUnit(Supplies supplies, String quantity) {
        this.supplies = supplies;
        this.quantity = quantity;
    }

    public Supplies getSupplies() {
        return supplies;
    }

    public void setSupplies(Supplies supplies) {
        this.supplies = supplies;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
