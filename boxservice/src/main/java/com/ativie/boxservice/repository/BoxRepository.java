package com.ativie.boxservice.repository;


import com.ativie.boxservice.model.Box;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxRepository extends MongoRepository<Box, ObjectId> {
}
