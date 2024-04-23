package com.umb.tradingapp.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
	
    @Value("${APPLICATION_URL}")
    private String applicationUrl;

	@Bean
	public OpenAPI tradingDatabaseOpenAPI() {
		return new OpenAPI()
			.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
				.components(new Components().addSecuritySchemes
					("Bearer Authentication", createAPIKeyScheme()))
			.info(new Info().title("Trading Application").description("Description").version("1.0")).servers(Arrays.asList(new Server().url(applicationUrl).description("Generated server url")));
	}
	
	private SecurityScheme createAPIKeyScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP)
			.bearerFormat("JWT")
			.scheme("bearer");
	}
}