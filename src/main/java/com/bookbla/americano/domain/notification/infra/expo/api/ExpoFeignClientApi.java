package com.bookbla.americano.domain.notification.infra.expo.api;

import com.bookbla.americano.domain.notification.infra.expo.api.dto.ReceiptsRequest;
import com.bookbla.americano.domain.notification.infra.expo.api.dto.ReceiptsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "expo", url = "https://exp.host")
public interface ExpoFeignClientApi {

    @PostMapping("/--/api/v2/push/getReceipts")
    ReceiptsResponse postReceipts(ReceiptsRequest receiptsRequest);

}
