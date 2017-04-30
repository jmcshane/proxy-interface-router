package com.proxytest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class ProxyRouterBean<T extends MethodInterceptor> extends AbstractRouterProxy {

    private final Map<String, MethodInterceptor> interceptors;
    private final RouterTargetGenerator<T> generator;
    private final List<String> randomInterceptorSelector;
    private Integer index = 0;

    public ProxyRouterBean(RouterTargetGenerator<T> generator) {
        this.generator = generator;
        this.randomInterceptorSelector = new ArrayList<>();
        this.interceptors = new HashMap<>();
    }

    public void modifyWeight(String key, int weight) {
        while (randomInterceptorSelector.contains(key)) {
            randomInterceptorSelector.remove(key);
        }
        IntStream.range(0, weight).forEach(i -> randomInterceptorSelector.add(key));
        Collections.shuffle(randomInterceptorSelector, new Random(System.nanoTime()));
        index = 0;
    }

    public void newRoute(String generationKey, int weight) {
        interceptors.put(generationKey, generator.generate(generationKey));
        modifyWeight(generationKey, weight);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (randomInterceptorSelector.isEmpty()) {
            throw new IllegalAccessException("No interceptor provided");
        }
        if (index >= randomInterceptorSelector.size()) {
            index = 0;
        }
        String key = randomInterceptorSelector.get(index);
        index++;
        return interceptors.get(key).invoke(invocation);
    }
}
