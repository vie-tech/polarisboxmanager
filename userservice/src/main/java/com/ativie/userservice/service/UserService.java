package com.ativie.userservice.service;


import com.ativie.userservice.dto.CreateNewUserRequest;
import com.ativie.userservice.model.UserAccount;
import com.ativie.userservice.repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    private final UserAccountRepository userAccountRepository;

    public UserService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public boolean userExists(String userPublicId) {
        log.info("Checking if user exists with id {}", userPublicId );
        return userAccountRepository.existsByUserPublicId(userPublicId);
    }

    public void createNewUser(CreateNewUserRequest request){
        UserAccount user = UserAccount.builder()
                .email(request.email())
                .username(request.username())
                .build();
        userAccountRepository.save(user);
    }


}
