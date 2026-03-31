package com.sathwik.auth.auth_service.filter;

import com.sathwik.auth.auth_service.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("🔥 JWT FILTER HIT: " + request.getRequestURI());

        String header = request.getHeader("Authorization");
        System.out.println("HEADER = " + header);

        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("❌ No Bearer header");
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        System.out.println("TOKEN = " + token);

        if (!jwtService.isValid(token)) {
            System.out.println("❌ TOKEN INVALID");
            filterChain.doFilter(request, response);
            return;
        }

        String userId = jwtService.extractUserId(token);
        System.out.println("✅ USER ID FROM TOKEN = " + userId);


        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userId, null, List.of());

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}