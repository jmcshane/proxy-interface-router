package com.proxytest;

import org.aopalliance.intercept.MethodInterceptor;

public interface RouterTargetGenerator<T extends MethodInterceptor> {
    T generate(String key);
}
