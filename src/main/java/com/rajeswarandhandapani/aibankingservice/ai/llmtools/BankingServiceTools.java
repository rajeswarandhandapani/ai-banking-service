package com.rajeswarandhandapani.aibankingservice.ai.llmtools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import com.rajeswarandhandapani.aibankingservice.services.BankingClient;

@Service
public class BankingServiceTools {

    private final BankingClient bankingClient;

    public BankingServiceTools(BankingClient bankingClient) {
        this.bankingClient = bankingClient;
    }

    @Tool(description = "Fetches the current user's bank accounts from the banking service.")
    public Object fetchCurrentUserAccounts() {
        return bankingClient.fetchMyAccounts().block().getBody();
    }

    @Tool(description = "Fetches the current user's transactions from the banking service.")
    public Object fetchCurrentUserTransactions() {
        return bankingClient.fetchMyTransactions().block().getBody();
    }

    @Tool(description = "Fetches the current user's payments from the banking service.")
    public Object fetchCurrentUserPayments() {
        return bankingClient.fetchMyPayments().block().getBody();
    }

    @Tool(description = "Fetches the current user's notifications from the banking service.")
    public Object fetchCurrentUserNotifications() {
        return bankingClient.fetchMyNotifications().block().getBody();
    }

}
