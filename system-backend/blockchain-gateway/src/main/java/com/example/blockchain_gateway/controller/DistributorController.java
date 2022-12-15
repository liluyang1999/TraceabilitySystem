package com.example.blockchain_gateway.controller;

import com.example.blockchain_gateway.domain.AjaxResult;
import com.example.blockchain_gateway.model.distributor.Distribution;
import com.example.blockchain_gateway.model.distributor.DistributionStatus;
import com.example.blockchain_gateway.model.distributor.Inventory;
import com.example.blockchain_gateway.model.order.Order;
import com.example.blockchain_gateway.model.order.OrderStatus;
import com.example.blockchain_gateway.model.supplies.Supplies;
import com.example.blockchain_gateway.service.DistributorService;
import com.example.blockchain_gateway.service.OrderService;
import com.example.blockchain_gateway.service.RegulatorService;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.gateway.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/blockchain/distributor")
@RestController
public class DistributorController {

    @Resource(name = "distributorContract")
    private Contract contract;

    @Autowired
    private DistributorService distributorService;

    @Autowired
    private RegulatorService regulatorService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/getAllOrders")
    public AjaxResult getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders(contract);
            return AjaxResult.success("Get all orders successfully", orders);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/getOrdersByStatus")
    public AjaxResult getOrdersByStatus(@RequestParam("status") String status) {
        try {
            List<Order> orders = orderService.getOrdersByStatus(status, contract);
            return AjaxResult.success("Get orders by status successfully", orders);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/getAllDistributions")
    public AjaxResult getAllDistributions() {
        try {
            List<Distribution> distributions = distributorService.getAllDistributions();
            return AjaxResult.success("Get all distributions successfully", distributions);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/getDistributionsByStatus")
    public AjaxResult getDistributionsByStatus(@RequestParam("status") String status) {
        try {
            List<Distribution> distributions = distributorService.getDistributionsByStatus(status);
            return AjaxResult.success("Get distributions by status successfully", distributions);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/getAllInventories")
    public AjaxResult getAllInventories() {
        try {
            List<Inventory> inventories = distributorService.getAllInventories();
            return AjaxResult.success("Get all inventories successfully", inventories);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/sendOrder")
    public AjaxResult sendOrder(@RequestBody Order order) {
        System.out.println("Send Order: " + order.toString());
        try {
            if (StringUtils.isEmpty(order.getOrderId())
                    && order.getDistributorId() != null
                    && !StringUtils.isEmpty(order.getDistributorName())
                    && order.getManufacturerId() != null
                    && !StringUtils.isEmpty(order.getManufacturerName())
                    && order.getSuppliesUnit() != null
                    && order.getSuppliesUnit().getSupplies() != null
                    && !StringUtils.isEmpty(order.getSuppliesUnit().getQuantity())) {
                Supplies supplies = regulatorService.getSuppliesById(order.getSuppliesUnit().getSupplies().getId());
                if (supplies != null) {
                    Order newOrder = distributorService.sendOrder(order);
                    if (newOrder != null) {
                        return AjaxResult.success("Send order successfully", newOrder);
                    }
                }
            }
            return AjaxResult.error("Failed to send order, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/updateOrder")
    public AjaxResult updateOrder(@RequestBody Order order) {
        System.out.println("Update Order: " + order.toString());
        try {
            if (!StringUtils.isEmpty(order.getOrderId())
                    && orderService.getOrderById(order.getOrderId(), contract) != null
                    && order.getSuppliesUnit() != null
                    && !StringUtils.isEmpty(order.getSuppliesUnit().getQuantity())
                    && !StringUtils.isEmpty(order.getStatus())
                    && order.getStatus().equals(OrderStatus.REQUESTED)) {
                Order newOrder = orderService.updateOrder(order, contract);
                if (newOrder != null) {
                    return AjaxResult.success("Update order successfully", newOrder);
                }
            }
            return AjaxResult.error("Failed to update the order, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/removeOrder")
    public AjaxResult removeOrder(@RequestParam String orderId) {
        System.out.println("Remove order: " + orderId);
        try {
            if (!StringUtils.isEmpty(orderId)) {
                Order order = orderService.getOrderById(orderId, contract);
                if (order != null && order.getStatus().equals(OrderStatus.REQUESTED)) {
                    String deleteId = orderService.removeOrder(orderId, contract);
                    if (deleteId != null) {
                        return AjaxResult.success("Remove order successfully", deleteId);
                    }
                }
            }
            return AjaxResult.error("Failed to remove order, the order has been confirmed");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/receiveProducts")
    public AjaxResult receiveProducts(@RequestBody Order order) {
        System.out.println("Receive Products: " + order.toString());
        try {
            if (!StringUtils.isEmpty(order.getOrderId())
                    && orderService.getOrderById(order.getOrderId(), contract) != null
                    && !StringUtils.isEmpty(order.getStatus())
                    && order.getStatus().equals(OrderStatus.TRANSFERRED)) {
                Order newOrder = distributorService.receiveProducts(order);
                if (newOrder != null) {
                    distributorService.storeIntoInventory(newOrder);
                    return AjaxResult.success("Distributor receives products successfully", newOrder);
                }
            }
            return AjaxResult.error("Failed to receive products, please contact the manufacturer");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/distributeProducts")
    public AjaxResult distributeProducts(@RequestBody Distribution distribution) {
        System.out.println("Distribute Products: " + distribution.toString());
        try {
            if (StringUtils.isEmpty(distribution.getDistributionId())
                    && distribution.getDistributorId() != null
                    && !StringUtils.isEmpty(distribution.getDistributorName())
                    && distribution.getProviderId() != null
                    && !StringUtils.isEmpty(distribution.getProviderName())
                    && !StringUtils.isEmpty(distribution.getOrderId())
                    && distribution.getSuppliesUnit() != null
                    && distribution.getSuppliesUnit().getSupplies() != null
                    && !StringUtils.isEmpty(distribution.getSuppliesUnit().getQuantity())) {
                Order order = orderService.getOrderById(distribution.getOrderId(), contract);
                if (order != null && order.getStatus().equals(OrderStatus.RECEIVED)
                        && order.getSuppliesUnit().getSupplies().getId().equals(distribution.getSuppliesUnit().getSupplies().getId())
                        && Long.parseLong(order.getSuppliesUnit().getQuantity()) >= Long.parseLong(distribution.getSuppliesUnit().getQuantity())) {
                    Distribution newDistribution = distributorService.distributeProducts(distribution);
                    if (newDistribution != null) {
                        distributorService.reduceInventory(newDistribution);
                        return AjaxResult.success("Distribute products successfully", newDistribution);
                    }
                }
            }
            return AjaxResult.error("Failed to distribute products, order and supplies do not match");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/updateDistribution")
    public AjaxResult updateDistribution(@RequestBody Distribution distribution) {
        System.out.println("Update Distribution: " + distribution.toString());
        try {
            // only update (deliverDate, driver, contact)
            if (!StringUtils.isEmpty(distribution.getDistributionId())
                    && distribution.getStatus().equals(DistributionStatus.DELIVERED)
                    && distributorService.getDistributionById(distribution.getDistributionId()) != null
                    && !StringUtils.isEmpty(distribution.getDeliverDate())
                    && !StringUtils.isEmpty(distribution.getDriver())
                    && !StringUtils.isEmpty(distribution.getContact())) {
                Distribution newDistribution = distributorService.updateDistribution(distribution);
                if (newDistribution != null) {
                    return AjaxResult.success("Update distribution successfully", newDistribution);
                }
            }
            return AjaxResult.error("Failed to update distribution, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/removeDistribution")
    public AjaxResult removeDistribution(@RequestParam String distributionId) {
        System.out.println("Remove distribution: " + distributionId);
        try {
            if (!StringUtils.isEmpty(distributionId)) {
                Distribution distribution = distributorService.getDistributionById(distributionId);
                if (distribution != null && !distribution.getStatus().equals(DistributionStatus.RECEIVED)) {
                    String deleteId = distributorService.removeDistribution(distributionId);
                    if (deleteId != null) {
                        Order order = orderService.getOrderById(distribution.getOrderId(), contract);
                        if (order != null) {
                            order.setSuppliesUnit(distribution.getSuppliesUnit());
                            distributorService.storeIntoInventory(order);
                        }
                        return AjaxResult.success("Remove distribution successfully", deleteId);
                    }
                }
            }
            return AjaxResult.success("Failed to remove distribution, the distribution has been received");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
