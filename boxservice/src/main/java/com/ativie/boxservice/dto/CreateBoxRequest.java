package com.ativie.boxservice.dto;

import jakarta.validation.constraints.NotNull;

public record CreateBoxRequest(@NotNull String name, @NotNull String userPublicId) {
}
