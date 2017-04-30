package com.proxytest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

@RestController
public class RouterController implements ApplicationContextAware {

    private final IRouter router;
    private ApplicationContext applicationContext;

    @Autowired
    public RouterController(IRouter router) {
        this.router = router;
    }
    @GetMapping("/test")
    public ResponseEntity<Integer> test() {
        return new ResponseEntity(router.route(), HttpStatus.OK);
    }

    @GetMapping("/init")
    public ResponseEntity<Void> setValue(@RequestParam("key") String key,
                                         @RequestParam("val") Integer value,
                                         @RequestParam("weight") Integer weight) {
        RouterProxy proxy = applicationContext
                .getBeansOfType(RouterProxy.class)
                .values().iterator().next();

        proxy.newRoute(new AbstractMap.SimpleEntry<>(key, buildMethod(value)), weight);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private MethodInterceptor buildMethod(Integer value) {
        return new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                return value;
            }
        };
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
