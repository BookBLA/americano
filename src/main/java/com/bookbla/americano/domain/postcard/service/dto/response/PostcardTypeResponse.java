package com.bookbla.americano.domain.postcard.service.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.postcard.repository.entity.PostcardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
        private String postcardImageUrl;

    }

    public static PostcardTypeResponse of(List<PostcardType> postcardTypeList) {
        List<PostcardTypeDto> postcardTypeDtoList = postcardTypeList.stream()
                .map(postcardType -> new PostcardTypeDto(postcardType.getId(), postcardType.getName(), postcardType.getPrice(), postcardType.getImageUrl()))
                .collect(Collectors.toList());
        return new PostcardTypeResponse(postcardTypeDtoList);
    }
}
