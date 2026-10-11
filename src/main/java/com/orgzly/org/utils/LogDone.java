package com.orgzly.org.utils;

/**
 * What to record when a note moves to a done state, as org's {@code org-log-done}.
 */
public enum LogDone {
    NONE,

    TIME,

    /** A closed time and a note. Callers that cannot prompt should treat it as {@link #TIME}. */
    NOTE;

    /**
     * Reads one {@code #+STARTUP:} or {@code LOGGING} token.
     *
     * @return null for a token unrelated to done-logging, so "not set" differs from "off"
     */
    public static LogDone fromToken(String token) {
        if (token == null) {
            return null;
        }

        switch (token) {
            case "nologdone":
                return NONE;
            case "logdone":
                return TIME;
            case "lognotedone":
                return NOTE;
            default:
                return null;
        }
    }
}
