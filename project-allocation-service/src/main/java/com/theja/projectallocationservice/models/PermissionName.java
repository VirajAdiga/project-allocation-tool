package com.theja.projectallocationservice.models;

/**
 * Enumeration representing the different types of permissions in the system.
 * These permissions define specific actions and functionalities that users can have access to.
 */
public enum PermissionName {
    MANAGE_USERS,                   // Permission to manage user accounts
    REGISTER_USER,                  // Permission to register a new user
    VIEW_USER_ACTIVITY,             // Permission to view user activity logs
    CREATE_PROJECT,                 // Permission to create a new project
    ADMIN_OWN_OPENINGS,             // Permission for an admin to manage their own job openings
    ADMIN_OTHER_OPENINGS,           // Permission for an admin to manage job openings of other users
    VIEW_PENDING_APPLICATIONS,      // Permission to view pending job applications
    MANAGE_OPENINGS,                // Permission to manage job openings
    RECRUITER_OWN_OPENINGS,         // Permission for a recruiter to manage their own job openings
    RECRUITER_OTHER_OPENINGS,       // Permission for a recruiter to manage job openings of other users
    CREATE_OPENING,                 // Permission to create a new job opening
    VIEW_REPORTS,                   // Permission to view various reports and analytics
    VIEW_ALL_OPENINGS,              // Permission to view all job openings
    VIEW_APPLIED_OPENINGS           // Permission to view job openings to which the user has applied
}
