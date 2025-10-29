package com.api.crud.controllers.dto.pets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPetsRequest implements Serializable {
    private Long id;
}
