package com.microcode.client.secutiry;

import com.microcode.client.clients.AuthServices;
import com.microcode.client.clients.AuthorizationServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;


@Component
@RequiredArgsConstructor
@Getter
@Setter
public class Handler implements HandlerInterceptor, WebMvcConfigurer {

    private final Env env;
    private final AuthServices authServices;
    private final AuthorizationServices authorizationServices;
    private final Path path;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Handler( env,authServices, authorizationServices, path));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Env.clearAll();
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return true;
        defineCredentials(request, handler);
        if (validateUri(request.getRequestURI())) return true;
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "unauthorized");
        return false;
    }

        public void defineCredentials(HttpServletRequest request, Object handler) {
        if (request.getHeader("Authorization") == null) return;
        Env.setCurrentToken(request.getHeader("Authorization").substring(7));
        if (Boolean.FALSE.equals(authServices.validateToken())) return;
        Env.setCurrentClient(authServices.findClient());
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(RequireMail.class)) {
                Env.setCurrentMail(authServices.findMail());
            }
            if (method.isAnnotationPresent(RequireType.class)) {
                Env.setCurrentType(authServices.findType());
                Env.setCurrentPermission(authServices.findPermission());
            }
        }
    }


    private boolean validateUri(String requestUri) {
        if(path.getOpenForUrl(requestUri)) return true;
        return authorizationServices.validateAccessRoute(path.getAuthorizedForUrl(requestUri));
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequireMail {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequireType {
    }

}