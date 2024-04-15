package com.bookbla.americano.domain.postcard.service.dto.response;

import lombok.*;

import java.util.List;

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
        private String postcardTypePrice;
    }
}
