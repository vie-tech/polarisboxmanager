package com.ativie.itemservice.service;


import com.ativie.itemservice.dto.CreateItemRequest;
import com.ativie.itemservice.model.Item;
import com.ativie.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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
}
