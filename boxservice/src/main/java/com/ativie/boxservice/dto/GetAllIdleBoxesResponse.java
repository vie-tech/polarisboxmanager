package com.ativie.boxservice.dto;

import java.util.List;

public record GetAllIdleBoxesResponse(String name,
        String txref,
        double weightLimit,
        double batteryCapacity
) {
}
