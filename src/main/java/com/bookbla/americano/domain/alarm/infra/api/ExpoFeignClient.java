package com.bookbla.americano.domain.alarm.infra.api;

import com.bookbla.americano.domain.alarm.infra.api.dto.ReceiptsRequest;
import com.bookbla.americano.domain.alarm.infra.api.dto.ReceiptsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "apple", url = "https://exp.host")
public interface ExpoFeignClient {

    @PostMapping("/--/api/v2/push/getReceipts")
    ReceiptsResponse postReceipts(ReceiptsRequest receiptsRequest);

}
