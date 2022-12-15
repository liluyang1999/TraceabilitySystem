package com.example.blockchain_gateway.controller;

import com.example.blockchain_gateway.domain.AjaxResult;
import com.example.blockchain_gateway.model.consumer.TraceTask;
import com.example.blockchain_gateway.model.provider.Sale;
import com.example.blockchain_gateway.service.ConsumerService;
import com.example.blockchain_gateway.service.ProviderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/blockchain/consumer")
@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ProviderService providerService;

    @RequestMapping("/traceSupplies")
    public AjaxResult traceSupplies(@RequestBody Sale sale) {
        System.out.println("Trace Supplies: " + sale.toString());
        try {
            if (!StringUtils.isEmpty(sale.getSaleId())) {
                sale = providerService.getSaleById(sale.getSaleId());
                if (sale != null) {
                    TraceTask traceTask = consumerService.traceSupplies(sale);
                    if (traceTask != null) {
                        return AjaxResult.success("Trace supplies successfully", traceTask);
                    }
                }
            }
            return AjaxResult.error("Failed to trace supplies");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/getAllTraceTasks")
    public AjaxResult getAllTraceTasks() {
        try {
            List<TraceTask> traceTasks = consumerService.getAllTraceTasks();
            return AjaxResult.success("Get all trace tasks successfully", traceTasks);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }


    @RequestMapping("/getTraceTaskById")
    public AjaxResult getTraceTaskById(@RequestParam("traceId") String traceId) {
        try {
            TraceTask traceTask = consumerService.getTraceTaskById(traceId);
            return AjaxResult.success("Get trace task by id successfully", traceTask);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
