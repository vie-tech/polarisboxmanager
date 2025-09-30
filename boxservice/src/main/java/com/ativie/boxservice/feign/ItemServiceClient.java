package com.ativie.boxservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ITEMSERVICE", path =  "/api/item")
public interface ItemServiceClient {

    @GetMapping("/item_exists/{itemCode}")
    ResponseEntity<Boolean> itemExists(@PathVariable String itemCode);
}
