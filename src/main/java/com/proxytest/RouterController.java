package com.proxytest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouterController implements ApplicationContextAware {

    private final IProxiedService router;
    private ApplicationContext applicationContext;

    @Autowired
    public RouterController(IProxiedService router) {
        this.router = router;
    }
    @GetMapping("/test")
    public ResponseEntity<Integer> test() {
        return new ResponseEntity(router.value(), HttpStatus.OK);
    }

    @GetMapping("/init")
    public ResponseEntity<Void> setValue(
        @RequestParam("key") String key,
        @RequestParam("weight") Integer weight) {

        RouterProxy proxy = applicationContext
                .getBeansOfType(RouterProxy.class)
                .values().iterator().next();

        proxy.newRoute(key, weight);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
