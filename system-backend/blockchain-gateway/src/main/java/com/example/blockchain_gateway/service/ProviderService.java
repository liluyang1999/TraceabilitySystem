package com.example.blockchain_gateway.service;

import com.example.blockchain_gateway.model.consumer.TraceTask;
import com.example.blockchain_gateway.model.distributor.Distribution;
import com.example.blockchain_gateway.model.distributor.DistributionStatus;
import com.example.blockchain_gateway.model.provider.Sale;
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

import static com.example.blockchain_gateway.domain.DeptType.DISTRIBUTOR;
import static com.example.blockchain_gateway.domain.DeptType.PROVIDER;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class ProviderService {

    @Resource(name = "providerContract")
    private Contract contract;

    @Autowired
    private Gson gson;

    @Autowired
    private ConsumerService consumerService;

    public Distribution receiveProducts(Distribution distribution) throws ContractException, InterruptedException, TimeoutException {
        distribution.setStatus(DistributionStatus.RECEIVED);
        distribution.setReceiveDate(DateUtils.getCurrentDateTime());

        String distributionStr = gson.toJson(distribution, Distribution.class);
        byte[] result = contract.submitTransaction(DISTRIBUTOR, "updateDistribution", distributionStr);
        if (result != null) {
            distributionStr = new String(result, UTF_8);
            return gson.fromJson(distributionStr, Distribution.class);
        } else {
            return null;
        }
    }

    public List<Sale> getAllSales() throws ContractException {
        byte[] result = contract.evaluateTransaction(PROVIDER, "selectAllSales");
        if (result != null) {
            String salesStr = new String(result, UTF_8);
            Type type = new TypeToken<List<Sale>>(){}.getType();
            return gson.fromJson(salesStr, type);
        } else {
            return null;
        }
    }

    public Sale getSaleById(String saleId) throws ContractException {
        byte[] result = contract.evaluateTransaction(PROVIDER, "selectSaleById", saleId);
        if (result != null) {
            String saleStr = new String(result, UTF_8);
            return gson.fromJson(saleStr, Sale.class);
        } else {
            return null;
        }
    }

    public Sale sellProducts(Sale sale) throws ContractException, InterruptedException, TimeoutException {
        sale.setSaleId(UUIDUtils.simpleUUID());
        String saleStr = gson.toJson(sale, Sale.class);
        byte[] result = contract.submitTransaction(PROVIDER, "insertSale", saleStr);
        if (result != null) {
            saleStr = new String(result, UTF_8);
            return gson.fromJson(saleStr, Sale.class);
        } else {
            return null;
        }
    }

    public Sale updateSale(Sale sale) throws ContractException, InterruptedException, TimeoutException {
        String saleStr = gson.toJson(sale, Sale.class);
        byte[] result = contract.submitTransaction(PROVIDER, "updateSale", saleStr);
        if (result != null) {
            saleStr = new String(result, UTF_8);
            return gson.fromJson(saleStr, Sale.class);
        } else {
            return null;
        }
    }

    public String removeSale(String saleId) throws ContractException, InterruptedException, TimeoutException {
        byte[] result = contract.submitTransaction(PROVIDER, "deleteSale", saleId);
        if (result != null) {
            return new String(result, UTF_8);
        } else {
            return null;
        }
    }

    public Boolean checkSaleNotUsed(Sale sale) throws ContractException {
        List<TraceTask> traceTasks = consumerService.getAllTraceTasks();
        if (traceTasks != null) {
            return traceTasks.stream().noneMatch(each -> each.getSale().getSaleId().equals(sale.getSaleId()));
        } else {
            return true;
        }
    }
}
