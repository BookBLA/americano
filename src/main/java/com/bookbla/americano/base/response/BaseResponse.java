package com.bookbla.americano.base.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    private final boolean isSuccess;
    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    public BaseResponse(T result) {
        this.isSuccess = true;
        this.message = "응답 성공!";
        this.code = 200;
        this.result = result;
    }
}
