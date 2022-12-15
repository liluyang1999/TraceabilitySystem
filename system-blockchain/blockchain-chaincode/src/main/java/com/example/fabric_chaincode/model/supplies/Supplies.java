package com.example.fabric_chaincode.model.supplies;

import com.example.fabric_chaincode.model.TxRecord;

public class Supplies {

    private String id;

    private String name;

    private String isOtc;

    private String remarks;

    private TxRecord txRecord;

    public Supplies(String id, String name, String isOtc, String remarks, TxRecord txRecord) {
        this.id = id;
        this.name = name;
        this.isOtc = isOtc;
        this.remarks = remarks;
        this.txRecord = txRecord;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsOtc() {
        return isOtc;
    }

    public void setIsOtc(String isOtc) {
        this.isOtc = isOtc;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public TxRecord getTxRecord() {
        return txRecord;
    }

    public void setTxRecord(TxRecord txRecord) {
        this.txRecord = txRecord;
    }
}
