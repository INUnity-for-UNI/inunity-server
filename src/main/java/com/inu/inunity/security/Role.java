package com.inu.inunity.security;

public enum Role {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_TEST("ROLE_TEST");

    String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.getRole().equals(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No Role found for value: " + role);
    }
}
