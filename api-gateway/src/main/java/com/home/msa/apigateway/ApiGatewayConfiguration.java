package com.home.msa.apigateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator getWayLocator(RouteLocatorBuilder routeLocatorBuilder) {

        Function<PredicateSpec, Buildable<Route>> routeFunction
                = p -> p.path("/get")
                .filters(f -> f.addRequestHeader("MyHeader", "MyUri")
                                .addRequestParameter("Param", "MyValue"))
                .uri("http://httpbin.org:80");
        return routeLocatorBuilder.routes()
                .route(routeFunction)
                .route(p -> p.path("//currency-exchange-service/**")
                            .uri("lb://currency-exchange-service"))
                .route(p -> p.path("//currency-conversion-service/**")
                           .uri("lb://currency-conversion-service"))
                .route(p -> p.path("//currency-conversion-service-new/**")
                        .filters(f->f.rewritePath("/currency-conversion-service-new/(?<segment>*)",
                                "/currency-conversion-service/${segment}"))
                           .uri("lb://currency-conversion-service"))
                .build();
    }

}
