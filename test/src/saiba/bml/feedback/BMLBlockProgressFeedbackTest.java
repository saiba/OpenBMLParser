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
 * Unit tests for XMLBMLBlockProgressTest 
 * @author Herwin
 *
 */
public class BMLBlockProgressFeedbackTest
{
    private static final double PRECISION = 0.00001;
    private FeedbackExtensionTests<BMLBlockProgressFeedback> fe = new FeedbackExtensionTests<BMLBlockProgressFeedback>();
    
    BMLBlockProgressFeedback fb = new BMLBlockProgressFeedback();        
    
    @Test
    public void testReadXML()
    {
        String str = "<blockProgress "+TestUtil.getDefNS()+"id=\"bml1:start\" globalTime=\"10\" characterId=\"doctor\"/>";
        fb.readXML(str);
        assertEquals("bml1",fb.getBmlId());
        assertEquals("start",fb.getSyncId());
        assertEquals(10, fb.getGlobalTime(), PRECISION);
        assertEquals("doctor", fb.getCharacterId());
    }
    
    @Test
    public void testWriteXML()
    {
        String str = "<blockProgress "+TestUtil.getDefNS()+"id=\"bml1:start\" globalTime=\"10\" characterId=\"doctor\"/>";
        
        BMLBlockProgressFeedback progressIn = new BMLBlockProgressFeedback();        
        progressIn.readXML(str);
        StringBuilder buf = new StringBuilder();
        progressIn.appendXML(buf);        
        
        BMLBlockProgressFeedback progressOut = new BMLBlockProgressFeedback();  
        progressOut.readXML(buf.toString());
        assertEquals("bml1",progressOut.getBmlId());
        assertEquals("start",progressOut.getSyncId());
        assertEquals(10, progressOut.getGlobalTime(), PRECISION);
        assertEquals("doctor", progressOut.getCharacterId());
    }
    
    @Test
    public void testCustomFloatAttribute() throws IOException
    {
        fe.testCustomFloatAttribute(fb,"bml1:start", "globalTime=\"10\"");
    }
}
