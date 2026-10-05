package com.orgzly.org;

import com.orgzly.org.utils.ArrayListSpaceSeparated;

/**
 * State keywords, single workflow.
 *
 * "NEXT" "FEEDBACK" "VERIFY" "|" "DONE" "DELEGATED"
 */
public class OrgStatesWorkflow {
    private ArrayListSpaceSeparated todoKeywords;
    private ArrayListSpaceSeparated doneKeywords;

    public OrgStatesWorkflow(ArrayListSpaceSeparated t, ArrayListSpaceSeparated d) {
        todoKeywords = t;
        doneKeywords = d;
    }

    /** {@code TODO(t)} and {@code WAIT(w@/!)} name the states {@code TODO} and {@code WAIT}. */
    private static String withoutFastAccessKey(String keyword) {
        if (keyword.endsWith(")")) {
            int open = keyword.indexOf('(');
            if (open >= 0) {
                return keyword.substring(0, open);
            }
        }
        return keyword;
    }

    /** A token that is only a key, such as {@code (t)}, names no state and is dropped. */
    private static ArrayListSpaceSeparated keywords(String s) {
        ArrayListSpaceSeparated list = new ArrayListSpaceSeparated();
        for (String keyword: new ArrayListSpaceSeparated(s)) {
            String stripped = withoutFastAccessKey(keyword);
            if (stripped.length() > 0) {
                list.add(stripped);
            }
        }
        return list;
    }

    public OrgStatesWorkflow(String s) {
        String st = s.trim();

        if (st.length() == 0) {
            todoKeywords = new ArrayListSpaceSeparated();
            doneKeywords = new ArrayListSpaceSeparated();

        } else {
            int bar = st.indexOf('|');

            if (bar == -1) { // No vertical bar - use last keyword as done state
                todoKeywords = keywords(st);
                doneKeywords = new ArrayListSpaceSeparated();

                if (!todoKeywords.isEmpty()) {
                    doneKeywords.add(todoKeywords.remove(todoKeywords.size() - 1));
                }

            } else {
                todoKeywords = keywords(st.substring(0, bar));
                doneKeywords = keywords(st.substring(bar+1));
            }
        }
    }

    public ArrayListSpaceSeparated getTodoKeywords() {
        return todoKeywords;
    }

    public ArrayListSpaceSeparated getDoneKeywords() {
        return doneKeywords;
    }

    public String toString() {
        return todoKeywords.toString() + " | " + doneKeywords.toString();
    }
}
