package com.kabunx.component.web.servlet;

import com.kabunx.component.common.constant.SecurityConstants;
import com.kabunx.component.common.context.AuthContext;
import com.kabunx.component.common.context.AuthContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 提取头信息中的用户信息
 */
public class AuthHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        String userType = request.getHeader(SecurityConstants.HEADER_AUTH_TYPE);
        String userId = request.getHeader(SecurityConstants.HEADER_AUTH_ID);
        if (Objects.nonNull(userType) && Objects.nonNull(userId)) {
            AuthContext authContext = new AuthContext();
            authContext.setType(userType);
            authContext.setId(Long.valueOf(userId));
            AuthContextHolder.setCurrentAuth(authContext);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) throws Exception {
        AuthContextHolder.removeCurrentAuth();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
