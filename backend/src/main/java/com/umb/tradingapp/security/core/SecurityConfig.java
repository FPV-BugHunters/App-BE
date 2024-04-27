package com.umb.tradingapp.security.core;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.umb.tradingapp.security.service.AuthenticationService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private static final String[] SWAGGER_PATHS = {"/api-docs/**", "/swagger-ui/**"};
    
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private LibraryAuthenticationEntryPoint authEntryPoint;
	

    
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http

            .csrf((csrf) -> csrf.disable()).cors(withDefaults())
			.sessionManagement( (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/registration").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/user").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/authentication").permitAll()
					.requestMatchers(HttpMethod.DELETE, "/api/authentication").permitAll()

					.requestMatchers("/api/cryptos").permitAll()
                .requestMatchers(SWAGGER_PATHS).permitAll()
                .anyRequest().authenticated())

                // .addFilterBefore(new LibraryAuthenticationFilter(authenticationService),
                //         UsernamePasswordAuthenticationFilter.class)

				// .addFilterBefore(libraryAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exception)-> exception.authenticationEntryPoint(authEntryPoint));

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
		config.applyPermitDefaultValues();

		source.registerCorsConfiguration("/**", config);
		return source;
	}

}
