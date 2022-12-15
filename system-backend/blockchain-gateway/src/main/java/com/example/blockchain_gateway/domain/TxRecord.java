package com.example.blockchain_gateway.domain;

public class TxRecord {

    private String txId;

    private String txTimestamp;

    public TxRecord(String txId, String txTimestamp) {
        this.txId = txId;
        this.txTimestamp = txTimestamp;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getTxTimestamp() {
        return txTimestamp;
    }

    public void setTxTimestamp(String txTimestamp) {
        this.txTimestamp = txTimestamp;
    }
}
