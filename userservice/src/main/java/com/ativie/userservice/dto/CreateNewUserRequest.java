package com.ativie.userservice.dto;

import jakarta.validation.constraints.NotNull;

public record CreateNewUserRequest(@NotNull String email, @NotNull String username) {
}
