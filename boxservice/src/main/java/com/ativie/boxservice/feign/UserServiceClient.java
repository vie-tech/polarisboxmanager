package com.ativie.boxservice.feign;


import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USERSERVICE", path = "/api/user")
public interface UserServiceClient {
    @GetMapping("/user_exists/{userPublicId}")
     ResponseEntity<Boolean> checkUserExists(@Valid @PathVariable String userPublicId);
}
