package com.example.blockchain_gateway.domain;

public class BlockRecord {

    private Long blockNumber;

    private Integer txCount;

    private String dataHash;

    public BlockRecord(Long blockNumber, Integer txCount, String dataHash) {
        this.blockNumber = blockNumber;
        this.txCount = txCount;
        this.dataHash = dataHash;
    }

    public BlockRecord() { }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Integer getTxCount() {
        return txCount;
    }

    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }
}
