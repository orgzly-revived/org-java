package com.orgzly.org.utils;

import com.orgzly.org.datetime.OrgDateTime;
import com.orgzly.org.datetime.OrgRange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * State setting logic.
 */
public class StateChangeLogic {
    private final Collection<String> doneKeywords;
    private final LogDone logDone;

    private String state;

    private OrgRange scheduled;
    private OrgRange deadline;
    private List<OrgRange> timestamps;
    private OrgRange closed;

    private boolean shifted = false;

    public StateChangeLogic(Collection<String> doneKeywords) {
        this(doneKeywords, LogDone.TIME);
    }

    public StateChangeLogic(Collection<String> doneKeywords, LogDone logDone) {
        this.doneKeywords = doneKeywords;
        this.logDone = logDone;
    }

    public void setState(
            String targetState,
            String originalState,
            OrgRange scheduledTime,
            OrgRange deadlineTime) {

        setState(targetState, originalState, scheduledTime, deadlineTime, new ArrayList<>());
    }

    public void setState(
            String targetState,
            String originalState,
            OrgRange scheduledTime,
            OrgRange deadlineTime,
            List<OrgRange> timestamps) {

        setState(targetState, originalState, scheduledTime, deadlineTime, null, timestamps);
    }

    /** {@link LogDone#NONE} keeps {@code closedTime} on a move into a done state. */
    public void setState(
            String targetState,
            String originalState,
            OrgRange scheduledTime,
            OrgRange deadlineTime,
            OrgRange closedTime,
            List<OrgRange> timestamps) {

        this.scheduled = scheduledTime;
        this.deadline = deadlineTime;
        this.timestamps = timestamps;

        if (targetState != null && doneKeywords.contains(targetState)) {
            if (! doneKeywords.contains(originalState)) { // to-do -> done
                /*
                 * Try to shift times. If successful (there was a repeater), keep the original
                 * state and remove closed time. If times were not shifted (there was no repeater)
                 * update the state and set closed time.
                 */

                if (scheduled != null) {
                    if (scheduled.shift()) {
                        shifted = true;
                    }
                }

                if (deadline != null) {
                    if (deadline.shift()) {

                        // Scheduled time exists but has no repeater - remove it
                        if (scheduled != null && !shifted) {
                            scheduled = null;
                        }

                        shifted = true;
                    }
                }

                for (OrgRange timestamp: timestamps) {
                    if (timestamp.shift()) {
                        shifted = true;
                    }
                }

                if (shifted) {
                    /* Keep the original state and remove the closed time */
                    state = originalState;
                    closed = null;
                } else {
                    /* Set state and closed time. */
                    state = targetState;
                    closed = closedOnDone(closedTime);
                }

            } else { // done -> done
                /*
                 * Set the state and the closed time.
                 */
                state = targetState;
                closed = closedOnDone(closedTime);
            }

        } else { // -> to-do
            /*
             * Set state and remove closed time.
             */
            state = targetState;
            closed = null;
        }
    }

    private OrgRange closedOnDone(OrgRange current) {
        return logDone == LogDone.NONE ? current : new OrgRange(new OrgDateTime(false));
    }

    public String getState() {
        return state;
    }

    public OrgRange getScheduled() {
        return scheduled;
    }

    public OrgRange getDeadline() {
        return deadline;
    }

    public List<OrgRange> getTimestamps() {
        return timestamps;
    }

    public OrgRange getClosed() {
        return closed;
    }

    public boolean isShifted() {
        return shifted;
    }
}