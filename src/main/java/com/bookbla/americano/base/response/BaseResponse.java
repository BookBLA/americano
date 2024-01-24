package com.bookbla.americano.base.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"isSuccess", "code", "result"})
public class BaseResponse<T> {

    private static final String DEFAULT_SUCCESS_RESPONSE_CODE = "2000";

    @JsonProperty("isSuccess")
    private final boolean isSuccess;
    private final String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    public BaseResponse(T result) {
        this.isSuccess = true;
        this.code = DEFAULT_SUCCESS_RESPONSE_CODE;
        this.result = result;
    }
}
