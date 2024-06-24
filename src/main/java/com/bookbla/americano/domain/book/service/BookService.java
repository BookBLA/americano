package com.bookbla.americano.domain.book.service;

import com.bookbla.americano.domain.book.infra.kakao.KakaoBookClient;
import com.bookbla.americano.domain.book.infra.kakao.dto.KakaoBookResponse;
import com.bookbla.americano.domain.book.infra.kakao.dto.KakaoTokenConfig;
import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private static final String AUTH_TYPE = "KakaoAK ";

    private final KakaoBookClient kakaoBookClient;
    private final String token;

    public BookService(KakaoBookClient kakaoBookClient, KakaoTokenConfig kakaoTokenConfig) {
        this.kakaoBookClient = kakaoBookClient;
        this.token = kakaoTokenConfig.getToken();
    }

    public BookSearchResponses searchBook(String text, int size, int page) {
        String tokenValue = AUTH_TYPE + token;
        KakaoBookResponse kakaoBookResponse = kakaoBookClient.getBookInformation(tokenValue, text, size, page);
        return kakaoBookResponse.toBookSearchResponsesWith(page);
    }
}
