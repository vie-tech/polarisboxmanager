package com.ativie.boxservice.repository;


import com.ativie.boxservice.enums.BoxState;
import com.ativie.boxservice.model.Box;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoxRepository extends MongoRepository<Box, ObjectId> {
    Optional<Box> findByTxref(String txref);

    List<Box> findByState(BoxState boxState);

    Optional<Box> findByBoxPublicId(String boxPublicId);
}
