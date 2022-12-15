package com.example.blockchain_gateway.model.doctor;

import com.example.blockchain_gateway.domain.TxRecord;
import com.example.blockchain_gateway.model.supplies.SuppliesUnit;

public class Prescription {

    private String prescriptionId;

    private String releaseDate;

    private String doctorUsername;

    private String patientUsername;

    private SuppliesUnit suppliesUnit;

    private TxRecord txRecord;

    public Prescription(String prescriptionId, String releaseDate, String doctorUsername, String patientUsername, SuppliesUnit suppliesUnit, TxRecord txRecord) {
        this.prescriptionId = prescriptionId;
        this.releaseDate = releaseDate;
        this.doctorUsername = doctorUsername;
        this.patientUsername = patientUsername;
        this.suppliesUnit = suppliesUnit;
        this.txRecord = txRecord;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

    public String getPatientUsername() {
        return patientUsername;
    }

    public void setPatientUsername(String patientUsername) {
        this.patientUsername = patientUsername;
    }

    public SuppliesUnit getSuppliesUnit() {
        return suppliesUnit;
    }

    public void setSuppliesUnit(SuppliesUnit suppliesUnit) {
        this.suppliesUnit = suppliesUnit;
    }

    public TxRecord getTxRecord() {
        return txRecord;
    }

    public void setTxRecord(TxRecord txRecord) {
        this.txRecord = txRecord;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId='" + prescriptionId + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", doctorUsername='" + doctorUsername + '\'' +
                ", patientUsername='" + patientUsername + '\'' +
                ", suppliesUnit=" + suppliesUnit +
                ", txRecord=" + txRecord +
                '}';
    }
}
