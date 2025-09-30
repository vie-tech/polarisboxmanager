package com.ativie.boxservice.dto;

import java.util.List;

public record GetItemsInBoxResponse(boolean success, List<GetItemWithCodeResponse> items) {}
