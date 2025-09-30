package com.ativie.boxservice.feign;


import com.ativie.boxservice.dto.GetItemsInBoxResponse;
import com.ativie.boxservice.dto.GetItemsInsideBoxRequest;
import com.ativie.boxservice.dto.GetItemsWithCodeRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "ITEMSERVICE", path =  "/item")
public interface ItemServiceClient {

    @GetMapping("/item_exists/{itemCode}")
    ResponseEntity<Boolean> itemExists(@PathVariable String itemCode);

    @PostMapping("/items_in_box")
    GetItemsInBoxResponse getAllItemsWithItemCode(@Valid @RequestBody GetItemsWithCodeRequest request);

}
