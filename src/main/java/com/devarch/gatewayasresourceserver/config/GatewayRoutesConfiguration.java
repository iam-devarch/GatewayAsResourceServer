package com.devarch.gatewayasresourceserver.config;

import com.devarch.gatewayasresourceserver.filters.ConditionalGatewayFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfiguration {


    @Value("${backend.services.url.global}")
    private String globalBackendURL;

    @Value("${backend.services.url.conditional}")
    private String conditionalBackendURL;

    public GatewayRoutesConfiguration(ConditionalGatewayFilter conditionalGatewayFilter) {
        this.conditionalGatewayFilter = conditionalGatewayFilter;
    }

    ConditionalGatewayFilter conditionalGatewayFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("globalFilterRoute", r -> r.path("/global/**")
                        .uri(globalBackendURL))
                .route("conditionalFilterRoute", r -> r.path("/conditional/**")
                        .filters(f -> f.filter(conditionalGatewayFilter))
                        .uri(conditionalBackendURL))
                .build();
    }
}
