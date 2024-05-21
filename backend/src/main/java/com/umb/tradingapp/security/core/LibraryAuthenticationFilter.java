package com.umb.tradingapp.security.core;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.umb.tradingapp.security.dto.UserRolesDto;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.service.AuthenticationService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibraryAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    public LibraryAuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Filtering request: " + request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        
        System.out.println("authHeader: " + authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring("Bearer".length()).trim();
            UserRolesDto userRoles = authenticationService.authenticate(token);
            UserEntity user = authenticationService.getUser(token);

            request.setAttribute("user", user);

            List<SimpleGrantedAuthority> roles = userRoles.getRoles().stream().map(
                    role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userRoles.getUserName(),
                    null, roles);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

}
