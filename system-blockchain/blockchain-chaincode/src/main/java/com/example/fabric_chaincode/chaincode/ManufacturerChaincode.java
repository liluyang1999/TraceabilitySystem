package com.example.fabric_chaincode.chaincode;

import com.example.fabric_chaincode.model.TxRecord;
import com.example.fabric_chaincode.model.manufacturer.Process;
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

public class ManufacturerChaincode {

    private static final Type PROCESS_TYPE = new TypeToken<List<Process>>(){}.getType();

    private static final String PROCESS_KEY = "processes";

    Gson gson = new Gson();

    public Response selectAllProcesses(ChaincodeStub stub, List<String> params) {
        String processesStr = stub.getStringState(PROCESS_KEY);
        byte[] payload = null;
        if (!processesStr.isEmpty()) {
            payload = processesStr.getBytes(UTF_8);
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectProcessesByOrderId(ChaincodeStub stub, List<String> params) {
        String orderId = params.get(1);

        String processesStr = stub.getStringState(PROCESS_KEY);
        byte[] payload = null;
        if (!processesStr.isEmpty()) {
            List<Process> processes = gson.fromJson(processesStr, PROCESS_TYPE);
            List<Process> results = new LinkedList<>();
            for (Process process : processes) {
                if (process.getOrderId().equals(orderId)) {
                    results.add(process);
                }
            }
            if (!results.isEmpty()) {
                String resultsStr = gson.toJson(results, PROCESS_TYPE);
                payload = resultsStr.getBytes(UTF_8);
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectProcessById(ChaincodeStub stub, List<String> params) {
        String processId = params.get(1);

        String processesStr = stub.getStringState(PROCESS_KEY);
        byte[] payload = null;
        if (!processesStr.isEmpty()) {
            List<Process> processes = gson.fromJson(processesStr, PROCESS_TYPE);
            for (Process process : processes) {
                if (process.getProcessId().equals(processId)) {
                    String processStr = gson.toJson(process, Process.class);
                    payload = processStr.getBytes(UTF_8);
                }
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response insertProcess(ChaincodeStub stub, List<String> params) {
        String processStr = params.get(1);
        Process process = gson.fromJson(processStr, Process.class);
        process.setTxRecords(new LinkedList<>());
        process.getTxRecords().add(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String processesStr = stub.getStringState(PROCESS_KEY);
        List<Process> processes = (!processesStr.isEmpty()) ? gson.fromJson(processesStr, PROCESS_TYPE)
                                                                : new LinkedList<>();
        processes.add(process);
        processesStr = gson.toJson(processes, PROCESS_TYPE);
        stub.putStringState(PROCESS_KEY, processesStr);

        processStr = gson.toJson(process, Process.class);
        byte[] payload = processStr.getBytes(UTF_8);
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response updateProcess(ChaincodeStub stub, List<String> params) {
        String processStr = params.get(1);
        Process process = gson.fromJson(processStr, Process.class);
        process.getTxRecords().add(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String processesStr = stub.getStringState(PROCESS_KEY);
        List<Process> processes = gson.fromJson(processesStr, PROCESS_TYPE);
        ListIterator<Process> iterator = processes.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            Process each = iterator.next();
            if (each.getProcessId().equals(process.getProcessId())) {
                iterator.set(process);
                processStr = gson.toJson(process, Process.class);
                payload = processStr.getBytes(UTF_8);
                break;
            }
        }
        processesStr = gson.toJson(processes, PROCESS_TYPE);
        stub.putStringState(PROCESS_KEY, processesStr);

        return ResponseUtils.newSuccessResponse(payload);
    }
}
