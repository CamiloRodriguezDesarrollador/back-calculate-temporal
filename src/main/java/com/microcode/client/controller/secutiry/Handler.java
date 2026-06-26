package com.microcode.client.controller.secutiry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class Handler implements HandlerInterceptor, WebMvcConfigurer {

        private static final String TOKEN_VALIDO = "mc_9f3A2kL8QwR7XyP2ZJdM8saduf9dshf9sdbnhduasdosjaJIHsjhs";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String token = request.getHeader("X-Current-Token");

        if (!TOKEN_VALIDO.equals(token)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden");
            return false;
        }

        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this)
                .excludePathPatterns("/api/chat/action/**");
    }
}
