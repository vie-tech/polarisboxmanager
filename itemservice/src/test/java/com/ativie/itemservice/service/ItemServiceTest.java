package com.ativie.itemservice.service;

import com.ativie.itemservice.dto.CreateItemRequest;
import com.ativie.itemservice.dto.GetItemWithCodeResponse;
import com.ativie.itemservice.dto.GetItemsWithCodeRequest;
import com.ativie.itemservice.model.Item;
import com.ativie.itemservice.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doesItemExist_WhenItemExists_ShouldReturnTrue() {

        Item item = Item.builder()
                .code("ITEM001")
                .name("Test Item")
                .build();
        when(itemRepository.findByCode("ITEM001")).thenReturn(Optional.of(item));


        boolean result = itemService.doesItemExist("ITEM001");


        assertThat(result).isTrue();
        verify(itemRepository).findByCode("ITEM001");
    }

    @Test
    void doesItemExist_WhenItemDoesNotExist_ShouldReturnFalse() {

        when(itemRepository.findByCode("ITEM999")).thenReturn(Optional.empty());


        boolean result = itemService.doesItemExist("ITEM999");


        assertThat(result).isFalse();
        verify(itemRepository).findByCode("ITEM999");
    }

    @Test
    void createNewItem_ShouldSaveAndReturnItem() {

        CreateItemRequest request = new CreateItemRequest("Laptop", 2.5);
        Item savedItem = Item.builder()
                .name("Laptop")
                .weight(2.5)
                .code("ITEM001")
                .build();
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);


        Item result = itemService.createNewItem(request);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Laptop");
        assertThat(result.getWeight()).isEqualTo(2.5);
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void getItemsWithCode_WhenAllCodesExist_ShouldReturnAllItems() {

        Item item1 = Item.builder()
                .code("ITEM001")
                .name("Laptop")
                .weight(2.5)
                .build();
        Item item2 = Item.builder()
                .code("ITEM002")
                .name("Mouse")
                .weight(0.2)
                .build();

        when(itemRepository.findByCode("ITEM001")).thenReturn(Optional.of(item1));
        when(itemRepository.findByCode("ITEM002")).thenReturn(Optional.of(item2));

        GetItemsWithCodeRequest request = new GetItemsWithCodeRequest(
                List.of("ITEM001", "ITEM002")
        );


        List<GetItemWithCodeResponse> result = itemService.getItemsWithCode(request);


        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Laptop");
        assertThat(result.get(0).code()).isEqualTo("ITEM001");
        assertThat(result.get(1).name()).isEqualTo("Mouse");
        assertThat(result.get(1).code()).isEqualTo("ITEM002");
    }

    @Test
    void getItemsWithCode_WhenSomeCodesDoNotExist_ShouldReturnOnlyExistingItems() {

        Item item1 = Item.builder()
                .code("ITEM001")
                .name("Laptop")
                .weight(2.5)
                .build();

        when(itemRepository.findByCode("ITEM001")).thenReturn(Optional.of(item1));
        when(itemRepository.findByCode("ITEM999")).thenReturn(Optional.empty());

        GetItemsWithCodeRequest request = new GetItemsWithCodeRequest(
                List.of("ITEM001", "ITEM999")
        );


        List<GetItemWithCodeResponse> result = itemService.getItemsWithCode(request);


        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Laptop");
    }

    @Test
    void getItemsWithCode_WhenNoCodesExist_ShouldReturnEmptyList() {

        when(itemRepository.findByCode(anyString())).thenReturn(Optional.empty());

        GetItemsWithCodeRequest request = new GetItemsWithCodeRequest(
                List.of("ITEM999", "ITEM888")
        );


        List<GetItemWithCodeResponse> result = itemService.getItemsWithCode(request);


        assertThat(result).isEmpty();
    }
}