package com.microcode.client.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.InetSocketAddress;
import java.util.Map;

public class IpHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String clientIp = null;
        if (request instanceof org.springframework.http.server.ServletServerHttpRequest servletRequest) {
            String xForwardedFor = servletRequest.getServletRequest().getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                clientIp = xForwardedFor.split(",")[0].trim();
            } else {
                InetSocketAddress remoteAddress = request.getRemoteAddress();
                clientIp = remoteAddress.getAddress().getHostAddress();
            }
        }

        if (clientIp != null) attributes.put("clientIp", clientIp);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
