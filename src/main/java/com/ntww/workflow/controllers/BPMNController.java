package com.ntww.workflow.controllers;

import com.ntww.workflow.service.ApplicationService;
import com.ntww.workflow.model.InitiatizationResponse;
import com.ntww.workflow.model.NCRRequest;
import com.ntww.workflow.model.TaskInfo;
import com.ntww.workflow.model.UserTaskResponse;
import org.camunda.bpm.spring.boot.starter.CamundaBpmNestedRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.ComponentScan;
import java.util.logging.Logger;

@RestController
@CrossOrigin
@ComponentScan("com.ntww.workflow.service")
public class BPMNController {
    static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Autowired
    ApplicationService applicationService;

    @PostMapping("/initiateNCRWorkflow")
    public InitiatizationResponse startNCRWorkflow(@RequestBody NCRRequest request)
            throws CamundaBpmNestedRuntimeException {

        InitiatizationResponse response = new InitiatizationResponse();
        try{
            response = applicationService.initiateWorkflow(request);
        }
        catch (Exception e) {
            logger.info("Error while creating process id in workflow");
        }
        return response;
    }

    @PostMapping("/completeTask")
    public UserTaskResponse completeTask(@RequestBody TaskInfo taskInfo)  throws CamundaBpmNestedRuntimeException {

        UserTaskResponse response = new UserTaskResponse();
        try {
            response = applicationService.completeUserTask(taskInfo);

        } catch (Exception e) { //
            System.out.println("Error While completing User Task...");
        }
        return response;
    }

}
