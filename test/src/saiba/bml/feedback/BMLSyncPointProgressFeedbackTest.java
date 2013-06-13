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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * unit test cases for the XMLBMLSyncPointProgress
 * @author Herwin
 */
public class BMLSyncPointProgressFeedbackTest
{
    private static final double PRECISION = 0.00001;
    
    @Test
    public void testReadXML()
    {
        String str = "<syncPointProgress "+TestUtil.getDefNS()+" characterId=\"doctor\" id=\"bml1:gesture1:stroke\" time=\"10\" globalTime=\"111\"/>";
        BMLSyncPointProgressFeedback progress = new BMLSyncPointProgressFeedback();
        progress.readXML(str);
        assertEquals("doctor", progress.getCharacterId());
        assertEquals(10, progress.getTime(),PRECISION);
        assertEquals(111, progress.getGlobalTime(),PRECISION);        
    }
    
    @Test
    public void testWriteXML()
    {
        String str = "<syncPointProgress "+TestUtil.getDefNS()+"characterId=\"doctor\" id=\"bml1:gesture1:stroke\" time=\"10\" globalTime=\"111\"/>";
        BMLSyncPointProgressFeedback progressIn = new BMLSyncPointProgressFeedback();
        progressIn.readXML(str);
        StringBuilder buf = new StringBuilder();
        progressIn.appendXML(buf);        
        
        BMLSyncPointProgressFeedback progressOut = new BMLSyncPointProgressFeedback();  
        progressOut.readXML(buf.toString());        
        assertEquals("doctor", progressOut.getCharacterId());
        assertEquals(10, progressOut.getTime(),PRECISION);
        assertEquals(111, progressOut.getGlobalTime(),PRECISION);        
    }
    
    @Test
    public void testWriteXMLNoCharacterId()
    {
        String str = "<syncPointProgress "+TestUtil.getDefNS()+"id=\"bml1:gesture1:stroke\" time=\"10\" globalTime=\"111\"/>";
        BMLSyncPointProgressFeedback progressIn = new BMLSyncPointProgressFeedback();
        progressIn.readXML(str);
        StringBuilder buf = new StringBuilder();
        progressIn.appendXML(buf);        
        
        BMLSyncPointProgressFeedback progressOut = new BMLSyncPointProgressFeedback();  
        progressOut.readXML(buf.toString());        
        assertNull(progressOut.getCharacterId());
        assertEquals(10, progressOut.getTime(),PRECISION);
        assertEquals(111, progressOut.getGlobalTime(),PRECISION);        
    }
}
