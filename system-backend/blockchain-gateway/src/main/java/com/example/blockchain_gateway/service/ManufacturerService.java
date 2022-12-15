package com.example.blockchain_gateway.service;

import com.example.blockchain_gateway.model.order.Order;
import com.example.blockchain_gateway.model.order.OrderStatus;
import com.example.blockchain_gateway.model.manufacturer.Process;
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
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.example.blockchain_gateway.domain.DeptType.MANUFACTURER;

@Service
public class ManufacturerService {

    @Resource(name = "manufacturerContract")
    private Contract contract;

    @Autowired
    private Gson gson;

    @Autowired
    private OrderService orderService;

    public Order confirmOrder(Order order) throws ContractException, InterruptedException, TimeoutException {
        order.setStatus(OrderStatus.CONFIRMED);
        return orderService.updateOrder(order, contract);
    }

    public List<Process> getAllProcesses() throws ContractException {
        byte[] result = contract.evaluateTransaction(MANUFACTURER, "selectAllProcesses");
        if (result != null) {
            String processesStr = new String(result);
            Type type = new TypeToken<List<Process>>(){}.getType();
            return gson.fromJson(processesStr, type);
        } else {
            return null;
        }
    }

    public List<Process> getProcessesByOrderId(String orderId) throws ContractException {
        byte[] result = contract.evaluateTransaction(MANUFACTURER, "selectProcessesByOrderId", orderId);
        if (result != null) {
            String processesStr = new String(result);
            Type type = new TypeToken<List<Process>>(){}.getType();
            return gson.fromJson(processesStr, type);
        } else {
            return null;
        }
    }

    public Process getProcessById(String processId) throws ContractException {
        byte[] result = contract.evaluateTransaction(MANUFACTURER, "selectProcessById", processId);
        if (result != null) {
            String processStr = new String(result);
            return gson.fromJson(processStr, Process.class);
        } else {
            return null;
        }
    }

    public Process recordProcess(Process process) throws ContractException, InterruptedException, TimeoutException {
        process.setProcessId(UUIDUtils.simpleUUID());
        process.setRecordDate(DateUtils.getCurrentDateTime());
        String processStr = gson.toJson(process, Process.class);
        byte[] result = contract.submitTransaction(MANUFACTURER, "insertProcess", processStr);
        if (result != null) {
            processStr = new String(result);
            return gson.fromJson(processStr, Process.class);
        } else {
            return null;
        }
    }

    public Process updateProcess(Process process) throws ContractException, InterruptedException, TimeoutException {
        process.setRecordDate(DateUtils.getCurrentDateTime());
        String processStr = gson.toJson(process, Process.class);
        byte[] result = contract.submitTransaction(MANUFACTURER, "updateProcess", processStr);
        if (result != null) {
            processStr = new String(result);
            return gson.fromJson(processStr, Process.class);
        } else {
            return null;
        }
    }

    public Order transferProducts(Order order) throws ContractException, InterruptedException, TimeoutException {
        order.setStatus(OrderStatus.TRANSFERRED);
        return orderService.updateOrder(order, contract);
    }
}
