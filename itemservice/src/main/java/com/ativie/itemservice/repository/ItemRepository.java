package com.ativie.itemservice.repository;

import com.ativie.itemservice.model.Item;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ItemRepository extends MongoRepository<Item, ObjectId> {
    Optional<Item> existsByCode(String itemCode);
    Optional<Item> findByCode(String itemCode);
}
