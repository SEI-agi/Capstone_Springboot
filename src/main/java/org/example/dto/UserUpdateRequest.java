package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(max = 150) String name,
        @Email @Size(max = 150) String email,
        @Size(min = 8, max = 255) String password,
        @Size(max = 30) String phone,
        String role
) {
}
