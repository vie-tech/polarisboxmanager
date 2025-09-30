package com.ativie.itemservice.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@Document(collection = "items")
public class Item {

    private ObjectId id;


    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code can only contain " +
            "uppercase letters, numbers, and underscore")
    @Builder.Default
    private String code = generateDefaultCode();


    @Builder.Default
    private String itemPublicId = UUID.randomUUID().toString();

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Positive(message = "Weight must be positive")
    @NotBlank(message = "Name cannot be blank")
    private double weight;

    private static String generateDefaultCode() {
        return "ITEM_" + (int) (Math.random() * 1000000);
    }
}
