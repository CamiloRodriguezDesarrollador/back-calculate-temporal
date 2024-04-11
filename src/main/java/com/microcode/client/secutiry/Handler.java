package com.microcode.client.secutiry;

import com.microcode.client.clients.AuthServices;
import com.microcode.client.clients.AuthorizationServices;
import com.microcode.client.clients.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
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
public class Handler implements HandlerInterceptor , WebMvcConfigurer {

    public final Env env;
    public final AuthServices authServices;
    public final UserServices userService;
    public final AuthorizationServices authorizationServices;
    public final Path path;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Handler(env, authServices, userService, authorizationServices, path));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Env.clearAll();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        defineCredentials(request, (HandlerMethod) handler);
        if (validateUri(request.getRequestURI())) return true;
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "unauthorized");
        return false;
    }

    public void defineCredentials(HttpServletRequest request, HandlerMethod handler) {
        if (request.getHeader("Authorization") == null) return;
        Env.setCurrentToken(request.getHeader("Authorization").substring(7));
        if (Boolean.FALSE.equals(authServices.validateToken())) return;

        Env.setCurrentClient(authServices.findClient());
        if (handler != null) {
            Method method = handler.getMethod();
            if (method.isAnnotationPresent(RequireMail.class)) {
                Env.setCurrentMail(authServices.findMail());
            }
            if (method.isAnnotationPresent(RequireUser.class)) {
                Env.setCurrentMail(authServices.findMail());
                Env.setCurrentUser(userService.findForMail(Env.getCurrentMail()));
            }
            if (method.isAnnotationPresent(RequireType.class)) {
                env.setTypeClient(authServices.findType());

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
    public @interface RequireUser {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequireType {
    }

}