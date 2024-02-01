package com.example.videogameapi.filters;

import com.example.videogameapi.services.JWTTokenService;
import com.example.videogameapi.services.VideoUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.videogameapi.configuration.DebugConfiguration.getLogger;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JWTTokenService jwtService;

    private final VideoUserDetailsService detailsService;

    @Autowired
    public JwtAuthFilter(JWTTokenService jwtService, VideoUserDetailsService detailsService) {
        this.detailsService = detailsService;
        this.jwtService = jwtService;
    }

    // Does a filter check on whether the user has a JWT Token or not.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Check if user is authenticated.
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Checks if a JWT Token exists already.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.getTokenUsername(token);
        }

        // If the user exists, but they are not authenticated. We will proceed to authenticate them.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = detailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, username)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Parses the results of the authentication.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continue through the Authentication chain regardless of whether the user is authenticated or not.
        filterChain.doFilter(request, response);
    }
}
