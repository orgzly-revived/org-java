package com.orgzly.org.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LogDoneTest {

    @Test
    public void testDoneTokens() {
        assertEquals(LogDone.NONE, LogDone.fromToken("nologdone"));
        assertEquals(LogDone.TIME, LogDone.fromToken("logdone"));
        assertEquals(LogDone.NOTE, LogDone.fromToken("lognotedone"));
    }

    /** Tokens governing other kinds of logging must be distinguishable from "off". */
    @Test
    public void testUnrelatedTokens() {
        assertNull(LogDone.fromToken("logrepeat"));
        assertNull(LogDone.fromToken("lognoterepeat"));
        assertNull(LogDone.fromToken("logdrawer"));
        assertNull(LogDone.fromToken("overview"));
        assertNull(LogDone.fromToken("nil"));
        assertNull(LogDone.fromToken(""));
        assertNull(LogDone.fromToken(null));
    }

    @Test
    public void testTokensAreCaseSensitive() {
        assertNull(LogDone.fromToken("NOLOGDONE"));
        assertNull(LogDone.fromToken("LogDone"));
    }
}
