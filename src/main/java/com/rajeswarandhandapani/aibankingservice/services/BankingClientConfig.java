package com.rajeswarandhandapani.aibankingservice.services;

import com.rajeswarandhandapani.aibankingservice.utils.ApplicationSecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.URI;

@Configuration
public class BankingClientConfig {


    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        builder.filter(authorizationFilter());
        return builder.build();
    }

    @Bean
    public BankingClient bankingClient(WebClient webClient) {
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(BankingClient.class);
    }

    private ExchangeFilterFunction authorizationFilter() {
        return (request, next) -> {
            
            URI uri = request.url();
            boolean targetIsLocalcost = "localcost".equalsIgnoreCase(uri.getHost()) && uri.getPort() == 4200;

            if (targetIsLocalcost && !request.headers().containsKey("Authorization")) {
                String bearer = ApplicationSecurityContext.getJwt();
                if (bearer != null && !bearer.isBlank()) {
                    ClientRequest authorized = ClientRequest.from(request)
                            .header("Authorization", bearer)
                            .build();
                    return next.exchange(authorized);
                }
            }
            return next.exchange(request);
        };
    }
}
