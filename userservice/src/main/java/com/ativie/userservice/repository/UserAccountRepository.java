package com.ativie.userservice.repository;


import com.ativie.userservice.model.UserAccount;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends MongoRepository<UserAccount, ObjectId> {
    boolean existsByUserPublicId(String userPublicId);
}
