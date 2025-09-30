package com.ativie.boxservice.dto;

public record AddItemToBoxRequest(String itemName, String boxTxref, double itemWeight,
                                  String userPublicId, String itemCode) {
}

