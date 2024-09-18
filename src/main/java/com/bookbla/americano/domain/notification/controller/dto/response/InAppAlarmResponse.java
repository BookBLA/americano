package com.bookbla.americano.domain.notification.controller.dto.response;


import com.bookbla.americano.domain.notification.controller.dto.request.InAppAlarmRequest;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InAppAlarmResponse {

    private String imageUrl;

    private String title;

    private String body;


    public static InAppAlarmResponse of(InAppAlarmRequest req) {
        return InAppAlarmResponse.builder()
                .body(req.getBody())
                .title(req.getTitle())
                .imageUrl(req.getImageUrl())
                .build();
    }
}
