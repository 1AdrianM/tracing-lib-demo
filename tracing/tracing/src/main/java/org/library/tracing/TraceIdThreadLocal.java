package org.library.tracing;

public class TraceIdThreadLocal {
    private static final ThreadLocal<String> threadLocalTraceId = new ThreadLocal<>();

    public static void setTraceId(String traceId) {
        threadLocalTraceId.set(traceId);
    }

    public static String getTraceId() {
        return threadLocalTraceId.get();
    }

    public static void clear() {
        threadLocalTraceId.remove();
    }
}
