package com.bank.registry.client.service;

import com.bank.registry.client.config.RegistryClientConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RegistryClientService {

    private final ObjectMapper objectMapper;
    private final RegistryClientConfig registryClientConfig;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public RegistryClientService(ObjectMapper objectMapper, RegistryClientConfig registryClientConfig) {
        this.objectMapper = new ObjectMapper();
        this.registryClientConfig = registryClientConfig;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void register() {
        String serviceRegistryUrl = null;
        try {
            serviceRegistryUrl = registryClientConfig.getRegistryUrl();

            Map<String, Object> serviceInfo = new LinkedHashMap<>();
            serviceInfo.put("serviceName", registryClientConfig.getServiceName());
            serviceInfo.put("host", registryClientConfig.getHost());
            serviceInfo.put("port", Integer.parseInt(registryClientConfig.getPort()));

            String requestBody = objectMapper.writeValueAsString(serviceInfo);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serviceRegistryUrl + "/registry/register"))
                    .headers("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("Successfully registered service to registry server {}.", serviceRegistryUrl);
            } else {
                log.warn("Failed to register service to registry server. Status: {}, Body: {}", response.statusCode(), response.body());
            }
        } catch (Exception e) {
            log.error("Failed to register service to registry server {} : {}", serviceRegistryUrl, e.getMessage());
        }
    }
}
