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

import static org.junit.Assert.*;

import org.junit.Test;

import saiba.bml.feedback.BMLWarningFeedback;
import saiba.utils.TestUtil;

/**
 * unit tests for XMLBMLWarning
 * @author Herwin
 *
 */
public class BMLWarningFeedbackTest
{
    @Test
    public void testReadXML()
    {
        String str = "<warningFeedback "+TestUtil.getDefNS()+"id=\"bml1\" characterId=\"doctor\" type=\"PARSING_FAILURE\">content</warningFeedback>";
        BMLWarningFeedback warning = new BMLWarningFeedback();
        warning.readXML(str);
        assertEquals("bml1",warning.getId());
        assertEquals("doctor",warning.getCharacterId());
        assertEquals(BMLWarningFeedback.PARSING_FAILURE,warning.getType());
        assertEquals("content",warning.getDescription());
    }
    
    @Test
    public void testWriteXML()
    {
        String str = "<warningFeedback "+TestUtil.getDefNS()+"id=\"bml1\" characterId=\"doctor\" type=\"PARSING_FAILURE\">content</warningFeedback>";
        BMLWarningFeedback warningIn = new BMLWarningFeedback();
        warningIn.readXML(str);
        StringBuilder buf = new StringBuilder();
        warningIn.appendXML(buf);     
        
        BMLWarningFeedback warningOut = new BMLWarningFeedback();
        warningOut.readXML(buf.toString());
        assertEquals("bml1",warningOut.getId());
        assertEquals("doctor",warningOut.getCharacterId());
        assertEquals(BMLWarningFeedback.PARSING_FAILURE,warningOut.getType());
        assertEquals("content",warningOut.getDescription());
    }
}
