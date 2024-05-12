package com.bookbla.americano.base.filter;

import java.io.IOException;

import com.bookbla.americano.base.log.RequestStorage;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@RequiredArgsConstructor
@Component
public class ServletWrappingFilter extends OncePerRequestFilter {

    private final RequestStorage requestStorage;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        requestStorage.set(wrappedRequest);

        filterChain.doFilter(wrappedRequest, response);
    }
}
