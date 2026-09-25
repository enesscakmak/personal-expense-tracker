package com.PersonalExpense.common;

import com.PersonalExpense.service.JwtService;
import com.PersonalExpense.service.TokenRevocationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final TokenRevocationService tokenRevocationService;

    public JwtAuthFilter(JwtService jwtService, TokenRevocationService tokenRevocationService){
        this.jwtService = jwtService;
        this.tokenRevocationService = tokenRevocationService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) &&
                StringUtils.startsWithIgnoreCase(authorizationHeader, "Bearer ")) {

            String token = authorizationHeader.substring(7).trim();
            if(jwtService.validateJwtToken(token) && !tokenRevocationService.isRevoked(token)) {
                Long userId = Long.valueOf(jwtService.getUserIdFromToken(token));                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                authentication.setDetails(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
                filterChain.doFilter(request, response);
        }
        else {
            filterChain.doFilter(request, response);
        }
    }
}
