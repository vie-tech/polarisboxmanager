package com.ativie.userservice.service;

import com.ativie.userservice.dto.CreateNewUserRequest;
import com.ativie.userservice.model.UserAccount;
import com.ativie.userservice.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void userExists_WhenUserExists_ShouldReturnTrue() {

        String userPublicId = "user123";
        when(userAccountRepository.existsByUserPublicId(userPublicId))
                .thenReturn(true);


        boolean result = userService.userExists(userPublicId);

        assertThat(result).isTrue();
        verify(userAccountRepository).existsByUserPublicId(userPublicId);
    }

    @Test
    void userExists_WhenUserDoesNotExist_ShouldReturnFalse() {

        String userPublicId = "user999";
        when(userAccountRepository.existsByUserPublicId(userPublicId))
                .thenReturn(false);


        boolean result = userService.userExists(userPublicId);


        assertThat(result).isFalse();
        verify(userAccountRepository).existsByUserPublicId(userPublicId);
    }

    @Test
    void createNewUser_ShouldSaveUser() {

        CreateNewUserRequest request = new CreateNewUserRequest(
                "john.doe@example.com",
                "johndoe"
        );


        userService.createNewUser(request);


        verify(userAccountRepository).save(argThat(user -> {
            assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
            assertThat(user.getUsername()).isEqualTo("johndoe");
            return true;
        }));
    }

    @Test
    void createNewUser_WithDifferentData_ShouldSaveCorrectly() {

        CreateNewUserRequest request = new CreateNewUserRequest(
                "jane.smith@example.com",
                "janesmith"
        );


        userService.createNewUser(request);


        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }
}