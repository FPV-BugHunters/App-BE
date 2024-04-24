package com.umb.tradingapp.security.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.umb.tradingapp.security.service.AuthenticationService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private LibraryAuthenticationEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/authentication").permitAll()
                      .anyRequest().authenticated());
//        http.exceptionHandling((exception)-> exception.authenticationEntryPoint(authEntryPoint).accessDeniedPage("/error/accedd-denied"));
        http.exceptionHandling((exception)-> exception.authenticationEntryPoint(authEntryPoint));
       // http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(new LibraryAuthenticationFilter(authenticationService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // povoli len dve, to druhe je / hocico po druhe lomeno
        return (web) -> web.ignoring().requestMatchers("/api/authentication").
                and().ignoring().requestMatchers("/**").
                and().ignoring().requestMatchers("/registration").
                and().ignoring().requestMatchers("/swagger-ui");
    }




}
