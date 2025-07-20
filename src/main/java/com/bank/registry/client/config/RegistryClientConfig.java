package com.bank.registry.client.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Getter
@Component
public class RegistryClientConfig implements EnvironmentAware {
    private final String host;
    private final String port;
    private final String serviceName;
    private final String registryUrl;

    private Environment environment;

    public RegistryClientConfig(
            @Value("${server.port:8080}") String port,
            @Value("${spring.application.name:unknown-service}") String serviceName,
            @Value("${registry.server.url:http://localhost:8787}") String registryUrl
    ) throws Exception {
        this.host = InetAddress.getLocalHost().getHostAddress();
        this.port = port;
        this.serviceName = serviceName;
        this.registryUrl = registryUrl;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
