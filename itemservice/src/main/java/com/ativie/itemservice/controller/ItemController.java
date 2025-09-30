package com.ativie.itemservice.controller;


import com.ativie.itemservice.dto.*;
import com.ativie.itemservice.model.Item;
import com.ativie.itemservice.service.ItemService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/item")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/item_exists/{itemCode}")
    public ResponseEntity<Boolean> itemExists(@PathVariable String itemCode) {

        boolean itemExists = itemService.doesItemExist(itemCode);
        log.info("Item exists is {}", itemExists);
        return ResponseEntity.ok(itemExists);
    }


    @PostMapping("/create")
    public ResponseEntity<CreateItemResponse> createNewItem(@Valid @RequestBody CreateItemRequest request) {
        Item newItem = itemService.createNewItem(request);

        return ResponseEntity.ok(new CreateItemResponse(newItem.getName(),
                newItem.getCode(), newItem.getWeight(),
                newItem.getItemPublicId()));
    }

    @PostMapping("/items_in_box")
    public GetItemsInBoxResponse getAllItemsWithItemCode(@Valid @RequestBody GetItemsWithCodeRequest request) {
        List<GetItemWithCodeResponse> items = itemService.getItemsWithCode(request);
        return new GetItemsInBoxResponse(true, items);
    }
}
