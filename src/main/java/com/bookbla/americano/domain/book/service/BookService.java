package com.bookbla.americano.domain.book.service;

import com.bookbla.americano.domain.book.infra.kakao.KakaoBookApi;
import com.bookbla.americano.domain.book.infra.kakao.dto.KakaoBookResponse;
import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private static final String AUTH_TYPE = "KakaoAK ";

    private final KakaoBookApi kakaoBookApi;

    @Value("${book.kakao.token}")
    private String kakaoToken;

    public BookService(KakaoBookApi kakaoBookApi) {
        this.kakaoBookApi = kakaoBookApi;
    }

    public BookSearchResponses searchBook(String text, int size, int page) {
        String tokenValue = AUTH_TYPE + kakaoToken;
        KakaoBookResponse kakaoBookResponse = kakaoBookApi.getBookInformation(tokenValue, text, size, page);
        return kakaoBookResponse.toBookSearchResponsesWith(page);
    }
}
