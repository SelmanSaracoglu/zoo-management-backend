package com.zoo.auth;

import com.zoo.user.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 8) String password,
        java.util.Set<Role> roles
) {}
