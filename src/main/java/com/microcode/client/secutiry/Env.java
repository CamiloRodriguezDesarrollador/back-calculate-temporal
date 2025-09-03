package com.microcode.client.secutiry;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Setter
@Service
public class Env {

    private static final ThreadLocal<Integer> currentClient = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<String> currentMail = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Integer> currentType = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<String> currentToken = ThreadLocal.withInitial(() -> null);

    public static String getCurrentToken() {
        return currentToken.get();
    }

    public static void setCurrentToken(String token) {
        currentToken.set(token);
    }


    public static String getCurrentMail() {
        return currentMail.get();
    }

    public static void setCurrentMail(String mail) {
        currentMail.set(mail);
    }

    public static Integer getCurrentClient() {
        return currentClient.get();
    }

    public static void setCurrentClient(Integer client) {
        currentClient.set(client);
    }

    public static Integer getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(Integer user) {
        currentUser.set(user);
    }

    public static Integer getCurrentType() {
        return currentType.get();
    }

    public static void setCurrentType(Integer user) {
        currentType.set(user);
    }
    public static void clearAll() {
        currentClient.remove();
        currentUser.remove();
        currentMail.remove();
        currentType.remove();
    }

    public static WebClient.Builder withCurrentHeaders(WebClient.Builder builder) {
        if (getCurrentUser() != null) {
            builder.defaultHeader("X-Current-User", getCurrentUser().toString());
        }
        if (getCurrentMail() != null) {
            builder.defaultHeader("X-Current-Mail", getCurrentMail());
        }
        if (getCurrentClient() != null) {
            builder.defaultHeader("X-Current-Client", getCurrentClient().toString());
        }
        if (getCurrentType() != null) {
            builder.defaultHeader("X-Current-Type", getCurrentType().toString());
        }
        return builder;
    }


}