package com.ativie.boxservice.service;

import com.ativie.boxservice.dto.*;
import com.ativie.boxservice.enums.BoxState;
import com.ativie.boxservice.feign.ItemServiceClient;
import com.ativie.boxservice.feign.UserServiceClient;
import com.ativie.boxservice.model.Box;
import com.ativie.boxservice.repository.BoxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BoxServiceTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private ItemServiceClient itemServiceClient;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private BoxService boxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void createNewBox_WhenUserExists_ShouldCreateBox() {
        // Arrange
        CreateBoxRequest request = new CreateBoxRequest("TestBox", "user123");
        when(userServiceClient.checkUserExists("user123"))
                .thenReturn(ResponseEntity.ok(true));

        Box savedBox = Box.builder()
                .name("TestBox")
                .userPublicId("user123")
                .build();
        when(boxRepository.save(any(Box.class))).thenReturn(savedBox);

        // Act
        Box result = boxService.createNewBox(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("TestBox");
        assertThat(result.getUserPublicId()).isEqualTo("user123");
        verify(userServiceClient).checkUserExists("user123");
        verify(boxRepository).save(any(Box.class));
    }

    @Test
    void createNewBox_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        CreateBoxRequest request = new CreateBoxRequest("TestBox", "user123");
        when(userServiceClient.checkUserExists("user123"))
                .thenReturn(ResponseEntity.ok(false));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boxService.createNewBox(request)
        );
        assertThat(exception.getMessage()).isEqualTo("User does not exist");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void createNewBox_WhenUserServiceFails_ShouldThrowException() {
        // Arrange
        CreateBoxRequest request = new CreateBoxRequest("TestBox", "user123");
        when(userServiceClient.checkUserExists("user123"))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> boxService.createNewBox(request)
        );
        assertThat(exception.getMessage()).contains("Cannot verify user existence");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void addItem_WhenBoxNotFound_ShouldThrowException() {

        AddItemToBoxRequest request = new AddItemToBoxRequest(
                "box123", "item001", 200.0, "4352e02d-bfda-42ce-ba5b-d2eee6d9616b", "ITEM_707770"
        );
        when(boxRepository.findByTxref("box123")).thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boxService.addItem(request)
        );
        assertThat(exception.getMessage()).contains("Box not found");
    }

    @Test
    void addItem_WhenBatteryBelow25_ShouldThrowException() {

        AddItemToBoxRequest request = new AddItemToBoxRequest(
                     "box123", "item001", 200.0, "4352e02d-bfda-42ce-ba5b-d2eee6d9616b", "ITEM_707770"
             );

        Box box = Box.builder()
                .txref("box123")
                .batteryCapacity(20.0)
                .build();

        when(boxRepository.findByTxref("box123")).thenReturn(Optional.of(box));


        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boxService.addItem(request)
        );
        assertThat(exception.getMessage());
    }

    @Test
    void addItem_WhenWeightLimitExceeded_ShouldThrowException() {

        AddItemToBoxRequest request = new AddItemToBoxRequest(
                "box123", "item001", 200.0, "4352e02d-bfda-42ce-ba5b-d2eee6d9616b", "ITEM_707770"
        );

        Box box = Box.builder()
                .txref("box123")
                .batteryCapacity(100.0)
                .weightLimit(500.0)
                .currentWeightCapacity(100.0)
                .build();

        when(boxRepository.findByTxref("box123")).thenReturn(Optional.of(box));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boxService.addItem(request)
        );
        assertThat(exception.getMessage());
    }

    @Test
    void addItem_WhenItemDoesNotExist_ShouldThrowException() {

        AddItemToBoxRequest request = new AddItemToBoxRequest(
                "box123", "item001", 200.0, "4352e02d-bfda-42ce-ba5b-d2eee6d9616b", "ITEM_707770"
        );

        Box box = Box.builder()
                .txref("box123")
                .batteryCapacity(100.0)
                .weightLimit(500.0)
                .currentWeightCapacity(100.0)
                .itemsLoaded(new ArrayList<>())
                .build();

        when(boxRepository.findByTxref("box123")).thenReturn(Optional.of(box));
        when(itemServiceClient.itemExists("item001"))
                .thenReturn(ResponseEntity.ok(false));


        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boxService.addItem(request)
        );
        assertThat(exception.getMessage());
    }

    @Test
    void addItem_WhenItemServiceFails_ShouldThrowException() {

        AddItemToBoxRequest request = new AddItemToBoxRequest(
                 "box123", "item001", 200.0, "4352e02d-bfda-42ce-ba5b-d2eee6d9616b", "ITEM_707770"
         );

        Box box = Box.builder()
                .txref("box123")
                .batteryCapacity(100.0)
                .weightLimit(500.0)
                .currentWeightCapacity(100.0)
                .itemsLoaded(new ArrayList<>())
                .build();

        when(boxRepository.findByTxref("box123")).thenReturn(Optional.of(box));
        when(itemServiceClient.itemExists("item001"))
                .thenThrow(new RuntimeException("Service down"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boxService.addItem(request)
        );
        assertThat(exception.getMessage());
    }



    @Test
    void getAllItemsInsideBox_WhenBoxExists_ShouldReturnItems() {

        String boxPublicId = "box-pub-123";
        List<String> itemCodes = List.of("item001", "item002");

        Box box = Box.builder()
                .boxPublicId(boxPublicId)
                .itemsLoaded(itemCodes)
                .build();

        List<GetItemWithCodeResponse> expectedItems = List.of(
                new GetItemWithCodeResponse("item001", "Item 1", 10.0),
                new GetItemWithCodeResponse("item002", "Item 2", 20.0)
        );

        GetItemsInBoxResponse response = new GetItemsInBoxResponse(true, expectedItems);

        when(boxRepository.findByBoxPublicId(boxPublicId))
                .thenReturn(Optional.of(box));
        when(itemServiceClient.getAllItemsWithItemCode(any(GetItemsWithCodeRequest.class)))
                .thenReturn(response);


        List<GetItemWithCodeResponse> result =
                boxService.getAllItemsInsideBox(boxPublicId);


        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(expectedItems);
        verify(itemServiceClient).getAllItemsWithItemCode(
                argThat(req -> req.codes().equals(itemCodes))
        );
    }

    @Test
    void getAllItemsInsideBox_WhenBoxNotFound_ShouldThrowException() {

        String boxPublicId = "box-pub-123";
        when(boxRepository.findByBoxPublicId(boxPublicId))
                .thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boxService.getAllItemsInsideBox(boxPublicId)
        );
        assertThat(exception.getMessage()).contains("Box does not exist");
    }



    @Test
    void getAllIdleBoxes_WhenIdleBoxesExist_ShouldReturnBoxes() {

        List<Box> idleBoxes = List.of(
                Box.builder()
                        .name("Box1")
                        .txref("txref1")
                        .weightLimit(500.0)
                        .batteryCapacity(100.0)
                        .state(BoxState.IDLE)
                        .build(),
                Box.builder()
                        .name("Box2")
                        .txref("txref2")
                        .weightLimit(300.0)
                        .batteryCapacity(80.0)
                        .state(BoxState.IDLE)
                        .build()
        );

        when(boxRepository.findByState(BoxState.IDLE)).thenReturn(idleBoxes);


        List<GetAllIdleBoxesResponse> result = boxService.getAllIdleBoxes();


        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Box1");
        assertThat(result.get(0).txref()).isEqualTo("txref1");
        assertThat(result.get(1).name()).isEqualTo("Box2");
    }

    @Test
    void getAllIdleBoxes_WhenNoIdleBoxes_ShouldReturnEmptyList() {

        when(boxRepository.findByState(BoxState.IDLE)).thenReturn(List.of());


        List<GetAllIdleBoxesResponse> result = boxService.getAllIdleBoxes();


        assertThat(result).isEmpty();
    }



    @Test
    void returnBoxBatteryLevel_WhenBoxExists_ShouldReturnBatteryLevel() {

        String txref = "txref123";
        Box box = Box.builder()
                .name("TestBox")
                .txref(txref)
                .batteryCapacity(75.0)
                .weightLimit(500.0)
                .build();

        when(boxRepository.findByTxref(txref)).thenReturn(Optional.of(box));


        GetBatteryLevelResponse result = boxService.returnBoxBatteryLevel(txref);


        assertThat(result.name()).isEqualTo("TestBox");
        assertThat(result.batteryCapacity()).isEqualTo(75.0);
        assertThat(result.weight()).isEqualTo(500.0);
    }

    @Test
    void returnBoxBatteryLevel_WhenBoxNotFound_ShouldThrowException() {

        String txref = "txref123";
        when(boxRepository.findByTxref(txref)).thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> boxService.returnBoxBatteryLevel(txref)
        );
        assertThat(exception.getMessage()).contains("This box does not exist");
    }
}