package com.ntww.workflow.service;

import com.ntww.workflow.constants.WorkflowConstants;
import com.ntww.workflow.model.InitiatizationResponse;
import com.ntww.workflow.model.NCRRequest;
import com.ntww.workflow.model.TaskInfo;
import com.ntww.workflow.model.UserTaskResponse;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.spring.boot.starter.CamundaBpmNestedRuntimeException;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ApplicationService {
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    RepositoryService repositoryService;

    // Create Process Instance for NCR Workflow and return response as updated status and pending task(s).
    public InitiatizationResponse initiateWorkflow(NCRRequest request) throws Exception {
        Logger logger;
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        logger.info("Initializing the request for Process - NCR Workflow");

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("ncrRequestId", request.getRequestId());
        variables.put("status", request.getStatus());
        InitiatizationResponse output = new InitiatizationResponse();

        try{
            ProcessInstance processInstance =
                    runtimeService.startProcessInstanceByKey(WorkflowConstants.NCR_WORKFLOW_DEFINITION_KEY, variables) ;

            // Update Status Value from Drafted to Request Submitted
            Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getProcessInstanceId())
                    .singleResult();
            runtimeService.setVariable(execution.getId(), "status", WorkflowConstants.STATUS_AFTER_PROCESS_CREATION);

            output = mapperInitiateRequest(processInstance);
            logger.info("Process Instance Id-" + processInstance.getProcessInstanceId() + " is created successfully!");
        }
        catch (Exception e) { //
            e.printStackTrace();
            System.out.println("Error while creating process instance id !!!");
        }
        return output;
    }

    public InitiatizationResponse mapperInitiateRequest(ProcessInstance processInstance){
        InitiatizationResponse output = new InitiatizationResponse();

        Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getProcessInstanceId())
                .singleResult();

        output.setProcessInstanceId(processInstance.getProcessInstanceId());
        output.setTaskId(processEngine.getTaskService().createTaskQuery().active().list().get(0).getId());

        output.setStatus((String) runtimeService.getVariable(execution.getId(),"status"));
        output.setRequestId((String) runtimeService.getVariable(execution.getId(),"ncrRequestId"));

        return output;
    }

    // Complete User Task and return response with the updated status in Workflow
    public UserTaskResponse completeUserTask(TaskInfo taskInfo)  throws CamundaBpmNestedRuntimeException {

        String userAction = taskInfo.getUserOutcome();
        String taskId = taskInfo.getTaskId();
        String taskName = "";
        UserTaskResponse output = new UserTaskResponse();
        Map<String, Object> variables = new HashMap<String, Object>();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(taskInfo.getProcessInstanceId())
                .singleResult();
        try {
            if(StringUtils.isNullOrEmpty(taskId)){
            // Get TaskId from Process Engine basis Process Instance Id
                taskId = processEngine.getTaskService().createTaskQuery().active().list().get(0).getId();
            }
            taskName = processEngine.getTaskService().createTaskQuery().active().list().get(0).getName();

            variables.put("outcome", userAction);
            taskService.complete(taskId, variables);
            System.out.println("Task is completed successfully: " + taskId +" having taskName: "+taskName);

            // Update Status in Workfow as per User Task and Action
            String updatedStatus = updateWorkflowStatus(execution,taskName, userAction);

            output.setStatus(updatedStatus);
            output.setProcessInstanceId(taskInfo.getProcessInstanceId());
            output.setRequestId((String) runtimeService.getVariable(execution.getId(), "requestId"));
        }
        catch (Exception e) { //
            e.printStackTrace();
            System.out.println("Error while completing user task !!!"+taskId);
        }

        return output;
    }


    private String updateWorkflowStatus(Execution execution, String taskName, String userAction){

        String updatedStatus = "";
        // Vendor Team Lead Review
        if(taskName.equalsIgnoreCase("Vendor Team Lead Review") && userAction.equalsIgnoreCase("rejected")){
            runtimeService.setVariable(execution.getId(), "status", WorkflowConstants.STATUS_REJECTED_VENDOR_TL);
        }
        else if(taskName.equalsIgnoreCase("Vendor Team Lead Review") && userAction.equalsIgnoreCase("accepted")){
            runtimeService.setVariable(execution.getId(), "status", WorkflowConstants.STATUS_ACCEPTED_VENDOR_TL);
        }
        // STC Team Lead Review
        else if(taskName.equalsIgnoreCase("STC Team Lead Review") && userAction.equalsIgnoreCase("rejected")){
            runtimeService.setVariable(execution.getId(), "status", WorkflowConstants.STATUS_REJECTED_STC_TL);
        }
        else if(taskName.equalsIgnoreCase("STC Team Lead Review") && userAction.equalsIgnoreCase("accepted")){
            runtimeService.setVariable(execution.getId(), "status", WorkflowConstants.STATUS_ACCEPTED_STC_TL);
        }
        // RFC Design Team Lead Review
        else if(taskName.equalsIgnoreCase("RF Design Team Lead Review") && userAction.equalsIgnoreCase("rejected")){
            runtimeService.setVariable(execution.getId(), "status", WorkflowConstants.STATUS_REJECTED_RF_DESIGN_TL);
        }
        else {
            runtimeService.setVariable(execution.getId(), "status", WorkflowConstants.STATUS_ACCEPTED_RF_DESIGN_TL);
        }

        updatedStatus = (String) runtimeService.getVariable(execution.getId(), "status");
        System.out.println("New status updated as#  : "+updatedStatus+ " !. after completing taskName---: " +taskName +" with userAction ---: " +userAction);

        return  updatedStatus;
    }


}
