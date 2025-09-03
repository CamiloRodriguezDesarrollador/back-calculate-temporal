package com.microcode.client.secutiry;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Nullable;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class Handler implements HandlerInterceptor, WebMvcConfigurer {

    private final Env env;

    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }

    @Override public boolean preHandle(@Nullable HttpServletRequest request,
                                       @Nullable HttpServletResponse response,
                                       @Nullable Object handler) {
        assert request != null;
        String userHeader = request.getHeader("X-Current-User");
        if (userHeader != null) Env.setCurrentUser(Integer.valueOf(userHeader));

        Env.setCurrentMail(request.getHeader("X-Current-Mail"));

        String client = request.getHeader("X-Current-Client");
        if (client != null) Env.setCurrentClient(Integer.valueOf(client));

        String type = request.getHeader("X-Current-Type");
        if (type != null) Env.setCurrentType(Integer.valueOf(type));

        String token = request.getHeader("X-Current-Token");
        if (token != null) Env.setCurrentToken(token);

        return true;

    }

    @Override public void afterCompletion(@Nullable HttpServletRequest request,
                                          @Nullable HttpServletResponse response,
                                          @Nullable Object handler, Exception ex) {
        Env.clearAll();
    }
}