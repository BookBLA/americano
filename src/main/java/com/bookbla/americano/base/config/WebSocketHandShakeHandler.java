package com.bookbla.americano.base.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class WebSocketHandShakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
        String name = httpServletRequest.getParameter("id");

        return new UserPrinciple(name);
    }

    public static class UserPrinciple implements Principal {
        public UserPrinciple(String name) {
            this.name = name;
        }

        private final String name;

        @Override
        public String getName() {
            return name;
        }
    }
}
