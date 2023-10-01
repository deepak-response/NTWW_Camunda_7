package com.ntww.workflow.model;

import java.util.Date;
public class TaskInfo {

    private String taskId;
    private String userOutcome;
    private Date createdDate;
    private Date lastUpdatedDate;
    private Date dueDate;
    private Date claimTime;
    private String taskDefId;
    private String taskName;
    private String assignee;
    private String processInstanceId;
    private String procDefinitionId;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserOutcome() {
        return userOutcome;
    }

    public void setUserOutcome(String userOutcome) {
        this.userOutcome = userOutcome;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getClaimTime() {
        return claimTime;
    }

    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }

    public String getTaskDefId() {
        return taskDefId;
    }

    public void setTaskDefId(String taskDefId) {
        this.taskDefId = taskDefId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcDefinitionId() {
        return procDefinitionId;
    }

    public void setProcDefinitionId(String procDefinitionId) {
        this.procDefinitionId = procDefinitionId;
    }
}