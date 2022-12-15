package com.example.blockchain_gateway.model.manufacturer;

import com.example.blockchain_gateway.domain.TxRecord;

import java.util.List;

public class Process {

    private String processId;

    private Long distributorId;

    private String orderId;

    private Long manufacturerId;

    private String manufacturerName;

    private String recordDate;

    private String status;

    private String remarks;

    private List<TxRecord> txRecords;

    public Process(String processId, Long distributorId, String orderId, Long manufacturerId, String manufacturerName, String recordDate, String status, String remarks, List<TxRecord> txRecords) {
        this.processId = processId;
        this.distributorId = distributorId;
        this.orderId = orderId;
        this.manufacturerId = manufacturerId;
        this.manufacturerName = manufacturerName;
        this.recordDate = recordDate;
        this.status = status;
        this.remarks = remarks;
        this.txRecords = txRecords;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Long getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<TxRecord> getTxRecords() {
        return txRecords;
    }

    public void setTxRecords(List<TxRecord> txRecords) {
        this.txRecords = txRecords;
    }

    @Override
    public String toString() {
        return "Process{" +
                "processId='" + processId + '\'' +
                ", distributorId=" + distributorId +
                ", orderId='" + orderId + '\'' +
                ", manufacturerId=" + manufacturerId +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", recordDate='" + recordDate + '\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                ", txRecords=" + txRecords +
                '}';
    }
}
