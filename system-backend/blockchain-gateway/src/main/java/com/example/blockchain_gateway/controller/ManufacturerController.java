package com.example.blockchain_gateway.controller;

import com.example.blockchain_gateway.domain.AjaxResult;
import com.example.blockchain_gateway.model.manufacturer.Process;
import com.example.blockchain_gateway.model.order.Order;
import com.example.blockchain_gateway.model.order.OrderStatus;
import com.example.blockchain_gateway.service.ManufacturerService;
import com.example.blockchain_gateway.service.OrderService;
import com.example.blockchain_gateway.service.RegulatorService;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.gateway.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/blockchain/manufacturer")
@RestController
public class ManufacturerController {

    @Resource(name = "manufacturerContract")
    private Contract contract;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private RegulatorService regulatorService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/getAllProcesses")
    public AjaxResult getAllProcesses() {
        try {
            List<Process> processes = manufacturerService.getAllProcesses();
            return AjaxResult.success("Get all processes successfully", processes);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/recordProcess")
    public AjaxResult recordProcess(@RequestBody Process process) {
        System.out.println("Record: " + process.toString());
        try {
            if (StringUtils.isEmpty(process.getProcessId())
                    && !StringUtils.isEmpty(process.getOrderId())
                    && process.getDistributorId() != null
                    && process.getManufacturerId() != null
                    && !StringUtils.isEmpty(process.getManufacturerName())
                    && !StringUtils.isEmpty(process.getStatus())
                    && !StringUtils.isEmpty(process.getRemarks())) {
                Order order = orderService.getOrderById(process.getOrderId(), contract);
                if (order != null && order.getLicenseId() != null
                        && order.getStatus().equals(OrderStatus.CONFIRMED)) {
                    Process newProcess = manufacturerService.recordProcess(process);
                    if (newProcess != null) {
                        return AjaxResult.success("Record process successfully", newProcess);
                    }
                }
            }
            return AjaxResult.error("Failed to record the process, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/updateProcess")
    public AjaxResult updateProcess(@RequestBody Process process) {
        System.out.println("Update: " + process.toString());
        try {
            if (!StringUtils.isEmpty(process.getProcessId())
                    && manufacturerService.getProcessById(process.getProcessId()) != null
                    && !StringUtils.isEmpty(process.getStatus())
                    && !StringUtils.isEmpty(process.getRemarks())) {
                Process newProcess = manufacturerService.updateProcess(process);
                if (newProcess != null) {
                    return AjaxResult.success("Update process successfully", newProcess);
                }
            }
            return AjaxResult.error("Failed to update the process, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/confirmOrder")
    public AjaxResult confirmOrder(@RequestBody Order order) {
        System.out.println("Confirm Order: " + order.toString());
        try {
            if (!StringUtils.isEmpty(order.getOrderId())
                    && orderService.getOrderById(order.getOrderId(), contract) != null
                    && !StringUtils.isEmpty(order.getStatus())
                    && !StringUtils.isEmpty(order.getLicenseId())
                    && order.getStatus().equals(OrderStatus.REQUESTED)
                    && regulatorService.getLicenseById(order.getLicenseId()) != null) {
                    Order newOrder = manufacturerService.confirmOrder(order);
                if (newOrder != null) {
                    return AjaxResult.success("Confirm the order successfully", newOrder);
                }
            }
            return AjaxResult.error("Failed to confirm the order, please contact the distributor");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/transferProducts")
    public AjaxResult transferProducts(@RequestBody Order order) {
        System.out.println("Transfer Products: " + order.toString());
        try {
            if (!StringUtils.isEmpty(order.getOrderId())
                    && orderService.getOrderById(order.getOrderId(), contract) != null
                    && !StringUtils.isEmpty(order.getStatus())
                    && order.getStatus().equals(OrderStatus.CONFIRMED)
                    && !StringUtils.isEmpty(order.getTransferDate())
                    && !StringUtils.isEmpty(order.getCarrier())
                    && !StringUtils.isEmpty(order.getContact())) {
                Order newOrder = manufacturerService.transferProducts(order);
                if (newOrder != null) {
                    return AjaxResult.success("Transfer products successfully", newOrder);
                }
            }
            return AjaxResult.error("Failed to transfer products, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
