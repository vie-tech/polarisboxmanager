package com.ativie.boxservice.controller;


import com.ativie.boxservice.dto.AddItemToBoxResponse;
import com.ativie.boxservice.dto.CreateBoxRequest;
import com.ativie.boxservice.dto.CreateBoxResponse;
import com.ativie.boxservice.dto.AddItemToBoxRequest;
import com.ativie.boxservice.model.Box;
import com.ativie.boxservice.service.BoxService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/box")
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
}
