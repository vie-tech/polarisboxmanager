package com.ativie.boxservice.service;


import com.ativie.boxservice.dto.*;
import com.ativie.boxservice.enums.BoxState;
import com.ativie.boxservice.feign.ItemServiceClient;
import com.ativie.boxservice.feign.UserServiceClient;
import com.ativie.boxservice.model.Box;
import com.ativie.boxservice.repository.BoxRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class BoxService {

    private final BoxRepository boxRepository;
    private final ItemServiceClient itemserviceClient;
    private final UserServiceClient userServiceClient;

    public Box createNewBox(CreateBoxRequest request) {
        Boolean exists;
        try {
            exists =
                    userServiceClient.checkUserExists(request.userPublicId()).getBody();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Cannot verify user existence at the moment. Please try " +
                            "again later.", e
            );
        }

        if (Boolean.FALSE.equals(exists)) {
            throw new IllegalArgumentException("User does not exist");
        }

        Box box = Box.builder()
                .name(request.name())
                .userPublicId(request.userPublicId())
                .build();

        return boxRepository.save(box);
    }


    public void addItem(AddItemToBoxRequest request) {
        Box box = boxRepository.findByTxref(request.boxTxref())
                .orElseThrow(() -> new IllegalArgumentException("Box not " +
                        "found"));

        if (box.getBatteryCapacity() < 25) {
            throw new IllegalStateException("Cannot load: Battery below 25%");
        }

        if (box.getCurrentWeightCapacity() + request.itemWeight() > box.getWeightLimit()) {
            throw new IllegalStateException("Cannot add item: weight limit " +
                    "exceeded");
        }

   /*     List<String> itemsAlreadyInBox = box.getItemsLoaded();
        if (itemsAlreadyInBox.contains(request.itemCode())) {
            throw new IllegalStateException("Cannot add item: Already Exists " +
                    "in box");
        }*/


        Boolean itemExists;
        try {
            itemExists =
                    itemserviceClient.itemExists(request.itemCode()).getBody();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot verify item existence at " +
                    "the moment", e);
        }

        if (itemExists == null || !itemExists) {
            throw new IllegalArgumentException("Item does not exist");
        }

        box.setState(BoxState.LOADING);
        box.setLastStateChange(Instant.now());
        box.getItemsLoaded().add(request.itemCode());
        box.setCurrentWeightCapacity(box.getCurrentWeightCapacity() + request.itemWeight());
        boxRepository.save(box);

    }


    public List<GetItemWithCodeResponse> getAllItemsInsideBox(String boxPublicId) {
        Box box = boxRepository.findByBoxPublicId(boxPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Box does not" +
                        " exist"));

        List<String> codes = box.getItemsLoaded();


        GetItemsInBoxResponse response =
                itemserviceClient.getAllItemsWithItemCode(
                        new GetItemsWithCodeRequest(codes)
                );

        return response.items();
    }

    public List<GetAllIdleBoxesResponse> getAllIdleBoxes() {
        return boxRepository.findByState(BoxState.IDLE)
                .stream()
                .map(box -> new GetAllIdleBoxesResponse(
                        box.getName(),
                        box.getTxref(),
                        box.getWeightLimit(),
                        box.getBatteryCapacity()
                ))
                .toList();
    }

    public GetBatteryLevelResponse returnBoxBatteryLevel(String txref) {
        Box box =
                boxRepository.findByTxref(txref).orElseThrow(() -> new IllegalArgumentException("This box does not exist"));
        return new GetBatteryLevelResponse(box.getName(),
                box.getBatteryCapacity(), box.getWeightLimit());

    }
}
