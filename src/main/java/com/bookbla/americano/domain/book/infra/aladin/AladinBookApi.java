package com.bookbla.americano.domain.book.infra.aladin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "aladin-book", url = "http://www.aladin.co.kr/ttb/api")
public interface AladinBookApi {

    @GetMapping("/itemLookup.aspx")
    String getBook(
            @RequestParam String ttbKey,
            @RequestParam String itemId,
            @RequestParam(required = false, defaultValue = "ISBN13") String itemIdType,
            @RequestParam(required = false, defaultValue = "js") String output,
            @RequestParam(required = false, defaultValue = "Big") String cover
    );
}
