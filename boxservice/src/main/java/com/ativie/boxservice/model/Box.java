package com.ativie.boxservice.model;


import com.ativie.boxservice.enums.BoxState;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "box")
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Box {
    private ObjectId id;

    @Builder.Default
    @Size(max = 20)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String boxPublicId = UUID.randomUUID().toString();

    private String name;

    @Min(1)
    @Max(500)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double weightLimit = 500;

    @Min(0)
    @Max(100)
    @Builder.Default
    private double batteryCapacity = 100.0;


    private String userPublicId;

    @Builder.Default
    private double currentWeightCapacity = 0.0;

    @Builder.Default
    private BoxState state = BoxState.IDLE;

    private List<String> itemsLoaded = new ArrayList<>();


    @Builder.Default
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Indexed(unique = true)
    private String txref = UUID.randomUUID().toString();

    private boolean charging;

    @CreatedDate
    private Instant createdAt;

    private Instant lastStateChange;

    @LastModifiedDate
    private Instant lastModifiedAt;
}
