package com.bookbla.americano.domain.alarm.infra.api.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.github.jav.exposerversdk.ExpoPushTicket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReceiptsRequest {

    private List<String> ids;

    public static ReceiptsRequest of(List<ExpoPushTicket> expoPushTickets) {
        List<String> ids = expoPushTickets.stream()
                .map(ExpoPushTicket::getId)
                .collect(Collectors.toList());
        return new ReceiptsRequest(ids);
    }

    public static ReceiptsRequest from(ExpoPushTicket expoPushTickets) {
        return new ReceiptsRequest(Arrays.asList(expoPushTickets.getId()));
    }

}
