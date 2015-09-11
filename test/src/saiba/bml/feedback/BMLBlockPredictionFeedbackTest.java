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
import hmi.xml.XMLScanException;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;
import org.xml.sax.SAXException;

import saiba.utils.TestUtil;

/**
 * Unit testcases for the XMLBMLBlockPredictionFeedbackElementTest element
 * @author hvanwelbergen
 * 
 */
public class BMLBlockPredictionFeedbackTest
{
    private static final double PRECISION = 0.0001;
    private BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback();
    private FeedbackExtensionTests<BMLBlockPredictionFeedback> fe = new FeedbackExtensionTests<BMLBlockPredictionFeedback>();

    @Test
    public void testReadXML()
    {
        String str = "<bml " + TestUtil.getDefNS() + " id=\"bml1\" globalStart=\"1\" globalEnd=\"2\"/>";
        fb.readXML(str);
        assertEquals("bml1", fb.getId());
        assertEquals(1, fb.getGlobalStart(), PRECISION);
        assertEquals(2, fb.getGlobalEnd(), PRECISION);
    }

    @Test(expected = XMLScanException.class)
    public void testEndOrStartRequired()
    {
        String str = "<bml id=\"bml1\"/>";
        fb.readXML(str);
    }

    @Test
    public void testWriteXML() throws SAXException, IOException
    {
        BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback("bml1", 1, 2);
        StringBuilder buf = new StringBuilder();
        fb.appendXML(buf);
        XMLAssert.assertXMLEqual("<bml " + TestUtil.getDefNS() + " id=\"bml1\" globalStart=\"1.0\" globalEnd=\"2.0\"/>", buf.toString());
    }

    @Test
    public void testCustomFloatAttribute() throws IOException
    {
        fe.testCustomFloatAttribute(fb, "bml1", "globalStart=\"1.0\"");
    }

    @Test
    public void testNoFloatAttribute() throws IOException
    {
        fe.testNoFloatAttribute(fb, "bml1", "globalStart=\"1.0\"");
    }

    @Test
    public void testCustomStringAttribute() throws IOException
    {
        fe.testCustomStringAttribute(fb, "bml1", "globalStart=\"1.0\"");
    }

    @Test
    public void testNoCustomStringAttribute() throws IOException
    {
        fe.testNoCustomStringAttribute(fb, "bml1", "globalStart=\"1.0\"");
    }

    @Test
    public void testWriteCustomFloatAttribute() throws IOException
    {
        fe.testWriteCustomFloatAttribute(new BMLBlockPredictionFeedback(), new BMLBlockPredictionFeedback(), "bml1", "globalStart=\"1.0\"");
    }

    @Test
    public void testWriteCustomStringAttribute() throws IOException
    {
        fe.testWriteCustomStringAttribute(new BMLBlockPredictionFeedback(), new BMLBlockPredictionFeedback(), "bml1", "globalStart=\"1.0\"");
    }

    @Test
    public void testWriteCustomStringAttributeNoPrefix() throws IOException
    {
        fe.testWriteCustomStringAttributeNoPrefix(new BMLBlockPredictionFeedback(), new BMLBlockPredictionFeedback(), "bml1",
                "globalStart=\"1.0\"");
    }

    @Test
    public void testWriteCustomStringAttributeNoPrefix2() throws IOException
    {
        fe.testWriteCustomStringAttributeNoPrefix2(new BMLBlockPredictionFeedback(), new BMLBlockPredictionFeedback(), "bml1",
                "globalStart=\"1.0\"");
    }
}
