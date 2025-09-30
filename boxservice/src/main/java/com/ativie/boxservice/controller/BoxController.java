package com.ativie.boxservice.controller;


import com.ativie.boxservice.dto.*;
import com.ativie.boxservice.model.Box;
import com.ativie.boxservice.service.BoxService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/box")
public class BoxController {
    private final BoxService boxService;

    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBox(@Valid @RequestBody CreateBoxRequest request) {
        Box savedBox = boxService.createNewBox(request);

        return ResponseEntity.ok(new CreateBoxResponse(
                true,
                String.format("Box %s created successfully", request.name()),
                savedBox.getTxref()
        ));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToBox(@Valid @RequestBody AddItemToBoxRequest request) {
        boxService.addItem(request);
        return ResponseEntity.ok(new AddItemToBoxResponse(true,
                String.format("Item %s added to box", request.itemName())));
    }

    @GetMapping("/checkLoadedItems/{boxPublicId}")
    public GetLoadedItemsResponse getLoadedItem(@PathVariable String boxPublicId) {
        List<GetItemWithCodeResponse> items =
                boxService.getAllItemsInsideBox(boxPublicId);
        return new GetLoadedItemsResponse(true, items);
    }

    @GetMapping("/getIdle")
    public ResponseEntity<Map<String, Object>> getIdleBoxes() {
        List<GetAllIdleBoxesResponse> idleBoxes = boxService.getAllIdleBoxes();
        return ResponseEntity.ok(
                Map.of("success", true, "boxes", idleBoxes)
        );
    }

    @GetMapping("/getBattery/{txref}")
    public ResponseEntity<Map<String, Object>> getBatteryLevel(@PathVariable String txref) {
        GetBatteryLevelResponse box = boxService.returnBoxBatteryLevel(txref);
        return ResponseEntity.ok(Map.of("success", true, "box", box));
    }
}
