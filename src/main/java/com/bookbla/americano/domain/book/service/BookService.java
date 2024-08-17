package com.bookbla.americano.domain.book.service;

import com.bookbla.americano.domain.book.infra.kakao.KakaoBookClient;
import com.bookbla.americano.domain.book.infra.kakao.dto.KakaoBookResponse;
import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {

    private final KakaoBookClient kakaoBookClient;

    public BookSearchResponses searchBook(String text, int size, int page) {
        KakaoBookResponse kakaoBookResponse = kakaoBookClient.searchBook(text, size, page);
        return kakaoBookResponse.toBookSearchResponsesWith(page);
    }
}
