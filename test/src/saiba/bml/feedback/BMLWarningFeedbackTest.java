/*******************************************************************************
 * Copyright (c) 2013 University of Twente, Bielefeld University
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package saiba.bml.feedback;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * unit tests for XMLBMLWarning
 * @author Herwin
 * 
 */
public class BMLWarningFeedbackTest
{
    private FeedbackExtensionTests<BMLWarningFeedback> fe = new FeedbackExtensionTests<BMLWarningFeedback>();
    private BMLWarningFeedback fb = new BMLWarningFeedback();

    @Test
    public void testReadXML()
    {
        String str = "<warningFeedback " + TestUtil.getDefNS()
                + "id=\"bml1\" characterId=\"doctor\" type=\"PARSING_FAILURE\">content</warningFeedback>";
        fb.readXML(str);
        assertEquals("bml1", fb.getId());
        assertEquals("doctor", fb.getCharacterId());
        assertEquals(BMLWarningFeedback.PARSING_FAILURE, fb.getType());
        assertEquals("content", fb.getDescription());
    }

    @Test
    public void testWriteXML()
    {
        String str = "<warningFeedback " + TestUtil.getDefNS()
                + "id=\"bml1\" characterId=\"doctor\" type=\"PARSING_FAILURE\">content</warningFeedback>";
        BMLWarningFeedback warningIn = new BMLWarningFeedback();
        warningIn.readXML(str);
        StringBuilder buf = new StringBuilder();
        warningIn.appendXML(buf);

        BMLWarningFeedback warningOut = new BMLWarningFeedback();
        warningOut.readXML(buf.toString());
        assertEquals("bml1", warningOut.getId());
        assertEquals("doctor", warningOut.getCharacterId());
        assertEquals(BMLWarningFeedback.PARSING_FAILURE, warningOut.getType());
        assertEquals("content", warningOut.getDescription());
    }

    @Test
    public void testCustomFloatAttribute() throws IOException
    {
        fe.testCustomFloatAttribute(fb, "bml1", "type=\"PARSING_FAILURE\"");
    }

    @Test
    public void testNoFloatAttribute() throws IOException
    {
        fe.testNoFloatAttribute(fb, "bml1", "type=\"PARSING_FAILURE\"");
    }

    @Test
    public void testCustomStringAttribute() throws IOException
    {
        fe.testCustomStringAttribute(fb, "bml1", "type=\"PARSING_FAILURE\"");
    }

    @Test
    public void testNoCustomStringAttribute() throws IOException
    {
        fe.testNoCustomStringAttribute(fb, "bml1", "type=\"PARSING_FAILURE\"");
    }

    @Test
    public void testWriteCustomFloatAttribute() throws IOException
    {
        fe.testWriteCustomFloatAttribute(new BMLWarningFeedback(), new BMLWarningFeedback(), "bml1", "type=\"PARSING_FAILURE\"");
    }

    @Test
    public void testWriteCustomStringAttribute() throws IOException
    {
        fe.testWriteCustomStringAttribute(new BMLWarningFeedback(), new BMLWarningFeedback(), "bml1", "type=\"PARSING_FAILURE\"");
    }

    @Test
    public void testWriteCustomStringAttributeNoPrefix() throws IOException
    {
        fe.testWriteCustomStringAttributeNoPrefix(new BMLWarningFeedback(), new BMLWarningFeedback(), "bml1", "type=\"PARSING_FAILURE\"");
    }

    @Test
    public void testWriteCustomStringAttributeNoPrefix2() throws IOException
    {
        fe.testWriteCustomStringAttributeNoPrefix2(new BMLWarningFeedback(), new BMLWarningFeedback(), "bml1", "type=\"PARSING_FAILURE\"");
    }
}
