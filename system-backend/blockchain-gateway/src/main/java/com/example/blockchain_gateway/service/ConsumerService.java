package com.example.blockchain_gateway.service;

import com.example.blockchain_gateway.model.consumer.TraceTask;
import com.example.blockchain_gateway.model.distributor.Distribution;
import com.example.blockchain_gateway.model.manufacturer.Process;
import com.example.blockchain_gateway.model.order.Order;
import com.example.blockchain_gateway.model.provider.Sale;
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
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static com.example.blockchain_gateway.domain.DeptType.CONSUMER;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class ConsumerService {

    @Resource(name = "consumerContract")
    private Contract contract;

    @Autowired
    private Gson gson;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RegulatorService regulatorService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private DistributorService distributorService;

    public TraceTask traceSupplies(Sale sale) throws ContractException, InterruptedException, TimeoutException {
        List<TraceTask> traceTasks = getAllTraceTasks();
        if (traceTasks != null) {
            Optional<TraceTask> result = traceTasks.stream().filter(each -> each.getSale().getSaleId().equals(sale.getSaleId())).findFirst();
            if (result.isPresent()) {
                TraceTask traceTask = result.get();
                initTraceTask(traceTask, sale);
                return traceTask;
            }
        }
        TraceTask traceTask = new TraceTask(sale.getUsername(), sale);
        traceTask.setTraceId(UUIDUtils.fastUUID());
        traceTask = uploadTraceTask(traceTask);
        if (traceTask != null) {
            initTraceTask(traceTask, sale);
        }
        return traceTask;
    }

    public void initTraceTask(TraceTask traceTask, Sale sale) throws ContractException {
        String suppliesId = sale.getSuppliesUnit().getSupplies().getId();
        Supplies supplies = regulatorService.getSuppliesById(suppliesId);
        if (supplies == null) {
            List<Supplies> suppliesList = regulatorService.getAllSupplies();
            if (suppliesList != null) {
                for (Supplies each : suppliesList) {
                    if (each.getName().equals(suppliesId)) {
                        supplies = each;
                        break;
                    }
                }
            }
        }

        Distribution distribution = distributorService.getDistributionById(sale.getDistributionId());
        Order order = orderService.getOrderById(sale.getOrderId(), contract);
        List<Process> processes = manufacturerService.getProcessesByOrderId(order.getOrderId());

        String licenseId = order.getLicenseId();
        License license = regulatorService.getLicenseById(licenseId);
        if (license == null) {
            List<License> licenses = regulatorService.getAllLicenses();
            if (licenses != null) {
                for (License each : licenses) {
                    if (each.getManufacturerName().equals(order.getManufacturerName())
                            && each.getSuppliesId().equals(order.getSuppliesUnit().getSupplies().getId())) {
                        license = each;
                        break;
                    }
                }
            }
        }

        traceTask.setSale(sale);
        traceTask.setSupplies(supplies);
        traceTask.setLicense(license);
        traceTask.setDistribution(distribution);
        traceTask.setOrder(order);
        traceTask.setProcesses(processes);
    }

    public List<TraceTask> getAllTraceTasks() throws ContractException {
        byte[] result = contract.evaluateTransaction(CONSUMER, "selectAllTraceTasks");
        if (result != null) {
            String traceTasksStr = new String(result, UTF_8);
            Type type = new TypeToken<List<TraceTask>>(){}.getType();
            return gson.fromJson(traceTasksStr, type);
        } else {
            return null;
        }
    }

    public TraceTask getTraceTaskById(String traceId) throws ContractException {
        byte[] result = contract.evaluateTransaction(CONSUMER, "selectTraceTaskById", traceId);
        if (result != null) {
            String traceTaskStr = new String(result, UTF_8);
            return gson.fromJson(traceTaskStr, TraceTask.class);
        } else {
            return null;
        }
    }

    private TraceTask uploadTraceTask(TraceTask traceTask) throws ContractException, InterruptedException, TimeoutException {
        traceTask.setTraceId(UUIDUtils.fastUUID());
        String traceTaskStr = gson.toJson(traceTask, TraceTask.class);
        byte[] result = contract.submitTransaction(CONSUMER, "insertTraceTask", traceTaskStr);
        if (result != null) {
            traceTaskStr = new String(result, UTF_8);
            return gson.fromJson(traceTaskStr, TraceTask.class);
        } else {
            return null;
        }
    }
}
