package com.api.crud.controllers.dto.pets;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
public class PetRegisterRequest implements Serializable {
    private String name;
    private String species;
    private String breed;
    private OffsetDateTime dateBirth;
    private Double weight;
    private Long owner;
    private String photo;
}
