package com.bookbla.americano.base.log;

import lombok.Getter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Getter
public class RequestStorage {

    private ContentCachingRequestWrapper request;

    public RequestStorage set(ContentCachingRequestWrapper request) {
        this.request = request;
        return this;
    }
}
