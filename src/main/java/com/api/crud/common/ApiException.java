package com.api.crud.common;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ResponseCode code;

    public ApiException(ResponseCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
