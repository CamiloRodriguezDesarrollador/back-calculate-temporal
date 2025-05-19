package com.microcode.client.secutiry;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class Env {

    private static final ThreadLocal<String> currentToken = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Integer> currentClient = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Integer> currentType = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Integer> currentPermission = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<String> currentMail = ThreadLocal.withInitial(() -> null);

//    private static final ThreadLocal<Integer> currentClient = ThreadLocal.withInitial(() -> 1);
//    private static final ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> 1);
//    private static final ThreadLocal<Integer> currentType = ThreadLocal.withInitial(() -> 1);
//    private static final ThreadLocal<Integer> currentPermission = ThreadLocal.withInitial(() -> 1);
//    private static final ThreadLocal<String> currentMail = ThreadLocal.withInitial(() -> "cgonzalez@activos.com.co");


    public static String getCurrentMail() {
        return currentMail.get();
    }

    public static void setCurrentMail(String mail) {
        currentMail.set(mail);
    }
    public static String getCurrentToken() {
        return currentToken.get();
    }

    public static void setCurrentToken(String token) {
        currentToken.set(token);
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

    public static Integer getCurrentType() {
        return currentType.get();
    }

    public static void setCurrentType(Integer user) {
        currentType.set(user);
    }


    public static void setCurrentUser(Integer user) {
        currentUser.set(user);
    }

    public static Integer getCurrentPermission() {
        return currentPermission.get();
    }

    public static void setCurrentPermission(Integer permission) {
        currentPermission.set(permission);
    }
    public static void clearAll() {
        currentToken.remove();
        currentClient.remove();
        currentUser.remove();
        currentMail.remove();
        currentPermission.remove();
    }


}