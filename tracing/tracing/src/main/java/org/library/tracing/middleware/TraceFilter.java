package org.library.tracing.middleware;

import org.library.tracing.TraceIdThreadLocal;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)

public class TraceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Obtener el traceId del header si existe; de lo contrario, generar uno nuevo
        String traceId = httpRequest.getHeader("X-Trace-Id");

        if (traceId == null) {
            traceId = UUID.randomUUID().toString(); // Generar un nuevo traceId
        }
        System.out.println(traceId);
        // Colocar el traceId en el ThreadLocal para que esté disponible en los logs
        TraceIdThreadLocal.setTraceId(traceId);
MDC.put("traceId", traceId);
        // Añadir el traceId al header de la respuesta
        httpResponse.addHeader("X-Trace-Id", traceId);

        try {
            // Continuar con la cadena de filtros
            chain.doFilter(request, response);
        } finally {
            // Limpiar el ThreadLocal y el MDC al final del procesamiento para evitar contaminación
            TraceIdThreadLocal.clear();
            MDC.clear();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization required
    }

    @Override
    public void destroy() {
        // No cleanup required
    }
}
