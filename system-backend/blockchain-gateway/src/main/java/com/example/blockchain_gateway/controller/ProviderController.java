package com.example.blockchain_gateway.controller;

import com.example.blockchain_gateway.domain.AjaxResult;
import com.example.blockchain_gateway.model.distributor.Distribution;
import com.example.blockchain_gateway.model.distributor.DistributionStatus;
import com.example.blockchain_gateway.model.order.Order;
import com.example.blockchain_gateway.model.provider.Sale;
import com.example.blockchain_gateway.model.supplies.Supplies;
import com.example.blockchain_gateway.service.*;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.gateway.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/blockchain/provider")
@RestController
public class ProviderController {

    @Resource(name = "providerContract")
    private Contract contract;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private DistributorService distributorService;

    @Autowired
    private RegulatorService regulatorService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestService restService;

    @RequestMapping("/getAllSales")
    public AjaxResult getAllSales() {
        try {
            List<Sale> sales = providerService.getAllSales();
            return AjaxResult.success("Get all sales successfully", sales);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/receiveProducts")
    public AjaxResult receiveProducts(@RequestBody Distribution distribution) {
        System.out.println("Receive Products: " + distribution.toString());
        try {
            if (!StringUtils.isEmpty(distribution.getDistributionId())
                    && !StringUtils.isEmpty(distribution.getOrderId())
                    && orderService.getOrderById(distribution.getOrderId(), contract) != null
                    && distribution.getDistributorId() != null
                    && !StringUtils.isEmpty(distribution.getDistributorName())
                    && distribution.getProviderId() != null
                    && !StringUtils.isEmpty(distribution.getProviderName())
                    && distribution.getSuppliesUnit() != null
                    && distribution.getSuppliesUnit().getSupplies() != null
                    && !StringUtils.isEmpty(distribution.getSuppliesUnit().getQuantity())
                    && !StringUtils.isEmpty(distribution.getStatus())
                    && distribution.getStatus().equals(DistributionStatus.DELIVERED)
                    && !StringUtils.isEmpty(distribution.getDeliverDate())
                    && !StringUtils.isEmpty(distribution.getDriver())
                    && !StringUtils.isEmpty(distribution.getContact())) {
                Distribution newDistribution = providerService.receiveProducts(distribution);
                if (newDistribution != null) {
                    return AjaxResult.success("Receives products successfully", newDistribution);
                }
            }
            return AjaxResult.error("Failed to receive products, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/sellProducts")
    public AjaxResult sellProducts(@RequestBody Sale sale) {
        System.out.println("Sell Products: " + sale.toString());
        try {
            if (StringUtils.isEmpty(sale.getSaleId())
                    && !StringUtils.isEmpty(sale.getUsername())
                    && sale.getSuppliesUnit() != null
                    && sale.getSuppliesUnit().getSupplies() != null
                    && !StringUtils.isEmpty(sale.getSuppliesUnit().getQuantity())
                    && sale.getProviderId() != null
                    && !StringUtils.isEmpty(sale.getProviderName())
                    && !StringUtils.isEmpty(sale.getPurchaseDate())
                    && !StringUtils.isEmpty(sale.getDistributionId())
                    && !StringUtils.isEmpty(sale.getOrderId())
                    && restService.isUserExist(sale.getUsername())) {
                Supplies supplies = regulatorService.getSuppliesById(sale.getSuppliesUnit().getSupplies().getId());
                Order order = orderService.getOrderById(sale.getOrderId(), contract);
                Distribution distribution = distributorService.getDistributionById(sale.getDistributionId());

                if (supplies != null && order != null && distribution != null
                        && distribution.getStatus().equals(DistributionStatus.RECEIVED)
                        && distribution.getSuppliesUnit().getSupplies().getId().equals(sale.getSuppliesUnit().getSupplies().getId())
                        && Long.parseLong(distribution.getSuppliesUnit().getQuantity()) >= Long.parseLong(sale.getSuppliesUnit().getQuantity())) {
                    Sale newSale = providerService.sellProducts(sale);
                    if (newSale != null) {
                        return AjaxResult.success("Sell products successfully", newSale);
                    }
                }
            }
            return AjaxResult.error("Failed to sell products, distribution and sale do not match");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/removeSale")
    public AjaxResult removeSale(@RequestParam String saleId) {
        try {
            if (!StringUtils.isEmpty(saleId)) {
                Sale sale = providerService.getSaleById(saleId);
                if (sale != null && providerService.checkSaleNotUsed(sale)) {
                    String deleteId = providerService.removeSale(saleId);
                    if (deleteId != null) {
                        return AjaxResult.success("Remove sale successfully", deleteId);
                    }
                }
            }
            return AjaxResult.error("Failed to remove sale, the sale has been confirmed by consumer");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/updateSale")
    public AjaxResult updateSale(@RequestBody Sale sale) {
        System.out.println("Update Sale: " + sale.toString());
        try {
            if (sale.getSaleId() != null
                    && providerService.getSaleById(sale.getSaleId()) != null) {
                Distribution distribution = distributorService.getDistributionById(sale.getDistributionId());
                if (distribution != null
                        && Long.parseLong(distribution.getSuppliesUnit().getQuantity()) >= Long.parseLong(sale.getSuppliesUnit().getQuantity())) {
                    Sale newSale = providerService.updateSale(sale);
                    if (newSale != null) {
                        return AjaxResult.success("Update sale successfully", newSale);
                    }
                }
            }
            return AjaxResult.error("Failed to update sale, distribution and sale do not match");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
