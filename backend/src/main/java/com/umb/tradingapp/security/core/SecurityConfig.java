package com.umb.tradingapp.security.core;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String[] SWAGGER_PATHS = {"/api-docs/**", "/swagger-ui/**", "/v3/api-docs/**"};
    
	private final LibraryAuthenticationEntryPoint libraryAuthenticationEntryPoint;
	private final LibraryAuthenticationFilter libraryAuthenticationFilter;

	public SecurityConfig(LibraryAuthenticationFilter libraryAuthenticationFilter, LibraryAuthenticationEntryPoint libraryAuthenticationEntryPoint) {
		this.libraryAuthenticationFilter = libraryAuthenticationFilter;
		this.libraryAuthenticationEntryPoint = libraryAuthenticationEntryPoint;
	}
    
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf((csrf) -> csrf.disable())
			.cors(withDefaults())
			.sessionManagement( (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests

                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/registration").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/cryptos").permitAll()

				.requestMatchers("/", "/**.html", "/assets/*.css", "/assets/*.js", "/assets/**", "/vite.svg" ).permitAll()

                .requestMatchers(SWAGGER_PATHS).permitAll()
				.anyRequest().authenticated()
			)

				.addFilterBefore(libraryAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling((exception) -> exception.authenticationEntryPoint(libraryAuthenticationEntryPoint));

            return http.build();
    }


	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("*"));
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowCredentials(false);
        config.setExposedHeaders(Arrays.asList("Authorization"));
		source.registerCorsConfiguration("/**", config);
		return source;
	}

}
