package com.example.blockchain_gateway.service;

import com.example.blockchain_gateway.model.distributor.Distribution;
import com.example.blockchain_gateway.model.distributor.DistributionStatus;
import com.example.blockchain_gateway.model.distributor.Inventory;
import com.example.blockchain_gateway.model.order.Order;
import com.example.blockchain_gateway.model.order.OrderStatus;
import com.example.blockchain_gateway.model.supplies.Supplies;
import com.example.blockchain_gateway.model.supplies.SuppliesUnit;
import com.example.blockchain_gateway.utils.DateUtils;
import com.example.blockchain_gateway.utils.UUIDUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static com.example.blockchain_gateway.domain.DeptType.DISTRIBUTOR;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class DistributorService {

    @Resource(name = "distributorContract")
    private Contract contract;

    @Autowired
    private Gson gson;

    @Autowired
    private OrderService orderService;

    public Order sendOrder(Order order) throws ContractException, InterruptedException, TimeoutException {
        order.setOrderId(UUIDUtils.simpleUUID());
        order.setStatus(OrderStatus.REQUESTED);
        order.setRequestDate(DateUtils.getCurrentDateTime());
        return orderService.insertOrder(order, contract);
    }

    public Order receiveProducts(Order order) throws ContractException, InterruptedException, TimeoutException {
        order.setStatus(OrderStatus.RECEIVED);
        order.setReceiveDate(DateUtils.getCurrentDateTime());
        return orderService.updateOrder(order, contract);
    }

    public List<Distribution> getAllDistributions() throws ContractException {
        byte[] result = contract.evaluateTransaction(DISTRIBUTOR, "selectAllDistributions");
        if (result != null) {
            String distributionsStr = new String(result, UTF_8);
            Type type = new TypeToken<List<Distribution>>(){}.getType();
            return gson.fromJson(distributionsStr, type);
        } else {
            return null;
        }
    }

    public List<Distribution> getDistributionsByStatus(String status) throws ContractException {
        byte[] result = contract.evaluateTransaction(DISTRIBUTOR, "selectDistributionsByStatus", status);
        if (result != null) {
            String distributionsStr = new String(result, UTF_8);
            Type type = new TypeToken<List<Distribution>>(){}.getType();
            return gson.fromJson(distributionsStr, type);
        } else {
            return null;
        }
    }

    public Distribution getDistributionById(String distributionId) throws ContractException {
        byte[] result = contract.evaluateTransaction(DISTRIBUTOR, "selectDistributionById", distributionId);
        if (result != null) {
            String distributionStr = new String(result, UTF_8);
            return gson.fromJson(distributionStr, Distribution.class);
        } else {
            return null;
        }
    }

    public Distribution distributeProducts(Distribution distribution) throws ContractException, InterruptedException, TimeoutException {
        distribution.setDistributionId(UUIDUtils.simpleUUID());
        distribution.setStatus(DistributionStatus.DELIVERED);
        distribution.setDeliverDate(DateUtils.getCurrentDateTime());
        String distributionStr = gson.toJson(distribution, Distribution.class);
        byte[] result = contract.submitTransaction(DISTRIBUTOR, "insertDistribution", distributionStr);
        if (result != null) {
            distributionStr = new String(result, UTF_8);
            return gson.fromJson(distributionStr, Distribution.class);
        } else {
            return null;
        }
    }

    public Distribution updateDistribution(Distribution distribution) throws ContractException, InterruptedException, TimeoutException {
        String distributionStr = gson.toJson(distribution, Distribution.class);
        byte[] result = contract.submitTransaction(DISTRIBUTOR, "updateDistribution", distributionStr);
        if (result != null) {
            distributionStr = new String(result, UTF_8);
            return gson.fromJson(distributionStr, Distribution.class);
        } else {
            return null;
        }
    }

    public String removeDistribution(String distributionId) throws ContractException, InterruptedException, TimeoutException {
        byte[] result = contract.submitTransaction(DISTRIBUTOR, "deleteDistribution", distributionId);
        if (result != null) {
            return new String(result, UTF_8);
        } else {
            return null;
        }
    }

    public List<Inventory> getAllInventories() throws ContractException {
        byte[] result = contract.evaluateTransaction(DISTRIBUTOR, "selectAllInventories");
        if (result != null) {
            String inventoriesStr = new String(result, UTF_8);
            Type type = new TypeToken<List<Inventory>>(){}.getType();
            return gson.fromJson(inventoriesStr, type);
        } else {
            return null;
        }
    }

    public void storeIntoInventory(Order order) throws ContractException, InterruptedException, TimeoutException {
        SuppliesUnit suppliesUnit = order.getSuppliesUnit();
        Supplies supplies = suppliesUnit.getSupplies();

        List<Inventory> inventories = getAllInventories();
        if (inventories != null) {
            Optional<Inventory> result = inventories.stream().filter(
                    each -> each.getDistributorId().equals(order.getDistributorId())).findFirst();
            Inventory inventory;
            String inventoryStr;
            if (result.isPresent()) {
                inventory = result.get();
                String remaining = inventory.getStock().get(supplies.getId());
                if (remaining != null) {
                    remaining = String.valueOf(Long.parseLong(remaining) + Long.parseLong(suppliesUnit.getQuantity()));
                    inventory.getStock().put(supplies.getId(), remaining);
                } else {
                    remaining = suppliesUnit.getQuantity();
                    inventory.getStock().put(supplies.getId(), remaining);
                }
                inventoryStr = gson.toJson(inventory, Inventory.class);
                contract.submitTransaction(DISTRIBUTOR, "updateInventory", inventoryStr);
            } else {
                createInventory(order);
            }
        } else {
            createInventory(order);
        }
    }

    public void reduceInventory(Distribution distribution) throws ContractException, InterruptedException, TimeoutException {
        SuppliesUnit suppliesUnit = distribution.getSuppliesUnit();
        Supplies supplies = suppliesUnit.getSupplies();

        List<Inventory> inventories = getAllInventories();
        if (inventories != null) {
            Optional<Inventory> result = inventories.stream().filter(
                    each -> each.getDistributorId().equals(distribution.getDistributorId())).findFirst();
            if (result.isPresent()) {
                Inventory inventory = result.get();
                String remaining = inventory.getStock().get(supplies.getId());
                if (remaining != null) {
                    remaining = String.valueOf(Long.parseLong(remaining) - Long.parseLong(suppliesUnit.getQuantity()));
                    inventory.getStock().put(supplies.getId(), remaining);
                }
                String inventoryStr = gson.toJson(inventory, Inventory.class);
                contract.submitTransaction(DISTRIBUTOR, "updateInventory", inventoryStr);
            }
        }
    }

    private void createInventory(Order order) throws ContractException, InterruptedException, TimeoutException {
        SuppliesUnit suppliesUnit = order.getSuppliesUnit();
        Supplies supplies = suppliesUnit.getSupplies();
        Inventory inventory = new Inventory(order.getDistributorId(), order.getDistributorName(), new HashMap<>());
        inventory.getStock().put(supplies.getId(), suppliesUnit.getQuantity());
        String inventoryStr = gson.toJson(inventory, Inventory.class);
        contract.submitTransaction(DISTRIBUTOR, "insertInventory", inventoryStr);
    }
}
