package com.proxytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ClassUtils;

@SpringBootApplication
public class ProxyInterfaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyInterfaceApplication.class, args);
	}

	@Bean("proxyRouterName")
	public ProxyRouterBean routerBean() {
		ProxyRouterBean bean = new ProxyRouterBean(null);
		bean.setBeanClassLoader(ClassUtils.getDefaultClassLoader());
		bean.setBusinessInterface(IProxiedService.class);
		return bean;
	}
}
