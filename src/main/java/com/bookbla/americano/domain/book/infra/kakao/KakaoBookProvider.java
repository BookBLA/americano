package com.bookbla.americano.domain.book.infra.kakao;


import com.bookbla.americano.domain.book.infra.kakao.dto.KakaoBookResponse;
import com.bookbla.americano.domain.book.service.BookProvider;
import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoBookProvider implements BookProvider {

    private static final String AUTH_TYPE = "KakaoAK ";

    private final KakaoBookClient kakaoBookClient;
    private final String token;

    public KakaoBookProvider(KakaoBookClient kakaoBookClient, @Value("${book.kakao.token}") String token) {
        this.kakaoBookClient = kakaoBookClient;
        this.token = token;
    }

    @Override
    public BookSearchResponses searchBooks(String text, int size, int page) {
        String tokenValue = AUTH_TYPE + token;
        KakaoBookResponse kakaoBookResponse = kakaoBookClient.getBookInformation(tokenValue, text, size, page);
        return kakaoBookResponse.toBookSearchResponsesWith(page);
    }

}
