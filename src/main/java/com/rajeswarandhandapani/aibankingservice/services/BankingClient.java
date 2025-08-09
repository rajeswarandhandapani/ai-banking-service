package com.rajeswarandhandapani.aibankingservice.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import reactor.core.publisher.Mono;

@HttpExchange(url = "http://localhost:8080", accept = "application/json", contentType = "application/json")
public interface BankingClient {

    @GetExchange(value = "/api/accounts/my-accounts", accept = "application/json")
    Mono<ResponseEntity<Object>> fetchMyAccounts();

}
