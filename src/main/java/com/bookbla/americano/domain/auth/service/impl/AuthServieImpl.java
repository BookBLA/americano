package com.bookbla.americano.domain.auth.service.impl;

import com.bookbla.americano.base.config.KakaoConfig;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.domain.auth.service.AuthService;
import com.bookbla.americano.domain.auth.service.dto.KakaoMemberResponse;
import com.bookbla.americano.domain.auth.service.dto.KakaoTokenResponse;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.enums.MemberType;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServieImpl implements AuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final KakaoConfig kakaoConfig;
    private final MemberRepository memberRepository;

    @Override
    public Long kakaoLogin(String code)  {
        String accessToken = getAccessToken(code);

        ResponseEntity<KakaoMemberResponse> kakaoMemberResponse = getMemberResource(accessToken);

        String oAuthEmail = kakaoMemberResponse.getBody().getEmail();

        Member member = memberRepository.findByOauthEmail(oAuthEmail)
            .orElseGet(() -> memberRepository.save(Member.builder()
                .oauthEmail(oAuthEmail)
                .memberType(MemberType.KAKAO)
                .build()));

        return member.getId();
    }

    private String getAccessToken(String authCode) {
        HttpHeaders header = createRequestHeader();
        MultiValueMap<String, String> body = createRequestBody(authCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, header);
        ResponseEntity<KakaoTokenResponse> kakaoTokenResponse = getKakaoTokenResponse(request);

        return Objects.requireNonNull(kakaoTokenResponse.getBody()).getAccessToken();
    }

    private ResponseEntity<KakaoTokenResponse> getKakaoTokenResponse(HttpEntity<MultiValueMap<String, String>> request) {
        String tokenUri = kakaoConfig.getTokenUri();

        try {
            return restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                request,
                KakaoTokenResponse.class
            );
        } catch (HttpClientErrorException e) {
            throw new BaseException(BaseExceptionType.TEST_FAIL);
        }
    }

    private ResponseEntity<KakaoMemberResponse> getMemberResource(String accessToken) {
        String resourceUri = kakaoConfig.getResourceUri();

        HttpHeaders headers = new HttpHeaders();
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
            throw new BaseException(BaseExceptionType.TEST_FAIL);
        }
    }

    private HttpHeaders createRequestHeader() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return header;
    }

    private MultiValueMap<String, String> createRequestBody(String authCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authCode);
        body.add("client_id", kakaoConfig.getClientId());
        body.add("client_secret", kakaoConfig.getClientSecret());
        body.add("redirect_uri", kakaoConfig.getRedirectUri());
        body.add("grant_type", kakaoConfig.getGrantType());
        return body;
    }

}
