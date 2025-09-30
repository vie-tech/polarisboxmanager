package com.ativie.itemservice.dto;

import jakarta.validation.constraints.NotNull;

public record CreateItemRequest(@NotNull String name, @NotNull double weight) {
}
