package com.example.blockchain_gateway.service;

import com.example.blockchain_gateway.model.order.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.example.blockchain_gateway.domain.DeptType.ORDER;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class OrderService {

    @Autowired
    private Gson gson;

    public List<Order> getAllOrders(Contract contract) throws ContractException {
        byte[] result = contract.evaluateTransaction(ORDER, "selectAllOrders");
        if (result != null) {
            String ordersStr = new String(result, UTF_8);
            Type type = new TypeToken<List<Order>>(){}.getType();
            return gson.fromJson(ordersStr, type);
        } else {
            return null;
        }
    }

    public List<Order> getOrdersByStatus(String status, Contract contract) throws ContractException {
        byte[] result = contract.evaluateTransaction(ORDER, "selectOrdersByStatus", status);
        if (result != null) {
            String ordersStr = new String(result, UTF_8);
            Type type = new TypeToken<List<Order>>(){}.getType();
            return gson.fromJson(ordersStr, type);
        } else {
            return null;
        }
    }

    public Order getOrderById(String orderId, Contract contract) throws ContractException {
        byte[] result = contract.evaluateTransaction(ORDER, "selectOrderById", orderId);
        if (result != null) {
            String orderStr = new String(result, UTF_8);
            return gson.fromJson(orderStr, Order.class);
        } else {
            return null;
        }
    }

    public Order insertOrder(Order order, Contract contract)
            throws ContractException, InterruptedException, TimeoutException {
        String orderStr = gson.toJson(order, Order.class);
        byte[] result = contract.submitTransaction(ORDER, "insertOrder", orderStr);
        if (result != null) {
            orderStr = new String(result, UTF_8);
            order = gson.fromJson(orderStr, Order.class);
            return order;
        } else {
            return null;
        }
    }

    public Order updateOrder(Order order, Contract contract)
            throws ContractException, InterruptedException, TimeoutException {
        String orderStr = gson.toJson(order, Order.class);
        byte[] result = contract.submitTransaction(ORDER, "updateOrder", orderStr);
        if (result != null) {
            orderStr = new String(result, UTF_8);
            order = gson.fromJson(orderStr, Order.class);
            return order;
        } else {
            return null;
        }
    }

    public String removeOrder(String orderId, Contract contract)
            throws ContractException, InterruptedException, TimeoutException {
        byte[] result = contract.submitTransaction(ORDER, "deleteOrder", orderId);
        if (result != null) {
            return new String(result, UTF_8);
        } else {
            return null;
        }
    }
}
