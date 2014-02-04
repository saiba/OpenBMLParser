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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hmi.xml.XMLFormatting;
import hmi.xml.XMLNameSpace;
import hmi.xml.XMLScanException;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;
import org.xml.sax.SAXException;

import saiba.bml.BMLInfo;
import saiba.utils.TestUtil;

import com.google.common.collect.ImmutableList;

/**
 * Unit testcases for the XMLBMLBlockPredictionFeedbackElementTest element
 * @author hvanwelbergen
 * 
 */
public class BMLBlockPredictionFeedbackTest
{
    private static final double PRECISION = 0.0001;

    @Test
    public void testReadXML()
    {
        BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback();
        String str = "<bml " + TestUtil.getDefNS() + " id=\"bml1\" globalStart=\"1\" globalEnd=\"2\"/>";
        fb.readXML(str);
        assertEquals("bml1", fb.getId());
        assertEquals(1, fb.getGlobalStart(), PRECISION);
        assertEquals(2, fb.getGlobalEnd(), PRECISION);
    }

    @Test(expected = XMLScanException.class)
    public void testEndOrStartRequired()
    {
        BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback();
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
        BMLInfo.addCustomFeedbackFloatAttribute(BMLBlockPredictionFeedback.class, "customfaceattributes", "attr2");
        BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback();
        String str = "<bml " + TestUtil.getDefNS()
                + " id=\"bml1\" xmlns:custom=\"customfaceattributes\" custom:attr2=\"10\" globalEnd=\"2\"/>";
        fb.readXML(str);
        assertEquals(10, fb.getCustomFloatParameterValue("customfaceattributes:attr2"), PRECISION);
        assertTrue(fb.specifiesCustomParameter("customfaceattributes:attr2"));
    }

    @Test
    public void testNoFloatAttribute() throws IOException
    {
        BMLInfo.addCustomFeedbackFloatAttribute(BMLBlockPredictionFeedback.class, "customfaceattributes", "attr2");
        BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback();
        String str = "<bml " + TestUtil.getDefNS() + " id=\"bml1\" xmlns:custom=\"customfaceattributes\" globalEnd=\"2\"/>";
        fb.readXML(str);
        assertFalse(fb.specifiesCustomParameter("customfaceattributes:attr2"));
    }

    @Test
    public void testCustomStringAttribute() throws IOException
    {
        BMLInfo.addCustomFeedbackStringAttribute(BMLBlockPredictionFeedback.class, "customfaceattributes", "attr1");
        BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback();
        String str = "<bml " + TestUtil.getDefNS()
                + " id=\"bml1\" xmlns:custom=\"customfaceattributes\" custom:attr1=\"blah\" globalEnd=\"2\"/>";
        fb.readXML(str);

        assertEquals("blah", fb.getCustomStringParameterValue("customfaceattributes:attr1"));
        assertTrue(fb.specifiesCustomParameter("customfaceattributes:attr1"));
        assertTrue(fb.satisfiesCustomConstraint("customfaceattributes:attr1", "blah"));
    }

    @Test
    public void testNoCustomStringAttribute() throws IOException
    {
        BMLInfo.addCustomFeedbackStringAttribute(BMLBlockPredictionFeedback.class, "customfaceattributes", "attr1");
        BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback();
        String str = "<bml " + TestUtil.getDefNS() + " id=\"bml1\" xmlns:custom=\"customfaceattributes\" globalEnd=\"2\"/>";
        fb.readXML(str);
        assertFalse(fb.specifiesCustomParameter("customfaceattributes:attr2"));
    }

    @Test
    public void testWriteCustomFloatAttribute() throws IOException
    {
        BMLInfo.addCustomFeedbackFloatAttribute(BMLBlockPredictionFeedback.class, "customfaceattributes", "attr2");
        BMLBlockPredictionFeedback fbIn = new BMLBlockPredictionFeedback();
        String str = "<bml " + TestUtil.getDefNS()
                + " id=\"bml1\" xmlns:custom=\"customfaceattributes\" custom:attr2=\"10\" globalEnd=\"2\"/>";
        fbIn.readXML(str);

        StringBuilder buf = new StringBuilder();
        fbIn.appendXML(buf, new XMLFormatting(), ImmutableList.of(new XMLNameSpace("custom", "customfaceattributes")));

        BMLBlockPredictionFeedback fbOut = new BMLBlockPredictionFeedback();
        fbOut.readXML(buf.toString());

        assertEquals(10, fbOut.getCustomFloatParameterValue("customfaceattributes:attr2"), PRECISION);
        assertTrue(fbOut.specifiesCustomParameter("customfaceattributes:attr2"));
    }
    
    
    @Test
    public void testWriteCustomStringAttribute() throws IOException
    {
        BMLInfo.addCustomFeedbackStringAttribute(BMLBlockPredictionFeedback.class, "customfaceattributes", "attr1");
        BMLBlockPredictionFeedback fbIn = new BMLBlockPredictionFeedback();
        String str = "<bml " + TestUtil.getDefNS()
                + " id=\"bml1\" xmlns:custom=\"customfaceattributes\" custom:attr1=\"blah\" globalEnd=\"2\"/>";
        fbIn.readXML(str);

        StringBuilder buf = new StringBuilder();
        fbIn.appendXML(buf, new XMLFormatting(), ImmutableList.of(new XMLNameSpace("custom", "customfaceattributes")));

        BMLBlockPredictionFeedback fbOut = new BMLBlockPredictionFeedback();
        fbOut.readXML(buf.toString());

        assertEquals("blah", fbOut.getCustomStringParameterValue("customfaceattributes:attr1"));
        assertTrue(fbOut.specifiesCustomParameter("customfaceattributes:attr1"));
    }

    @Test
    public void testWriteCustomStringAttributeNoPrefix() throws IOException
    {
        BMLInfo.addCustomFeedbackStringAttribute(BMLBlockPredictionFeedback.class, "customfaceattributes", "attr1");
        BMLBlockPredictionFeedback fbIn = new BMLBlockPredictionFeedback();
        String str = "<bml " + TestUtil.getDefNS()
                + " id=\"bml1\" xmlns:custom=\"customfaceattributes\" custom:attr1=\"blah\" globalEnd=\"2\"/>";
        fbIn.readXML(str);

        StringBuilder buf = new StringBuilder();
        fbIn.appendXML(buf, new XMLFormatting(), ImmutableList.of(new XMLNameSpace("custom", "customfaceattributes")));

        BMLBlockPredictionFeedback fbOut = new BMLBlockPredictionFeedback();
        fbOut.readXML(buf.toString());

        assertEquals("blah", fbOut.getCustomStringParameterValue("customfaceattributes:attr1"));
        assertTrue(fbOut.specifiesCustomParameter("customfaceattributes:attr1"));
    }
    
    // @Test
    // public void testWriteCustomStringAttributeNoPrefix()throws IOException
    // {
    // BMLInfo.addCustomStringAttribute(createBehaviour("bml1","").getClass(), "customfaceattributes","attr1");
    // Behaviour behIn = createBehaviour("bml1", "xmlns:custom=\"customfaceattributes\" custom:attr1=\"blah\"");
    //
    // StringBuilder buf = new StringBuilder();
    // behIn.appendXML(buf);
    //
    // Behaviour behOut = parseBehaviour("bml1",buf.toString());
    // assertEquals("blah", behOut.getStringParameterValue("customfaceattributes:attr1"));
    // assertTrue(behOut.specifiesParameter("customfaceattributes:attr1"));
    // }
    
    // @Test
    // public void testWriteCustomStringAttributeNoPrefix2()throws IOException
    // {
    // BMLInfo.addCustomStringAttribute(createBehaviour("bml1","").getClass(), "http://customfaceattributes.com","attr1");
    // Behaviour behIn = createBehaviour("bml1", "xmlns:custom=\"http://customfaceattributes.com\" custom:attr1=\"blah\"");
    //
    // StringBuilder buf = new StringBuilder();
    // behIn.appendXML(buf);
    // System.out.println(buf);
    //
    // Behaviour behOut = parseBehaviour("bml1",buf.toString());
    // assertEquals("blah", behOut.getStringParameterValue("http://customfaceattributes.com:attr1"));
    // assertTrue(behOut.specifiesParameter("http://customfaceattributes.com:attr1"));
    // }

}
