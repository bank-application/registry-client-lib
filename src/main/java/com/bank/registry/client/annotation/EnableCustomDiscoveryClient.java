package com.bank.registry.client.annotation;

import com.bank.registry.client.config.RegistryClientConfig;
import com.bank.registry.client.service.RegistryClientService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RegistryClientConfig.class, RegistryClientService.class})
public @interface EnableCustomDiscoveryClient {
}
