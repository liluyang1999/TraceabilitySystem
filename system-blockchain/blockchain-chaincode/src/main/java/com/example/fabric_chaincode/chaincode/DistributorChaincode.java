package com.example.fabric_chaincode.chaincode;

import com.example.fabric_chaincode.model.TxRecord;
import com.example.fabric_chaincode.model.distributor.Distribution;
import com.example.fabric_chaincode.model.distributor.Inventory;
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

public class DistributorChaincode {

    private static final Type DISTRIBUTION_TYPE = new TypeToken<List<Distribution>>(){}.getType();

    private static final Type INVENTORY_TYPE = new TypeToken<List<Inventory>>(){}.getType();

    private static final String DISTRIBUTION_KEY = "distributions";

    private static final String INVENTORY_KEY = "inventories";

    Gson gson = new Gson();

    public Response selectAllDistributions(ChaincodeStub stub, List<String> params) {
        String resultsStr = stub.getStringState(DISTRIBUTION_KEY);
        byte[] payload = null;
        if (!resultsStr.isEmpty()) {
            payload = resultsStr.getBytes(UTF_8);
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectDistributionById(ChaincodeStub stub, List<String> params) {
        String distributionId = params.get(1);

        String distributionsStr = stub.getStringState(DISTRIBUTION_KEY);
        byte[] payload = null;
        if (!distributionsStr.isEmpty()) {
            List<Distribution> distributions = gson.fromJson(distributionsStr, DISTRIBUTION_TYPE);
            for (Distribution distribution : distributions) {
                if (distribution.getDistributionId().equals(distributionId)) {
                    String resultStr = gson.toJson(distribution, Distribution.class);
                    payload = resultStr.getBytes(UTF_8);
                    break;
                }
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectDistributionsByStatus(ChaincodeStub stub, List<String> params) {
        String status = params.get(1);

        String distributionsStr = stub.getStringState(DISTRIBUTION_KEY);
        byte[] payload = null;
        if (!distributionsStr.isEmpty()) {
            List<Distribution> distributions = gson.fromJson(distributionsStr, DISTRIBUTION_TYPE);
            List<Distribution> results = new LinkedList<>();
            for (Distribution distribution : distributions) {
                if (distribution.getStatus().equals(status)) {
                    results.add(distribution);
                }
            }
            if (!results.isEmpty()) {
                String resultsStr = gson.toJson(results, DISTRIBUTION_TYPE);
                payload = resultsStr.getBytes(UTF_8);
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response insertDistribution(ChaincodeStub stub, List<String> params) {
        String distributionStr = params.get(1);
        Distribution distribution = gson.fromJson(distributionStr, Distribution.class);
        distribution.setTxRecords(new LinkedList<>());
        distribution.getTxRecords().add(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String distributionsStr = stub.getStringState(DISTRIBUTION_KEY);
        List<Distribution> distributions = (!distributionsStr.isEmpty()) ? gson.fromJson(distributionsStr, DISTRIBUTION_TYPE)
                                                                            : new LinkedList<>();
        distributions.add(distribution);
        distributionsStr = gson.toJson(distributions, DISTRIBUTION_TYPE);
        stub.putStringState(DISTRIBUTION_KEY, distributionsStr);

        distributionStr = gson.toJson(distribution, Distribution.class);
        byte[] payload = distributionStr.getBytes(UTF_8);
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response updateDistribution(ChaincodeStub stub, List<String> params) {
        String distributionStr = params.get(1);
        Distribution distribution = gson.fromJson(distributionStr, Distribution.class);
        distribution.getTxRecords().add(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String distributionsStr = stub.getStringState(DISTRIBUTION_KEY);
        List<Distribution> distributions = gson.fromJson(distributionsStr, DISTRIBUTION_TYPE);
        ListIterator<Distribution> iterator = distributions.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            Distribution each = iterator.next();
            if (each.getDistributionId().equals(distribution.getDistributionId())) {
                iterator.set(distribution);
                distributionStr = gson.toJson(distribution, Distribution.class);
                payload = distributionStr.getBytes(UTF_8);
            }
        }
        distributionsStr = gson.toJson(distributions, DISTRIBUTION_TYPE);
        stub.putStringState(DISTRIBUTION_KEY, distributionsStr);

        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response deleteDistribution(ChaincodeStub stub, List<String> params) {
        String distributionId = params.get(1);

        String distributionsStr = stub.getStringState(DISTRIBUTION_KEY);
        List<Distribution> distributions = gson.fromJson(distributionsStr, DISTRIBUTION_TYPE);
        ListIterator<Distribution> iterator = distributions.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            Distribution distribution = iterator.next();
            if (distribution.getDistributionId().equals(distributionId)) {
                iterator.remove();
                payload = distributionId.getBytes(UTF_8);
                break;
            }
        }
        distributionsStr = gson.toJson(distributions, DISTRIBUTION_TYPE);
        stub.putStringState(DISTRIBUTION_KEY, distributionsStr);

        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectAllInventories(ChaincodeStub stub, List<String> params) {
        String inventoriesStr = stub.getStringState(INVENTORY_KEY);
        byte[] payload = null;
        if (!inventoriesStr.isEmpty()) {
            payload = inventoriesStr.getBytes(UTF_8);
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response insertInventory(ChaincodeStub stub, List<String> params) {
        String inventoryStr = params.get(1);
        Inventory inventory = gson.fromJson(inventoryStr, Inventory.class);

        String inventoriesStr = stub.getStringState(INVENTORY_KEY);
        List<Inventory> inventories = (!inventoriesStr.isEmpty()) ? gson.fromJson(inventoriesStr, INVENTORY_TYPE)
                                                                     : new LinkedList<>();
        inventories.add(inventory);
        inventoriesStr = gson.toJson(inventories, INVENTORY_TYPE);
        stub.putStringState(INVENTORY_KEY, inventoriesStr);

        byte[] payload = inventoryStr.getBytes(UTF_8);
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response updateInventory(ChaincodeStub stub, List<String> params) {
        String inventoryStr = params.get(1);
        Inventory inventory = gson.fromJson(inventoryStr, Inventory.class);

        String inventoriesStr = stub.getStringState(INVENTORY_KEY);
        List<Inventory> inventories = gson.fromJson(inventoriesStr, INVENTORY_TYPE);
        ListIterator<Inventory> iterator = inventories.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            Inventory each = iterator.next();
            if (each.getDistributorId().equals(inventory.getDistributorId())) {
                iterator.set(inventory);
                payload = inventoryStr.getBytes(UTF_8);
                break;
            }
        }
        inventoriesStr = gson.toJson(inventories, INVENTORY_TYPE);
        stub.putStringState(INVENTORY_KEY, inventoriesStr);

        return ResponseUtils.newSuccessResponse(payload);
    }
}
