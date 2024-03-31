package com.InstaGenius.CloudGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudGatewayApplication.class, args);
	}


	/* Proxy, wchodzac pod 127.0.0.1:8082/api (adres cloud gateway) bedziemy bili pod endpointy pod portem 8081,
	* czyli resource server. */
	@Bean
	RouteLocator gateway(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder
				.routes()
				.route(
						route -> route
								.path("/api/**")
								.filters(f -> f
										.tokenRelay()
										.removeRequestHeader("Cookie")
										.rewritePath("/api/(?<remaining>.*)", "/${remaining}"))
								.uri("http://localhost:8081")
				).build();

	}


}
