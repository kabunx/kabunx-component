package com.kabunx.component.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * 追踪信息工具类
 */
public class TraceUtils {

    /**
     * 需要保证该字段和日志配置的字段相同
     */
    private static final String TRACE_ID = "traceId";

    public static void setTraceId(String traceId) {
        if (StringUtils.isBlank(traceId)) {
            traceId = RandomUtils.uuid().toLowerCase();
        }
        MDC.put(TRACE_ID, traceId);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void removeTraceId() {
        MDC.remove(TRACE_ID);
    }
}
