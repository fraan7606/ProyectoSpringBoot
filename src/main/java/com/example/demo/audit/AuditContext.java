package com.example.demo.audit;

public final class AuditContext {
    private static final ThreadLocal<String> ACTOR = new ThreadLocal<>();

    private AuditContext() {
    }

    public static void setActor(String actor) {
        ACTOR.set(actor);
    }

    public static String getActor() {
        return ACTOR.get();
    }

    public static void clear() {
        ACTOR.remove();
    }
}