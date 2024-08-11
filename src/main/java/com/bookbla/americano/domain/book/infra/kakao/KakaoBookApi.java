package com.bookbla.americano.domain.book.infra.kakao;

import com.bookbla.americano.domain.book.infra.kakao.dto.KakaoBookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "kakao-book", url = "https://dapi.kakao.com/v3")
public interface KakaoBookApi {

    @GetMapping("/search/book")
    KakaoBookResponse getBookInformation(
            @RequestHeader("Authorization") String tokenValue,
            @RequestParam String query,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) int page
    );

}
