package com.api.crud.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private String status;
    private T body;

    public static <T> ApiResponse<T> ok(T body) {
        return new ApiResponse<>(ResponseCode.SUCCESS.getCode(), body);
    }

    public static ApiResponse<String> error(ResponseCode code) {
        return new ApiResponse<>(code.getCode(), code.getMessage());
    }
}
