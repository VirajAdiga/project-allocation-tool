package com.theja.projectallocationservice.config;

import com.theja.projectallocationservice.entities.enums.PermissionName;
import com.theja.projectallocationservice.dto.RequestContext;
import com.theja.projectallocationservice.services.UserServiceClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Component class responsible for filtering requests to enforce permission-based access control.
@Slf4j
@Component
@AllArgsConstructor
public class PermissionsFilter extends OncePerRequestFilter {
    private final UserServiceClient userServiceClient;

    private final RequestContext requestContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Permissions filter received request");
        // Retrieve the request path
        String requestPath = request.getRequestURI();

        // Exclude Swagger UI paths from authorization check
        if (requestPath.startsWith("/swagger-ui") || requestPath.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response); // Allow access without authorization check
            return;
        }

        // Retrieve the authorization header from the request
        final String authHeader = request.getHeader("Authorization");

        // Check if the authorization header is missing
        if (authHeader == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing authorization header");
            return;
        }

        // Retrieve the list of permissions associated with the authenticated user
        java.util.List<PermissionName> permissions = userServiceClient.getPermissions(authHeader);

        // Set the permissions and logged-in user details in the RequestContext
        requestContext.setPermissions(permissions);
        requestContext.setLoggedinUser(userServiceClient.getUser(authHeader));

        // Continue processing the request by passing it through the filter chain
        filterChain.doFilter(request, response);
    }
}

