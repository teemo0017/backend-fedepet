package com.api.crud.controllers.dto.dates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindDatesRequest implements Serializable {

    private Long id;
}
