package com.ativie.userservice.controller;


import com.ativie.userservice.dto.CreateNewUserRequest;
import com.ativie.userservice.dto.CreateNewUserResponse;
import com.ativie.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user_exists/{userPublicId}")
    public ResponseEntity<Boolean> checkUserExists(@Valid @PathVariable String userPublicId) {
        System.out.println("It hit here");
        boolean doesUserExist = userService.userExists(userPublicId);
        return ResponseEntity.ok(doesUserExist);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody CreateNewUserRequest request) {
        userService.createNewUser(request);
        return ResponseEntity.ok(new CreateNewUserResponse(true,
                String.format("%s account was created successfully",
                        request.username())));
    }
}
