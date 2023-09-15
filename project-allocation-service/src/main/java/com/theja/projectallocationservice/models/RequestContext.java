package com.theja.projectallocationservice.models;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

/**
 * Represents the contextual information for the current request.
 * Holds permissions and logged-in user information in a request-scoped context.
 */
@RequestScope
@Component
@Data
public class RequestContext {
    List<PermissionName> permissions; // List of permissions associated with the user
    PublicUser loggedinUser;          // The currently logged-in user's information
}
