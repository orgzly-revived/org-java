package com.orgzly.org.parser;

import com.orgzly.org.OrgHead;
import com.orgzly.org.OrgTestParser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/** The test parser knows only TODO and DONE, so any other state found here came from the file. */
public class OrgFileWorkflowTest extends OrgTestParser {

    private OrgHead firstHead(String file) throws IOException {
        return parserBuilder.setInput(file).build().parse().getHeadsInList().get(0).getHead();
    }

    @Test
    public void testStateFromFileIsRecognised() throws IOException {
        OrgHead head = firstHead("#+TODO: NEXT | CNCL\n\n* CNCL Abandoned\n");

        Assert.assertEquals("CNCL", head.getState());
        Assert.assertEquals("Abandoned", head.getTitle());
    }

    @Test
    public void testTodoSideFromFileIsRecognised() throws IOException {
        OrgHead head = firstHead("#+TODO: NEXT | CNCL\n\n* NEXT Do this\n");

        Assert.assertEquals("NEXT", head.getState());
        Assert.assertEquals("Do this", head.getTitle());
    }

    @Test
    public void testConfiguredStateIsNotRecognisedWhenFileDeclaresItsOwn() throws IOException {
        OrgHead head = firstHead("#+TODO: NEXT | CNCL\n\n* TODO Not a state here\n");

        Assert.assertNull(head.getState());
        Assert.assertEquals("TODO Not a state here", head.getTitle());
    }

    @Test
    public void testConfiguredWorkflowSurvivesAFileDeclaringNone() throws IOException {
        OrgHead head = firstHead("#+TITLE: Notes\n\n* TODO Still a state\n");

        Assert.assertEquals("TODO", head.getState());
        Assert.assertEquals("Still a state", head.getTitle());
    }

    @Test
    public void testSeqTodoIsAWorkflow() throws IOException {
        OrgHead head = firstHead("#+SEQ_TODO: STARTED | FINISHED\n\n* STARTED Going\n");

        Assert.assertEquals("STARTED", head.getState());
    }

    @Test
    public void testTypTodoIsAWorkflow() throws IOException {
        OrgHead head = firstHead("#+TYP_TODO: ANNA | BORIS\n\n* BORIS Assigned\n");

        Assert.assertEquals("BORIS", head.getState());
    }

    @Test
    public void testSeveralWorkflowLinesAllApply() throws IOException {
        String file = "#+TODO: NEXT | CNCL\n#+TODO: BUG | FIXED\n\n* FIXED Done with it\n";

        Assert.assertEquals("FIXED", firstHead(file).getState());
    }

    @Test
    public void testWorkflowAppliesToEveryHeading() throws IOException {
        String file = "#+TODO: NEXT | CNCL\n\n* NEXT One\n* CNCL Two\n";
        OrgParsedFile parsed = parserBuilder.setInput(file).build().parse();

        Assert.assertEquals("NEXT", parsed.getHeadsInList().get(0).getHead().getState());
        Assert.assertEquals("CNCL", parsed.getHeadsInList().get(1).getHead().getState());
    }
}
