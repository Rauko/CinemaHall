package com.cinema.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public JwtAuthFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                    throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        try {

            userEmail = jwtUtils.extractUsername(jwtToken);

        } catch (Exception ex) {

            log.warn("Invalid or expired JWT: path={}, method={}",
                    request.getRequestURI(),
                    request.getMethod()
            );

            filterChain.doFilter(request, response);
            return;
        }

        //check that user is not authorised repeatedly
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            try {

                if (jwtUtils.validateToken(jwtToken, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetails(request)
                    );

                    // adding in Spring security context
                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);

                    log.debug("JWT authentication successful: userEmail={}, path={}, method={}",
                            userEmail,
                            request.getRequestURI(),
                            request.getMethod()
                    );

                } else {

                    log.warn("JWT validation failed: userEmail={}, path={}, method={}",
                            userEmail,
                            request.getRequestURI(),
                            request.getMethod()
                    );
                }

            } catch (Exception ex) {

                log.warn("Invalid or expired JWT: userEmail={}, path={}, method={}",
                        userEmail,
                        request.getRequestURI(),
                        request.getMethod()
                );
            }
        }
        //let it go down the chain
        filterChain.doFilter(request, response);
    }
}