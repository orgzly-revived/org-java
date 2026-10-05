package com.orgzly.org.utils;

import com.orgzly.org.datetime.OrgRange;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StateChangeLogicTest {

    @Test
    public void testFromTodoToNext() {
        StateChangeLogic scl  = new StateChangeLogic(Collections.singleton("DONE"));
        scl.setState("NEXT", "TODO", null, null);
        assertEquals("NEXT", scl.getState());
        assertNull(scl.getClosed());
    }

    @Test
    public void testFromTodoToDone() {
        StateChangeLogic scl  = new StateChangeLogic(Collections.singleton("DONE"));
        scl.setState("DONE", "TODO", null, null);
        assertEquals("DONE", scl.getState());
        assertNotNull(scl.getClosed());
    }

    @Test
    public void testFromDoneToCncl() {
        StateChangeLogic scl  = new StateChangeLogic(Arrays.asList("DONE", "CNCL"));
        scl.setState("CNCL", "DONE", null, null);
        assertEquals("CNCL", scl.getState());
        assertNotNull(scl.getClosed());
    }

    @Test
    public void testFromNoteToDoneWithRepeater() {
        StateChangeLogic scl  = new StateChangeLogic(Collections.singleton("DONE"));
        scl.setState("DONE", "NOTE", OrgRange.parse("<2018-02-06 Tue +7d>"), null);
        assertEquals("NOTE", scl.getState());
        assertEquals("<2018-02-13 Tue +7d>", scl.getScheduled().toString());
        assertNull(scl.getClosed());
    }

    @Test
    public void testFromNoteToDoneTimestampsWithRepeater() {
        StateChangeLogic scl  = new StateChangeLogic(Collections.singleton("DONE"));
        scl.setState(
                "DONE",
                "NOTE",
                OrgRange.parse("<2018-02-06 Tue +7d>"),
                null,
                Arrays.asList(
                        OrgRange.parse("<2018-02-06 Tue +1d>"),
                        OrgRange.parse("<2018-02-06 Tue +7d>")));
        assertEquals("NOTE", scl.getState());
        assertEquals("<2018-02-13 Tue +7d>", scl.getScheduled().toString());
        assertEquals("<2018-02-07 Wed +1d>", scl.getTimestamps().get(0).toString());
        assertEquals("<2018-02-13 Tue +7d>", scl.getTimestamps().get(1).toString());
        assertNull(scl.getClosed());
    }

    @Test
    public void testFromNoteToTodoWithRepeater() {
        StateChangeLogic scl  = new StateChangeLogic(Collections.singleton("DONE"));
        scl.setState("NEXT", "NOTE", OrgRange.parse("<2018-02-06 Tue +7d>"), null);
        assertEquals("NEXT", scl.getState());
        assertEquals("<2018-02-06 Tue +7d>", scl.getScheduled().toString());
        assertNull(scl.getClosed());
    }

    @Test
    public void testFromTodoToDoneWithoutLogging() {
        StateChangeLogic scl = new StateChangeLogic(Collections.singleton("DONE"), LogDone.NONE);
        scl.setState("DONE", "TODO", null, null);
        assertEquals("DONE", scl.getState());
        assertNull(scl.getClosed());
    }

    @Test
    public void testFromDoneToCnclWithoutLoggingKeepsClosedTime() {
        StateChangeLogic scl = new StateChangeLogic(Arrays.asList("DONE", "CNCL"), LogDone.NONE);
        OrgRange closed = OrgRange.parse("[2018-02-06 Tue 10:00]");
        scl.setState("CNCL", "DONE", null, null, closed, Collections.emptyList());
        assertEquals("CNCL", scl.getState());
        assertEquals("[2018-02-06 Tue 10:00]", scl.getClosed().toString());
    }

    @Test
    public void testFromDoneToTodoWithoutLoggingClearsClosedTime() {
        StateChangeLogic scl = new StateChangeLogic(Collections.singleton("DONE"), LogDone.NONE);
        OrgRange closed = OrgRange.parse("[2018-02-06 Tue 10:00]");
        scl.setState("TODO", "DONE", null, null, closed, Collections.emptyList());
        assertEquals("TODO", scl.getState());
        assertNull(scl.getClosed());
    }

    @Test
    public void testRepeaterStillShiftsWithoutLogging() {
        StateChangeLogic scl = new StateChangeLogic(Collections.singleton("DONE"), LogDone.NONE);
        scl.setState("DONE", "NOTE", OrgRange.parse("<2018-02-06 Tue +7d>"), null);
        assertTrue(scl.isShifted());
        assertEquals("NOTE", scl.getState());
        assertEquals("<2018-02-13 Tue +7d>", scl.getScheduled().toString());
        assertNull(scl.getClosed());
    }

    @Test
    public void testNoteLogsTheClosedTimeLikeTime() {
        StateChangeLogic scl = new StateChangeLogic(Collections.singleton("DONE"), LogDone.NOTE);
        scl.setState("DONE", "TODO", null, null);
        assertEquals("DONE", scl.getState());
        assertNotNull(scl.getClosed());
    }
}
