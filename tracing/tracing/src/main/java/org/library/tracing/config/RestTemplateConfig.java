package org.library.tracing.config;

import org.library.tracing.TraceIdThreadLocal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new TraceIdInterceptor()));
        return restTemplate;
    }

    public static class TraceIdInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            String traceId = TraceIdThreadLocal.getTraceId();
            if (traceId == null) {
                traceId = UUID.randomUUID().toString(); // Si no hay traceId, generar uno nuevo
            }
            request.getHeaders().add("X-Trace-Id", traceId); // Añadir traceId a la solicitud HTTP saliente
            return execution.execute(request, body);
        }
    }
}
