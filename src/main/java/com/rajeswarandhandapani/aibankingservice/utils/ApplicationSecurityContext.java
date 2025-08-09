package com.rajeswarandhandapani.aibankingservice.utils;

import org.springframework.stereotype.Component;

@Component
public class ApplicationSecurityContext {

    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();
    private static final ThreadLocal<String> jwt = new ThreadLocal<>();

    public static String getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(String userId) {
        currentUser.set(userId);
    }

    public static String getJwt() {
        return jwt.get();
    }

    public static void setJwt(String jwtToken) {
        jwt.set(jwtToken);
    }

    public static void clear() {
        currentUser.remove();
        jwt.remove();
    }

}
