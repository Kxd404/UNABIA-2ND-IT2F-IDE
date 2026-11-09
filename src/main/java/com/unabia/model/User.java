package com.unabia.model;

/**
 * Immutable data model of one row in the users table (matches the proposal's Table 1).
 */
public record User(int userId, String username, String passwordHash,
                   String fullName, String role, String status, String createdAt) {
}
