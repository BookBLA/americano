package com.bookbla.americano.base.log.query;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;

@RequiredArgsConstructor
public class ConnectionProxyHandler implements InvocationHandler {

    private static final String QUERY_PREPARE_STATEMENT = "prepareStatement";

    private final Object connection;
    private final QueryCounter queryCounter;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        countQuery(method);
        return method.invoke(connection, args);
    }

    private void countQuery(Method method) {
        if (isPrepareStatement(method) && isRequest()) {
            queryCounter.increaseCount();
        }
    }

    private boolean isPrepareStatement(Method method) {
        return method.getName().equals(QUERY_PREPARE_STATEMENT);
    }

    private boolean isRequest() {
        return RequestContextHolder.getRequestAttributes() != null;
    }

}
