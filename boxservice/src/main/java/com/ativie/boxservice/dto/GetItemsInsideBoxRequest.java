package com.ativie.boxservice.dto;

import jakarta.validation.constraints.NegativeOrZero;

import java.util.List;

public record GetItemsInsideBoxRequest(List<String> codes) {

}
