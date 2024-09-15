package com.bookbla.americano.domain.auth.infra.kakao;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.auth.exception.KakaoExceptionType;
import com.bookbla.americano.domain.auth.infra.kakao.dto.KakaoMemberResponse;
import com.bookbla.americano.domain.auth.infra.kakao.dto.KakaoOAuth2MemberResponse;
import com.bookbla.americano.domain.auth.service.OAuth2Provider;
import com.bookbla.americano.domain.member.enums.MemberType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuth2Provider implements OAuth2Provider {

    private final KakaoConfig kakaoConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public KakaoOAuth2MemberResponse getMemberResponse(String accessToken) {
        ResponseEntity<KakaoMemberResponse> kakaoMemberResponse = getMemberResource(accessToken);
        return new KakaoOAuth2MemberResponse(kakaoMemberResponse.getBody().getEmail(),
                kakaoMemberResponse.getBody().getProfileImageUrl());
    }

    private ResponseEntity<KakaoMemberResponse> getMemberResource(String accessToken) {
        String resourceUri = kakaoConfig.getResourceUri();

        HttpHeaders headers = createRequestHeader();
        headers.setBearerAuth(accessToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(
                    resourceUri,
                    HttpMethod.GET,
                    request,
                    KakaoMemberResponse.class
            );
        } catch (HttpClientErrorException e) {
            throw new BaseException(KakaoExceptionType.MEMBER_RESOURCE_ERROR);
        }
    }

    private HttpHeaders createRequestHeader() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return header;
    }

    @Override
    public MemberType getMemberType() {
        return MemberType.KAKAO;
    }
}
