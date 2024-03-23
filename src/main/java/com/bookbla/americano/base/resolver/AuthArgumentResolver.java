package com.bookbla.americano.base.resolver;

import com.bookbla.americano.base.jwt.BearerTokenExtractor;
import com.bookbla.americano.base.jwt.JwtProvider;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    // Interceptor -> ArgumentResolver -> Controller
    // 요청에 담긴 인자를 해독해 컨트롤러로 넘기기 위해 구현

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(User.class)
                && parameter.getParameterType().equals(LoginUser.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = BearerTokenExtractor.extract(Objects.requireNonNull(request));

        String id = jwtProvider.decodeToken(accessToken);
        Long memberId = Long.valueOf(id);
        return new LoginUser(memberId);
    }
}
