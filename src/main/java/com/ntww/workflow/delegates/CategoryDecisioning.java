package com.ntww.workflow.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.logging.Logger;

@Component
public class CategoryDecisioning implements JavaDelegate {
    static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String loanAmount = "0.0";
        String category = "Gold";

        loanAmount = (String) delegateExecution.getVariable("loanAmount");
        logger.info("Applied Loan Amount " + loanAmount + " by Customer: "+delegateExecution.getVariable("customerName"));

        double appliedLoanAmountDouble = Double.parseDouble(loanAmount);

        if (appliedLoanAmountDouble >= 10000) {
            category = "Titanium";
        } else if (appliedLoanAmountDouble >= 5000) {
            category = "Platinum";
        }

        delegateExecution.setVariable("category", category);

    }
}
