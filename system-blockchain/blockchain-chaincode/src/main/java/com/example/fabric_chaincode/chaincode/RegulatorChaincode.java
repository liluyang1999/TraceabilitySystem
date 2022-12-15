package com.example.fabric_chaincode.chaincode;

import com.example.fabric_chaincode.model.TxRecord;
import com.example.fabric_chaincode.model.regulator.License;
import com.example.fabric_chaincode.model.supplies.Supplies;
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

public class RegulatorChaincode {

    private static final Type SUPPLIES_TYPE = new TypeToken<List<Supplies>>(){}.getType();

    private static final Type LICENSE_TYPE = new TypeToken<List<License>>(){}.getType();

    private static final String SUPPLIES_KEY = "supplies";

    private static final String LICENSE_KEY = "licenses";

    Gson gson = new Gson();

    public Response selectAllSupplies(ChaincodeStub stub, List<String> params) {
        String resultsStr = stub.getStringState(SUPPLIES_KEY);
        byte[] payload = null;
        if (!resultsStr.isEmpty()) {
            payload = resultsStr.getBytes(UTF_8);
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectSuppliesById(ChaincodeStub stub, List<String> params) {
        String id = params.get(1);
        String allSuppliesStr = stub.getStringState(SUPPLIES_KEY);
        byte[] payload = null;
        if (!allSuppliesStr.isEmpty()) {
            List<Supplies> allSupplies = gson.fromJson(allSuppliesStr, SUPPLIES_TYPE);
            for (Supplies supplies : allSupplies) {
                if (supplies.getId().equals(id)) {
                    payload = gson.toJson(supplies, Supplies.class).getBytes(UTF_8);
                    break;
                }
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectSuppliesByName(ChaincodeStub stub, List<String> params) {
        String name = params.get(1);
        String allSuppliesStr = stub.getStringState(SUPPLIES_KEY);
        byte[] payload = null;
        if (!allSuppliesStr.isEmpty()) {
            List<Supplies> allSupplies = gson.fromJson(allSuppliesStr, SUPPLIES_TYPE);
            for (Supplies supplies : allSupplies) {
                if (supplies.getName().equals(name)) {
                    payload = gson.toJson(supplies, Supplies.class).getBytes(UTF_8);
                    break;
                }
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response insertSupplies(ChaincodeStub stub, List<String> params) {
        String suppliesStr = params.get(1);
        Supplies supplies = gson.fromJson(suppliesStr, Supplies.class);
        supplies.setTxRecord(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String allSuppliesStr = stub.getStringState(SUPPLIES_KEY);
        List<Supplies> allSupplies = (!allSuppliesStr.isEmpty()) ? gson.fromJson(allSuppliesStr, SUPPLIES_TYPE)
                                                    : new LinkedList<>();
        allSupplies.add(supplies);
        allSuppliesStr = gson.toJson(allSupplies, SUPPLIES_TYPE);
        stub.putStringState(SUPPLIES_KEY, allSuppliesStr);

        suppliesStr = gson.toJson(supplies, Supplies.class);
        byte[] payload = suppliesStr.getBytes(UTF_8);
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response updateSupplies(ChaincodeStub stub, List<String> params) {
        String suppliesStr = params.get(1);
        Supplies supplies = gson.fromJson(suppliesStr, Supplies.class);
        supplies.setTxRecord(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String allSuppliesStr = stub.getStringState(SUPPLIES_KEY);
        List<Supplies> allSupplies = gson.fromJson(allSuppliesStr, SUPPLIES_TYPE);
        ListIterator<Supplies> iterator = allSupplies.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            Supplies each = iterator.next();
            if (each.getId().equals(supplies.getId())) {
                iterator.set(supplies);
                suppliesStr = gson.toJson(supplies, Supplies.class);
                payload = suppliesStr.getBytes(UTF_8);
                break;
            }
        }
        allSuppliesStr = gson.toJson(allSupplies, SUPPLIES_TYPE);
        stub.putStringState(SUPPLIES_KEY, allSuppliesStr);

        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response deleteSupplies(ChaincodeStub stub, List<String> params) {
        String suppliesId = params.get(1);

        String allSuppliesStr = stub.getStringState(SUPPLIES_KEY);
        List<Supplies> allSupplies = gson.fromJson(allSuppliesStr, SUPPLIES_TYPE);
        ListIterator<Supplies> iterator = allSupplies.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            Supplies each = iterator.next();
            if (each.getId().equals(suppliesId)) {
                iterator.remove();
                payload = suppliesId.getBytes(UTF_8);
                break;
            }
        }
        allSuppliesStr = gson.toJson(allSupplies, SUPPLIES_TYPE);
        stub.putStringState(SUPPLIES_KEY, allSuppliesStr);

        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectAllLicenses(ChaincodeStub stub, List<String> params) {
        String licensesStr = stub.getStringState(LICENSE_KEY);
        byte[] payload = null;
        if (!licensesStr.isEmpty()) {
            payload = licensesStr.getBytes(UTF_8);
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectLicenseById(ChaincodeStub stub, List<String> params) {
        String licenseId = params.get(1);
        String licensesStr = stub.getStringState(LICENSE_KEY);
        byte[] payload = null;
        if (!licensesStr.isEmpty()) {
            List<License> licenses = gson.fromJson(licensesStr, LICENSE_TYPE);
            for (License license : licenses) {
                if (license.getLicenseId().equals(licenseId)) {
                    payload = gson.toJson(license, License.class).getBytes(UTF_8);
                    break;
                }
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response insertLicense(ChaincodeStub stub, List<String> params) {
        String licenseStr = params.get(1);
        License license = gson.fromJson(licenseStr, License.class);
        license.setTxRecord(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String licensesStr = stub.getStringState(LICENSE_KEY);
        List<License> licenses = (!licensesStr.isEmpty()) ? gson.fromJson(licensesStr, LICENSE_TYPE)
                                                            : new LinkedList<>();
        licenses.add(license);
        licensesStr = gson.toJson(licenses, LICENSE_TYPE);
        stub.putStringState(LICENSE_KEY, licensesStr);

        licenseStr = gson.toJson(license, License.class);
        byte[] payload = licenseStr.getBytes(UTF_8);
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response updateLicense(ChaincodeStub stub, List<String> params) {
        String licenseStr = params.get(1);
        License license = gson.fromJson(licenseStr, License.class);
        license.setTxRecord(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String licensesStr = stub.getStringState(LICENSE_KEY);
        List<License> licenses = gson.fromJson(licensesStr, LICENSE_TYPE);
        ListIterator<License> iterator = licenses.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            License each = iterator.next();
            if (each.getLicenseId().equals(license.getLicenseId())) {
                iterator.set(license);
                licenseStr = gson.toJson(license, License.class);
                payload = licenseStr.getBytes(UTF_8);
                break;
            }
        }
        licensesStr = gson.toJson(licenses, LICENSE_TYPE);
        stub.putStringState(LICENSE_KEY, licensesStr);

        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response deleteLicense(ChaincodeStub stub, List<String> params) {
        String licenseId = params.get(1);

        String licensesStr = stub.getStringState(LICENSE_KEY);
        List<License> licenses = gson.fromJson(licensesStr, LICENSE_TYPE);
        ListIterator<License> iterator = licenses.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            License each = iterator.next();
            if (each.getLicenseId().equals(licenseId)) {
                iterator.remove();
                payload = licenseId.getBytes(UTF_8);
                break;
            }
        }
        licensesStr = gson.toJson(licenses, LICENSE_TYPE);
        stub.putStringState(LICENSE_KEY, licensesStr);

        return ResponseUtils.newSuccessResponse(payload);
    }
}
