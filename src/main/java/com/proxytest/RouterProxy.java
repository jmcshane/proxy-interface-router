package com.proxytest;

import org.aopalliance.intercept.MethodInterceptor;

import java.util.Map;
import java.util.stream.IntStream;

public interface RouterProxy {
    void modifyWeight(String key, int weight);

    void newRoute(Map.Entry<String, MethodInterceptor> interceptor, int weight);

}
