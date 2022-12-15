package com.example.fabric_chaincode.model.consumer;

import com.example.fabric_chaincode.model.distributor.Distribution;
import com.example.fabric_chaincode.model.order.Order;
import com.example.fabric_chaincode.model.provider.Sale;
import com.example.fabric_chaincode.model.regulator.License;
import com.example.fabric_chaincode.model.supplies.Supplies;

import java.util.List;

public class TraceTask {

    private String traceId;

    private String username;

    private Sale sale;

    /******Queried from blockchain when tracing******/
    private Supplies supplies;

    private Order order;

    private License license;

    private List<Process> processes;

    private Distribution distribution;
    /************************************************************/

    public TraceTask(String username, Sale sale) {
        this.username = username;
        this.sale = sale;
    }

    public TraceTask(String traceId, String username, Sale sale, Supplies supplies, Order order, License license, List<Process> processes, Distribution distribution) {
        this.traceId = traceId;
        this.username = username;
        this.sale = sale;
        this.supplies = supplies;
        this.order = order;
        this.license = license;
        this.processes = processes;
        this.distribution = distribution;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Supplies getSupplies() {
        return supplies;
    }

    public void setSupplies(Supplies supplies) {
        this.supplies = supplies;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }
}