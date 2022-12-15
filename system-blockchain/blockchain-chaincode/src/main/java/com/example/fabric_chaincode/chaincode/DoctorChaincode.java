package com.example.fabric_chaincode.chaincode;

import com.example.fabric_chaincode.model.TxRecord;
import com.example.fabric_chaincode.model.doctor.Prescription;
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

public class DoctorChaincode {

    private static final Type PRESCRIPTION_TYPE = new TypeToken<List<Prescription>>(){}.getType();

    private static final String PRESCRIPTION_KEY = "prescriptions";

    Gson gson = new Gson();

    public Response selectAllPrescriptions(ChaincodeStub stub, List<String> params) {
        String resultsStr = stub.getStringState(PRESCRIPTION_KEY);
        byte[] payload = null;
        if (!resultsStr.isEmpty()) {
            payload = resultsStr.getBytes(UTF_8);
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectPrescriptionById(ChaincodeStub stub, List<String> params) {
        String prescriptionId = params.get(1);

        String prescriptionsStr = stub.getStringState(PRESCRIPTION_KEY);
        byte[] payload = null;
        if (!prescriptionsStr.isEmpty()) {
            List<Prescription> prescriptions = gson.fromJson(prescriptionsStr, PRESCRIPTION_TYPE);
            for (Prescription prescription : prescriptions) {
                if (prescription.getPrescriptionId().equals(prescriptionId)) {
                    String prescriptionStr = gson.toJson(prescription, Prescription.class);
                    payload = prescriptionStr.getBytes(UTF_8);
                    break;
                }
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response insertPrescription(ChaincodeStub stub, List<String> params) {
        String prescriptionStr = params.get(1);
        Prescription prescription = gson.fromJson(prescriptionStr, Prescription.class);
        prescription.setTxRecord(new TxRecord(stub.getTxId(), stub.getTxTimestamp().toString()));

        String prescriptionsStr = stub.getStringState(PRESCRIPTION_KEY);
        List<Prescription> prescriptions = (!prescriptionsStr.isEmpty()) ? gson.fromJson(prescriptionsStr, PRESCRIPTION_TYPE)
                                                                            : new LinkedList<>();
        prescriptions.add(prescription);
        prescriptionsStr = gson.toJson(prescriptions, PRESCRIPTION_TYPE);
        stub.putStringState(PRESCRIPTION_KEY, prescriptionsStr);

        prescriptionStr = gson.toJson(prescription, Prescription.class);
        byte[] payload = prescriptionStr.getBytes(UTF_8);
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response deletePrescription(ChaincodeStub stub, List<String> params) {
        String prescriptionId = params.get(1);

        String prescriptionsStr = stub.getStringState(PRESCRIPTION_KEY);
        List<Prescription> prescriptions = gson.fromJson(prescriptionsStr, PRESCRIPTION_TYPE);
        ListIterator<Prescription> iterator = prescriptions.listIterator();
        byte[] payload = null;
        while (iterator.hasNext()) {
            Prescription each = iterator.next();
            if (each.getPrescriptionId().equals(prescriptionId)) {
                iterator.remove();
                payload = prescriptionId.getBytes(UTF_8);
                break;
            }
        }
        prescriptionsStr = gson.toJson(prescriptions, PRESCRIPTION_TYPE);
        stub.putStringState(PRESCRIPTION_KEY, prescriptionsStr);

        return ResponseUtils.newSuccessResponse(payload);
    }
}
