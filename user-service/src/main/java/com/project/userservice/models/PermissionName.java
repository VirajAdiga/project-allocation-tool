package com.project.userservice.models;

// Enum representing different permission names for user roles.
public enum PermissionName {
    MANAGE_USERS,               // Permission to manage users.
    REGISTER_USER,              // Permission to register new users.
    VIEW_USER_ACTIVITY,         // Permission to view user activity.
    CREATE_PROJECT,             // Permission to create a project.
    ADMIN_OWN_OPENINGS,         // Permission for an admin to view their own openings.
    ADMIN_OTHER_OPENINGS,       // Permission for an admin to view other admin's openings.
    VIEW_PENDING_APPLICATIONS,  // Permission to view pending applications.
    MANAGE_OPENINGS,            // Permission to manage job openings.
    RECRUITER_OWN_OPENINGS,     // Permission for a recruiter to view their own openings.
    RECRUITER_OTHER_OPENINGS,   // Permission for a recruiter to view other recruiter's openings.
    CREATE_OPENING,             // Permission to create a job opening.
    VIEW_REPORTS,               // Permission to view reports.
    VIEW_ALL_OPENINGS,          // Permission to view all job openings.
    VIEW_APPLIED_OPENINGS,       // Permission to view applied job openings.
    MANAGE_INTERVIEWER_STATUS,
    INTERVIEWER
}
