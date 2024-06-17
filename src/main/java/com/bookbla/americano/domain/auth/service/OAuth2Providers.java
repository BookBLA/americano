package com.bookbla.americano.domain.auth.service;

import com.bookbla.americano.domain.member.enums.MemberType;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Providers {

    private final Map<MemberType, OAuth2Provider> oAuthProviderMap;

    public OAuth2Providers(Set<OAuth2Provider> oAuth2Providers) {
        oAuthProviderMap = oAuth2Providers.stream()
                .collect(Collectors.toMap(
                        OAuth2Provider::getMemberType,
                        Function.identity()
                ));
    }

    public OAuth2Provider getProvider(MemberType memberType) {
        return oAuthProviderMap.get(memberType);
    }
}
