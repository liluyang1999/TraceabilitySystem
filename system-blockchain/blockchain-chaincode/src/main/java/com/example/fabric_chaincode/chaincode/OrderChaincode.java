package com.example.fabric_chaincode.chaincode;

import com.example.fabric_chaincode.model.TxRecord;
import com.example.fabric_chaincode.model.order.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hyperledger.fabric.shim.Chaincode.Response;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static java.nio.charset.StandardCharsets.UTF_8;

public class OrderChaincode {

    private static final Type ORDER_TYPE = new TypeToken<List<Order>>(){}.getType();

    private static final String ORDER_KEY = "orders";

    Gson gson = new Gson();

    public Response selectAllOrders(ChaincodeStub stub, List<String> params) {
        String ordersStr = stub.getStringState(ORDER_KEY);
        byte[] payload = null;
        if (!ordersStr.isEmpty()) {
            payload = ordersStr.getBytes(UTF_8);
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectOrdersByStatus(ChaincodeStub stub, List<String> params) {
        String status = params.get(1);

        String ordersStr = stub.getStringState(ORDER_KEY);
        byte[] payload = null;
        if (!ordersStr.isEmpty()) {
            List<Order> orders = gson.fromJson(ordersStr, ORDER_TYPE);
            List<Order> results = new LinkedList<>();
            for (Order order : orders) {
                if (order.getStatus().equals(status)) {
                    results.add(order);
                }
            }
            if (!results.isEmpty()) {
                String resultsStr = gson.toJson(results, ORDER_TYPE);
                payload = resultsStr.getBytes(UTF_8);
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectOrderById(ChaincodeStub stub, List<String> params) {
        String orderId = params.get(1);

        String ordersStr = stub.getStringState(ORDER_KEY);
        byte[] payload = null;
        if (!ordersStr.isEmpty()) {
            List<Order> orders = gson.fromJson(ordersStr, ORDER_TYPE);
            for (Order order : orders) {
                if (order.getOrderId().equals(orderId)) {
                    String orderStr = gson.toJson(order, Order.class);
                    payload = orderStr.getBytes(UTF_8);
                }
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response insertOrder(ChaincodeStub stub, List<String> params) {
        String orderStr = params.get(1);

        Order order = gson.fromJson(orderStr, Order.class);
        order.setTxRecords(new LinkedList<>());
        order.getTxRecords().add(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String ordersStr = stub.getStringState(ORDER_KEY);
        List<Order> orders = (!ordersStr.isEmpty()) ? gson.fromJson(ordersStr, ORDER_TYPE)
                                                        : new LinkedList<>();
        orders.add(order);
        ordersStr = gson.toJson(orders, ORDER_TYPE);
        stub.putStringState(ORDER_KEY, ordersStr);

        orderStr = gson.toJson(order, Order.class);
        byte[] payload = orderStr.getBytes(UTF_8);
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response updateOrder(ChaincodeStub stub, List<String> params) {
        String orderStr = params.get(1);
        Order order = gson.fromJson(orderStr, Order.class);
        order.getTxRecords().add(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String ordersStr = stub.getStringState(ORDER_KEY);
        List<Order> orders = gson.fromJson(ordersStr, ORDER_TYPE);
        byte[] payload = null;
        ListIterator<Order> iterator = orders.listIterator();
        while (iterator.hasNext()) {
            Order each = iterator.next();
            if (each.getOrderId().equals(order.getOrderId())) {
                iterator.set(order);
                orderStr = gson.toJson(order, Order.class);
                payload = orderStr.getBytes(UTF_8);
                break;
            }
        }
        ordersStr = gson.toJson(orders, ORDER_TYPE);
        stub.putStringState(ORDER_KEY, ordersStr);

        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response deleteOrder(ChaincodeStub stub, List<String> params) {
        String orderId = params.get(1);

        String ordersStr = stub.getStringState(ORDER_KEY);
        List<Order> orders = gson.fromJson(ordersStr, ORDER_TYPE);
        ListIterator<Order> iterator = orders.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            Order each = iterator.next();
            if (each.getOrderId().equals(orderId)) {
                iterator.remove();
                payload = orderId.getBytes(UTF_8);
                break;
            }
        }
        ordersStr = gson.toJson(orders, ORDER_TYPE);
        stub.putStringState(ORDER_KEY, ordersStr);

        return ResponseUtils.newSuccessResponse(payload);
    }
}
