package com.project.userservice.config;

import com.project.userservice.models.DBUser;
import com.project.userservice.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Custom JWT authentication filter to validate and authenticate requests using JWT tokens.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter implements Ordered {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private boolean disabled = false; // Flag to temporarily disable the filter

    /**
     * Setter method to enable/disable the filter.
     *
     * @param disabled Flag indicating whether the filter is disabled.
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Retrieve the request path
        String requestPath = request.getRequestURI();

        // Exclude Swagger UI paths from authorization check
        if (requestPath.startsWith("/swagger-ui") || requestPath.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response); // Allow access without authorization check
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (disabled) {
            // If the filter is disabled, pass the request directly to the next filter in the chain.
            filterChain.doFilter(request, response);
            return;
        }

        // Check for certain conditions where the filter should not be applied.
        if (!request.getServletPath().startsWith("/api/v1/auth/register") && !request.getServletPath().startsWith("/api/v1/auth/authenticate") && (authHeader == null || !authHeader.startsWith("Bearer "))) {
            throw new RuntimeException("Invalid request");
        } else if (request.getServletPath().startsWith("/api/v1/auth/register") || request.getServletPath().startsWith("/api/v1/auth/authenticate")) {
            // For registration and authentication requests, bypass the filter and continue the chain.
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractSubject(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            DBUser user = userRepository.findByEmail(userEmail).orElseThrow(RuntimeException::new);
            String storedToken = user.getToken(); // Retrieve the token from the user object

            // Validate the token and ensure it matches the stored token.
            if (storedToken != null && jwtService.isTokenValid(jwt, userEmail) && jwt.equals(storedToken)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        null
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
                return;
            }
        }

        // If none of the conditions are met, throw an exception indicating invalid user email.
        throw  new RuntimeException("User email is not valid");
    }

    @Override
    public int getOrder() {
        // Set the order of the filter in the filter chain.
        return Ordered.LOWEST_PRECEDENCE - 100;
    }
}

