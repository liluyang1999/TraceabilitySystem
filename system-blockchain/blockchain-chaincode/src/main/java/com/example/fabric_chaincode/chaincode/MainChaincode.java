package com.example.fabric_chaincode.chaincode;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.example.fabric_chaincode.chaincode.ChaincodeType.*;

public class MainChaincode extends ChaincodeBase {

    private static final Logger logger = LoggerFactory.getLogger(MainChaincode.class);

    public static void main(String[] args) {
        new MainChaincode().start(args);
    }

    @Override
    public Response init(ChaincodeStub stub) {
        String func = stub.getFunction();
        if (!func.equals("init")) {
            return ResponseUtils.newErrorResponse("Function other than init is not supported......");
        }
        return ResponseUtils.newSuccessResponse("Init function successfully......");
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        logger.info("Invoke java chaincode......");
        String category = stub.getFunction();
        List<String> params = stub.getParameters();
        switch (category) {
            case REGULATOR : return checkRegulator(stub, params);
            case MANUFACTURER : return checkManufacturer(stub, params);
            case DISTRIBUTOR : return checkDistributor(stub, params);
            case PROVIDER : return checkProvider(stub, params);
            case CONSUMER : return checkConsumer(stub, params);
            case DOCTOR : return checkDoctor(stub, params);
            default : return checkOrder(stub, params);
        }
    }

    private Response checkRegulator(ChaincodeStub stub, List<String> params) {
        logger.info("Invoke regulator chaincode......");
        RegulatorChaincode regulator = new RegulatorChaincode();
        String method = params.get(0);
        switch (method) {
            case "selectAllLicenses" : return regulator.selectAllLicenses(stub, params);
            case "selectLicenseById" : return regulator.selectLicenseById(stub, params);
            case "insertLicense" : return regulator.insertLicense(stub, params);
            case "updateLicense" : return regulator.updateLicense(stub, params);
            case "deleteLicense" : return regulator.deleteLicense(stub, params);
            case "selectSuppliesById" : return regulator.selectSuppliesById(stub, params);
            case "selectSuppliesByName" : return regulator.selectSuppliesByName(stub, params);
            case "insertSupplies" : return regulator.insertSupplies(stub, params);
            case "updateSupplies" : return regulator.updateSupplies(stub, params);
            case "deleteSupplies" : return regulator.deleteSupplies(stub, params);
            default: return regulator.selectAllSupplies(stub, params);
        }
    }

    private Response checkOrder(ChaincodeStub stub, List<String> params) {
        logger.info("Invoke order chaincode......");
        OrderChaincode order = new OrderChaincode();
        String method = params.get(0);
        switch (method) {
            case "selectOrdersByStatus" : return order.selectOrdersByStatus(stub, params);
            case "selectOrderById" : return order.selectOrderById(stub, params);
            case "insertOrder" : return order.insertOrder(stub, params);
            case "updateOrder" : return order.updateOrder(stub, params);
            case "deleteOrder" : return order.deleteOrder(stub, params);
            default: return order.selectAllOrders(stub, params);
        }
    }

    private Response checkManufacturer(ChaincodeStub stub, List<String> params) {
        logger.info("Invoke manufacturer chaincode......");
        ManufacturerChaincode manufacturer = new ManufacturerChaincode();
        String method = params.get(0);
        switch (method) {
            case "selectProcessesByOrderId" : return manufacturer.selectProcessesByOrderId(stub, params);
            case "selectProcessById" : return manufacturer.selectProcessById(stub, params);
            case "insertProcess" : return manufacturer.insertProcess(stub, params);
            case "updateProcess" : return manufacturer.updateProcess(stub, params);
            default : return manufacturer.selectAllProcesses(stub, params);
        }
    }

    private Response checkDistributor(ChaincodeStub stub, List<String> params) {
        logger.info("Invoke distributor chaincode......");
        DistributorChaincode distributor = new DistributorChaincode();
        String method = params.get(0);
        switch (method) {
            case "selectDistributionsByStatus" : return distributor.selectDistributionsByStatus(stub, params);
            case "selectDistributionById" : return distributor.selectDistributionById(stub, params);
            case "insertDistribution" : return distributor.insertDistribution(stub, params);
            case "updateDistribution" : return distributor.updateDistribution(stub, params);
            case "deleteDistribution" : return distributor.deleteDistribution(stub, params);
            case "selectAllInventories" : return distributor.selectAllInventories(stub, params);
            case "insertInventory" : return distributor.insertInventory(stub, params);
            case "updateInventory" : return distributor.updateInventory(stub, params);
            default : return distributor.selectAllDistributions(stub, params);
        }
    }

    private Response checkProvider(ChaincodeStub stub, List<String> params) {
        logger.info("Invoke provider chaincode......");
        ProviderChaincode provider = new ProviderChaincode();
        String method = params.get(0);
        switch (method) {
            case "selectSalesByOrderId" : return provider.selectSalesByOrderId(stub, params);
            case "selectSalesByUsername" : return provider.selectSalesByUsername(stub, params);
            case "selectSaleById" : return provider.selectSaleById(stub, params);
            case "insertSale" : return provider.insertSale(stub, params);
            case "updateSale" : return provider.updateSale(stub, params);
            case "deleteSale" : return provider.deleteSale(stub, params);
            default : return provider.selectAllSales(stub, params);
        }
    }

    private Response checkConsumer(ChaincodeStub stub, List<String> params) {
        logger.info("Invoke consumer chaincode......");
        ConsumerChaincode consumer = new ConsumerChaincode();
        String method = params.get(0);
        switch (method) {
            case "selectTraceTaskById" : return consumer.selectTraceTaskById(stub, params);
            case "insertTraceTask": return consumer.insertTraceTask(stub, params);
            default: return consumer.selectAllTraceTasks(stub, params);
        }
    }

    private Response checkDoctor(ChaincodeStub stub, List<String> params) {
        logger.info("Invoke doctor chaincode......");
        DoctorChaincode doctor = new DoctorChaincode();
        String method = params.get(0);
        switch (method) {
            case "selectPrescriptionById" : return doctor.selectPrescriptionById(stub, params);
            case "insertPrescription" : return doctor.insertPrescription(stub, params);
            case "deletePrescription" : return doctor.deletePrescription(stub, params);
            default : return doctor.selectAllPrescriptions(stub, params);
        }
    }
}
