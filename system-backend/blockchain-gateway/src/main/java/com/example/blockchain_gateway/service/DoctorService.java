package com.example.blockchain_gateway.service;

import com.example.blockchain_gateway.model.doctor.Prescription;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.example.blockchain_gateway.domain.DeptType.DOCTOR;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class DoctorService {

    @Resource(name = "providerContract")
    private Contract contract;

    @Autowired
    private Gson gson;

    @Autowired
    private ProviderService providerService;

    public List<Prescription> getAllPrescriptions() throws ContractException {
        byte[] result = contract.evaluateTransaction(DOCTOR, "selectAllPrescriptions");
        if (result != null) {
            String prescriptionsStr = new String(result, UTF_8);
            Type type = new TypeToken<List<Prescription>>(){}.getType();
            return gson.fromJson(prescriptionsStr, type);
        } else {
            return null;
        }
    }

    public Prescription getPrescriptionById(String prescriptionId) throws ContractException {
        byte[] result = contract.evaluateTransaction(DOCTOR, "selectPrescriptionById", prescriptionId);
        if (result != null) {
            String prescriptionStr = new String(result, UTF_8);
            return gson.fromJson(prescriptionStr, Prescription.class);
        } else {
            return null;
        }
    }

    public Prescription releasePrescription(Prescription prescription) throws ContractException, InterruptedException, TimeoutException {
        prescription.setPrescriptionId(UUIDUtils.fastUUID());
        prescription.setReleaseDate(DateUtils.getCurrentDateTime());
        String prescriptionStr = gson.toJson(prescription, Prescription.class);
        byte[] result = contract.submitTransaction(DOCTOR, "insertPrescription", prescriptionStr);
        if (result != null) {
            prescriptionStr = new String(result, UTF_8);
            return gson.fromJson(prescriptionStr, Prescription.class);
        } else {
            return null;
        }
    }

    public String removePrescription(String prescriptionId) throws ContractException, InterruptedException, TimeoutException {
        byte[] result = contract.submitTransaction(DOCTOR, "deletePrescription", prescriptionId);
        if (result != null) {
            return new String(result, UTF_8);
        } else {
            return null;
        }
    }

    public Boolean checkPrescriptionNotUsed(Prescription prescription) throws ContractException {
        List<Sale> sales = providerService.getAllSales();
        Date releaseDate = DateUtils.parseDateStr(prescription.getReleaseDate());
        String suppliesId = prescription.getSuppliesUnit().getSupplies().getId();
        if (sales != null) {
            return sales.stream().noneMatch(each -> each.getUsername().equals(prescription.getPatientUsername())
                                                    && each.getSuppliesUnit().getSupplies().getId().equals(suppliesId)
                                                    && DateUtils.parseDateStr(each.getPurchaseDate()).after(releaseDate));
        } else {
            return true;
        }
    }
}
