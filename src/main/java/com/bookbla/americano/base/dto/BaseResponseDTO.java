package com.bookbla.americano.base.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class BaseResponseDTO {

    private final boolean isSuccess = true;
    private Object data;

    public static BaseResponseDTO create(Object contents) {
        BaseResponseDTO dto = new BaseResponseDTO();
        dto.setContents(contents);
        return dto;
    }

    public void setContents(Object contents) {
        this.data = contents;
    }
}
