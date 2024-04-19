package com.bookbla.americano.domain.postcard.service.dto.response;

import com.bookbla.americano.domain.postcard.repository.entity.PostcardType;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class PostcardTypeResponse {
    private final List<PostcardTypeDto> postcardTypeList;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostcardTypeDto {
        private Long postcardTypeId;
        private String postcardTypeName;
        private int postcardTypePrice;
    }

    public static PostcardTypeResponse of(List<PostcardType> postcardTypeList) {
        List<PostcardTypeDto> postcardTypeDtoList = postcardTypeList.stream()
                .map(postcardType -> new PostcardTypeDto(postcardType.getId(), postcardType.getName(), postcardType.getPrice()))
                .collect(Collectors.toList());
        return new PostcardTypeResponse(postcardTypeDtoList);
    }
}
