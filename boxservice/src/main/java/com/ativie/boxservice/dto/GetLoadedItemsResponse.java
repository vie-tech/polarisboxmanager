package com.ativie.boxservice.dto;

import java.util.List;

public record GetLoadedItemsResponse(boolean success, List<GetItemWithCodeResponse> items) {
}
