package com.api.crud.repositories.interfaces;

import com.api.crud.controllers.dto.dates.SaveDateRequest;

public interface IDatesService {

    void save(SaveDateRequest saveDateRequest);
}
