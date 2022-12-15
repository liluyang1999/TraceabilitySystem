package com.example.fabric_chaincode.chaincode;

import com.example.fabric_chaincode.model.consumer.TraceTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hyperledger.fabric.shim.Chaincode.Response;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ConsumerChaincode {

    private static final Type TRACE_TASK_TYPE = new TypeToken<List<TraceTask>>(){}.getType();

    private static final String TRACE_TASK_KEY = "traceTasks";

    Gson gson = new Gson();

    public Response selectAllTraceTasks(ChaincodeStub stub, List<String> params) {
        String traceTasksStr = stub.getStringState(TRACE_TASK_KEY);
        byte[] payload = null;
        if (!traceTasksStr.isEmpty()) {
            payload = traceTasksStr.getBytes(UTF_8);
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response selectTraceTaskById(ChaincodeStub stub, List<String> params) {
        String traceId = params.get(1);

        String traceTasksStr = stub.getStringState(TRACE_TASK_KEY);
        byte[] payload = null;
        if (!traceTasksStr.isEmpty()) {
            List<TraceTask> traceTasks = gson.fromJson(traceTasksStr, TRACE_TASK_TYPE);
            for (TraceTask traceTask : traceTasks) {
                if (traceTask.getTraceId().equals(traceId)) {
                    String traceTaskStr = gson.toJson(traceTask, TraceTask.class);
                    payload = traceTaskStr.getBytes(UTF_8);
                }
            }
        }
        return ResponseUtils.newSuccessResponse(payload);
    }

    public Response insertTraceTask(ChaincodeStub stub, List<String> params) {
        String traceTaskStr = params.get(1);
        TraceTask traceTask = gson.fromJson(traceTaskStr, TraceTask.class);

        String traceTasksStr = stub.getStringState(TRACE_TASK_KEY);
        List<TraceTask> traceTasks = (!traceTasksStr.isEmpty()) ? gson.fromJson(traceTasksStr, TRACE_TASK_TYPE)
                                                                    : new LinkedList<>();
        traceTasks.add(traceTask);
        traceTasksStr = gson.toJson(traceTasks, TRACE_TASK_TYPE);
        stub.putStringState(TRACE_TASK_KEY, traceTasksStr);

        traceTaskStr = gson.toJson(traceTask, TraceTask.class);
        byte[] payload = traceTaskStr.getBytes(UTF_8);
        return ResponseUtils.newSuccessResponse(payload);
    }
}
