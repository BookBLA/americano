package com.bookbla.americano.domain.book.infra.kakao;

import com.bookbla.americano.domain.book.infra.kakao.dto.KakaoBookResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoBookClient {

    private static final String AUTH_TYPE = "KakaoAK ";

    private final KakaoBookApi kakaoBookApi;

    @Value("${book.kakao.token}")
    private String kakaoToken;

    public KakaoBookClient(KakaoBookApi kakaoBookApi) {
        this.kakaoBookApi = kakaoBookApi;
    }

    public KakaoBookResponse searchBook(String text, int size, int page) {
        String tokenValue = AUTH_TYPE + kakaoToken;
        return kakaoBookApi.getBookInformation(tokenValue, text, size, page);
    }
}
