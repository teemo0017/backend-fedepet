package com.api.crud.controllers.dto.pets;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Builder
public class PetListResponse implements Serializable {
    private String name;
    private String species;
    private String breed;
    private OffsetDateTime dateBirth;
    private Double weight;
}
