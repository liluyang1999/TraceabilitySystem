package com.example.blockchain_gateway.service;

import com.example.blockchain_gateway.model.order.Order;
import com.example.blockchain_gateway.model.order.OrderStatus;
import com.example.blockchain_gateway.model.regulator.License;
import com.example.blockchain_gateway.model.supplies.Supplies;
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

import static com.example.blockchain_gateway.domain.DeptType.REGULATOR;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class RegulatorService {

    @Resource(name = "regulatorContract")
    private Contract contract;

    @Autowired
    private Gson gson;

    @Autowired
    private OrderService orderService;

    public List<License> getAllLicenses() throws ContractException {
        byte[] result = contract.evaluateTransaction(REGULATOR, "selectAllLicenses");
        if (result != null) {
            String licensesStr = new String(result, UTF_8);
            Type type = new TypeToken<List<License>>(){}.getType();
            return gson.fromJson(licensesStr, type);
        } else {
            return null;
        }
    }

    public License getLicenseById(String licenseId) throws ContractException {
        byte[] result = contract.evaluateTransaction(REGULATOR, "selectLicenseById", licenseId);
        if (result != null) {
            String licenseStr = new String(result, UTF_8);
            return gson.fromJson(licenseStr, License.class);
        } else {
            return null;
        }
    }

    public License releaseLicense(License license) throws ContractException, InterruptedException, TimeoutException {
        license.setLicenseId(UUIDUtils.simpleUUID());
        String licenseStr = gson.toJson(license, License.class);
        byte[] result = contract.submitTransaction(REGULATOR, "insertLicense", licenseStr);
        licenseStr = new String(result, UTF_8);
        return gson.fromJson(licenseStr, License.class);
    }

    public License updateLicense(License license) throws ContractException, InterruptedException, TimeoutException {
        String licenseStr = gson.toJson(license, License.class);
        byte[] result = contract.submitTransaction(REGULATOR, "updateLicense", licenseStr);
        licenseStr = new String(result, UTF_8);
        return gson.fromJson(licenseStr, License.class);
    }

    public String removeLicense(String licenseId) throws ContractException, InterruptedException, TimeoutException {
        byte[] result = contract.submitTransaction(REGULATOR, "deleteLicense", licenseId);
        if (result != null) {
            return new String(result, UTF_8);
        } else {
            return null;
        }
    }

    public List<Supplies> getAllSupplies() throws ContractException {
        byte[] result = contract.evaluateTransaction(REGULATOR, "selectAllSupplies");
        if (result != null) {
            String suppliesStr = new String(result, UTF_8);
            Type type = new TypeToken<List<Supplies>>(){}.getType();
            return gson.fromJson(suppliesStr, type);
        } else {
            return null;
        }
    }

    public Supplies getSuppliesById(String id) throws ContractException {
        byte[] result = contract.evaluateTransaction(REGULATOR, "selectSuppliesById", id);
        if (result != null) {
            String suppliesStr = new String(result, UTF_8);
            return gson.fromJson(suppliesStr, Supplies.class);
        } else {
            return null;
        }
    }

    public Supplies getSuppliesByName(String name) throws ContractException {
        byte[] result = contract.evaluateTransaction(REGULATOR, "selectSuppliesByName", name);
        if (result != null) {
            String suppliesStr = new String(result, UTF_8);
            return gson.fromJson(suppliesStr, Supplies.class);
        } else {
            return null;
        }
    }

    public Supplies releaseSupplies(Supplies supplies) throws ContractException, InterruptedException, TimeoutException {
        supplies.setId(UUIDUtils.simpleUUID());
        String suppliesStr = gson.toJson(supplies, Supplies.class);
        byte[] result = contract.submitTransaction(REGULATOR, "insertSupplies", suppliesStr);
        suppliesStr = new String(result, UTF_8);
        return gson.fromJson(suppliesStr, Supplies.class);
    }

    public Supplies updateSupplies(Supplies supplies) throws ContractException, InterruptedException, TimeoutException {
        String suppliesStr = gson.toJson(supplies, Supplies.class);
        byte[] result = contract.submitTransaction(REGULATOR, "updateSupplies", suppliesStr);
        suppliesStr = new String(result, UTF_8);
        return gson.fromJson(suppliesStr, Supplies.class);
    }

    public String removeSupplies(String id) throws ContractException, InterruptedException, TimeoutException {
        byte[] result = contract.submitTransaction(REGULATOR, "deleteSupplies", id);
        if (result != null) {
            return new String(result, UTF_8);
        } else {
            return null;
        }
    }

    public Boolean checkLicenseNotUsedById(String licenseId) throws ContractException {
        List<Order> orders = orderService.getAllOrders(contract);
        if (orders != null) {
            return orders.stream().noneMatch(each -> each.getLicenseId().equals(licenseId)
                    && !each.getStatus().equals(OrderStatus.REQUESTED));
        } else {
            return true;
        }
    }

    public Boolean checkSuppliesNotUsedById(String suppliesId) throws ContractException {
        List<Order> orders = orderService.getAllOrders(contract);
        if (orders != null) {
            return orders.stream().noneMatch(each -> each.getSuppliesUnit().getSupplies().getId().equals(suppliesId));
        } else {
            return true;
        }
    }
}
