package com.proxytest;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.stereotype.Component;

@Component
public class IntegerMethodInvocationGenerator implements RouterTargetGenerator<MethodInterceptor>{

    @Override
    public MethodInterceptor generate(String key) {
        return (invocation) -> Integer.valueOf(key);
    }
}
