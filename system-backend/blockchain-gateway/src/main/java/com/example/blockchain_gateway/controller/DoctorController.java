package com.example.blockchain_gateway.controller;

import com.example.blockchain_gateway.domain.AjaxResult;
import com.example.blockchain_gateway.model.doctor.Prescription;
import com.example.blockchain_gateway.service.DoctorService;
import com.example.blockchain_gateway.service.RestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/blockchain/doctor")
@RestController
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private RestService restService;

    @RequestMapping("/getAllPrescriptions")
    public AjaxResult getAllPrescriptions() {
        try {
            List<Prescription> prescriptions = doctorService.getAllPrescriptions();
            return AjaxResult.success("Get all prescriptions successfully", prescriptions);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/releasePrescription")
    public AjaxResult releasePrescription(@RequestBody Prescription prescription) {
        System.out.println("Release Prescription: " + prescription);
        try {
            if (prescription.getPrescriptionId() == null
                    && !StringUtils.isEmpty(prescription.getDoctorUsername())
                    && !StringUtils.isEmpty(prescription.getPatientUsername())
                    && prescription.getSuppliesUnit() != null
                    && prescription.getSuppliesUnit().getSupplies() != null
                    && !StringUtils.isEmpty(prescription.getSuppliesUnit().getQuantity())) {
                if (restService.isUserExist(prescription.getDoctorUsername())
                        && restService.isUserExist(prescription.getPatientUsername())) {
                    Prescription newPrescription = doctorService.releasePrescription(prescription);
                    if (newPrescription != null) {
                        return AjaxResult.success("Release prescription successfully");
                    }
                }
            }
            return AjaxResult.error("Failed to release prescription, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/removePrescription")
    public AjaxResult removePrescription(@RequestParam String prescriptionId) {
        System.out.println("Remove Prescription: " + prescriptionId);
        try {
            if (!StringUtils.isEmpty(prescriptionId)) {
                Prescription prescription = doctorService.getPrescriptionById(prescriptionId);
                if (prescription != null && doctorService.checkPrescriptionNotUsed(prescription)) {
                    String deleteId = doctorService.removePrescription(prescriptionId);
                    if (deleteId != null) {
                        return AjaxResult.success("Remove prescription successfully", deleteId);
                    }
                }
            }
            return AjaxResult.error("Failed to remove prescription, the patient has bought the medical supplies in the prescription");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
