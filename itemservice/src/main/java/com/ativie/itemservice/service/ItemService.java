package com.ativie.itemservice.service;


import com.ativie.itemservice.dto.CreateItemRequest;
import com.ativie.itemservice.dto.GetItemWithCodeResponse;
import com.ativie.itemservice.dto.GetItemsWithCodeRequest;
import com.ativie.itemservice.model.Item;
import com.ativie.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public boolean doesItemExist(String itemCode) {
        log.info("Checking if item with code {} exists", itemCode);
        Item item = itemRepository.findByCode(itemCode).orElse(null);
        return item != null;
    }

    public Item createNewItem(CreateItemRequest request) {
        Item item = Item.builder()
                .name(request.name())
                .weight(request.weight())
                .build();

        return itemRepository.save(item);
    }

    public List<GetItemWithCodeResponse> getItemsWithCode(GetItemsWithCodeRequest request) {

        return request.codes().stream()
                .map(itemRepository::findByCode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(item -> new GetItemWithCodeResponse(item.getName(), item.getCode(), item.getWeight()))
                .toList();
    }
}
