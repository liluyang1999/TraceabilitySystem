package com.example.fabric_chaincode.chaincode;

import com.example.fabric_chaincode.model.TxRecord;
import com.example.fabric_chaincode.model.provider.Sale;
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

public class ProviderChaincode {

    private static final Type SALE_TYPE = new TypeToken<List<Sale>>(){}.getType();

    private static final String SALE_KEY = "sales";

    Gson gson = new Gson();

    public Response selectAllSales(ChaincodeStub stub, List<String> params) {
        String salesStr = stub.getStringState(SALE_KEY);
        byte[] payload = null;
        if (!salesStr.isEmpty()) {
            payload = salesStr.getBytes(UTF_8);
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectSaleById(ChaincodeStub stub, List<String> params) {
        String saleId = params.get(1);

        String salesStr = stub.getStringState(SALE_KEY);
        byte[] payload = null;
        if (!salesStr.isEmpty()) {
            List<Sale> sales = gson.fromJson(salesStr, SALE_TYPE);
            for (Sale sale : sales) {
                if (sale.getSaleId().equals(saleId)) {
                    String resultStr = gson.toJson(sale, Sale.class);
                    payload = resultStr.getBytes(UTF_8);
                    break;
                }
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectSalesByOrderId(ChaincodeStub stub, List<String> params) {
        String orderId = params.get(1);

        String salesStr = stub.getStringState(SALE_KEY);
        byte[] payload = null;
        if (!salesStr.isEmpty()) {
            List<Sale> sales = gson.fromJson(salesStr, SALE_TYPE);
            List<Sale> results = new LinkedList<>();
            for (Sale sale : sales) {
                if (sale.getOrderId().equals(orderId)) {
                    results.add(sale);
                }
            }
            if (!results.isEmpty()) {
                String resultsStr = gson.toJson(results, SALE_TYPE);
                payload = resultsStr.getBytes(UTF_8);
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectSalesByUsername(ChaincodeStub stub, List<String> params) {
        String username = params.get(1);

        String salesStr = stub.getStringState(SALE_KEY);
        byte[] payload = null;
        if (!salesStr.isEmpty()) {
            List<Sale> sales = gson.fromJson(salesStr, SALE_TYPE);
            List<Sale> results = new LinkedList<>();
            for (Sale sale : sales) {
                if (sale.getUsername().equals(username)) {
                    results.add(sale);
                }
            }
            if (!results.isEmpty()) {
                String resultsStr = gson.toJson(results, SALE_TYPE);
                payload = resultsStr.getBytes(UTF_8);
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response insertSale(ChaincodeStub stub, List<String> params) {
        String saleStr = params.get(1);
        Sale sale = gson.fromJson(saleStr, Sale.class);
        sale.setTxRecords(new LinkedList<>());
        sale.getTxRecords().add(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String salesStr = stub.getStringState(SALE_KEY);
        List<Sale> sales = (!salesStr.isEmpty()) ? gson.fromJson(salesStr, SALE_TYPE)
                                                    : new LinkedList<>();
        sales.add(sale);
        salesStr = gson.toJson(sales, SALE_TYPE);
        stub.putStringState(SALE_KEY, salesStr);

        saleStr = gson.toJson(sale, Sale.class);
        byte[] payload = saleStr.getBytes(UTF_8);
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response updateSale(ChaincodeStub stub, List<String> params) {
        String saleStr = params.get(1);
        Sale sale = gson.fromJson(saleStr, Sale.class);
        sale.getTxRecords().add(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String salesStr = stub.getStringState(SALE_KEY);
        List<Sale> sales = gson.fromJson(salesStr, SALE_TYPE);
        ListIterator<Sale> iterator = sales.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            Sale each = iterator.next();
            if (each.getSaleId().equals(sale.getSaleId())) {
                iterator.set(sale);
                saleStr = gson.toJson(sale, Sale.class);
                payload = saleStr.getBytes(UTF_8);
                break;
            }
        }
        salesStr = gson.toJson(sales, SALE_TYPE);
        stub.putStringState(SALE_KEY, salesStr);

        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response deleteSale(ChaincodeStub stub, List<String> params) {
        String saleId = params.get(1);

        String salesStr = stub.getStringState(SALE_KEY);
        List<Sale> sales = gson.fromJson(salesStr, SALE_TYPE);
        ListIterator<Sale> iterator = sales.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            Sale each = iterator.next();
            if (each.getSaleId().equals(saleId)) {
                iterator.remove();
                payload = saleId.getBytes(UTF_8);
                break;
            }
        }
        salesStr = gson.toJson(sales, SALE_TYPE);
        stub.putStringState(SALE_KEY, salesStr);

        return ResponseUtils.newSuccessResponse(payload);
    }
}
