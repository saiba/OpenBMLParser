package saiba.bml.feedback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hmi.xml.XMLFormatting;
import hmi.xml.XMLNameSpace;

import java.io.IOException;

import saiba.bml.BMLInfo;
import saiba.utils.TestUtil;

import com.google.common.collect.ImmutableList;

public class FeedbackExtensionTests<E extends AbstractBMLFeedback>
{
    private static final double PRECISION = 0.0001;

    public void testCustomFloatAttribute(E fb, String id) throws IOException
    {
        testCustomFloatAttribute(fb, id, "");
    }

    public void testCustomFloatAttribute(E fb, String id, String attributes) throws IOException
    {
        BMLInfo.addCustomFeedbackFloatAttribute(fb.getClass(), "customfaceattributes", "attr2");
        String str = "<" + fb.getXMLTag() + " " + TestUtil.getDefNS() + " id=\"" + id + "\" " + attributes
                + " xmlns:custom=\"customfaceattributes\" custom:attr2=\"10\" />";
        fb.readXML(str);
        assertEquals(10, fb.getCustomFloatParameterValue("customfaceattributes:attr2"), PRECISION);
        assertTrue(fb.specifiesCustomParameter("customfaceattributes:attr2"));
    }

    public void testNoFloatAttribute(E fb, String id) throws IOException
    {
        testNoFloatAttribute(fb, id, "");
    }

    public void testNoFloatAttribute(E fb, String id, String attributes) throws IOException
    {
        BMLInfo.addCustomFeedbackFloatAttribute(fb.getClass(), "customfaceattributes", "attr2");
        String str = "<" + fb.getXMLTag() + " " + TestUtil.getDefNS() + " id=\"" + id + "\" " + attributes
                + " xmlns:custom=\"customfaceattributes\" globalEnd=\"2\"/>";
        fb.readXML(str);
        assertFalse(fb.specifiesCustomParameter("customfaceattributes:attr2"));
    }

    public void testCustomStringAttribute(E fb, String id) throws IOException
    {
        testCustomStringAttribute(fb, id, "");
    }

    public void testCustomStringAttribute(E fb, String id, String attributes) throws IOException
    {
        BMLInfo.addCustomFeedbackStringAttribute(fb.getClass(), "customfaceattributes", "attr1");
        String str = "<" + fb.getXMLTag() + " " + TestUtil.getDefNS() + " id=\"" + id + "\" " + attributes
                + " xmlns:custom=\"customfaceattributes\" custom:attr1=\"blah\" globalEnd=\"2\"/>";
        fb.readXML(str);

        assertEquals("blah", fb.getCustomStringParameterValue("customfaceattributes:attr1"));
        assertTrue(fb.specifiesCustomParameter("customfaceattributes:attr1"));
        assertTrue(fb.satisfiesCustomConstraint("customfaceattributes:attr1", "blah"));
    }

    public void testNoCustomStringAttribute(E fb, String id) throws IOException
    {
        testNoCustomStringAttribute(fb, id, "");
    }

    public void testNoCustomStringAttribute(E fb, String id, String attributes) throws IOException
    {
        BMLInfo.addCustomFeedbackStringAttribute(fb.getClass(), "customfaceattributes", "attr1");
        String str = "<" + fb.getXMLTag() + " " + TestUtil.getDefNS() + " id=\"" + id + "\" " + attributes
                + " xmlns:custom=\"customfaceattributes\" globalEnd=\"2\"/>";
        fb.readXML(str);
        assertFalse(fb.specifiesCustomParameter("customfaceattributes:attr2"));
    }

    public void testWriteCustomFloatAttribute(E fbIn, E fbOut, String id) throws IOException
    {
        testWriteCustomFloatAttribute(fbIn, fbOut, id, "");
    }

    public void testWriteCustomFloatAttribute(E fbIn, E fbOut, String id, String attributes) throws IOException
    {
        BMLInfo.addCustomFeedbackFloatAttribute(fbIn.getClass(), "customfaceattributes", "attr2");
        String str = "<" + fbIn.getXMLTag() + " " + TestUtil.getDefNS() + " id=\"" + id + "\" " + attributes
                + " xmlns:custom=\"customfaceattributes\" custom:attr2=\"10\" globalEnd=\"2\"/>";
        fbIn.readXML(str);

        StringBuilder buf = new StringBuilder();
        fbIn.appendXML(buf, new XMLFormatting(), ImmutableList.of(new XMLNameSpace("custom", "customfaceattributes")));

        fbOut.readXML(buf.toString());
        assertEquals(10, fbOut.getCustomFloatParameterValue("customfaceattributes:attr2"), PRECISION);
        assertTrue(fbOut.specifiesCustomParameter("customfaceattributes:attr2"));
    }

    public void testWriteCustomStringAttribute(E fbIn, E fbOut, String id) throws IOException
    {
        testWriteCustomStringAttribute(fbIn, fbOut, id, "");
    }
    
    public void testWriteCustomStringAttribute(E fbIn, E fbOut, String id, String attributes) throws IOException
    {
        BMLInfo.addCustomFeedbackStringAttribute(fbIn.getClass(), "customfaceattributes", "attr1");
        String str = "<" + fbIn.getXMLTag() + " " + TestUtil.getDefNS() + " id=\"" + id + "\" xmlns:custom=\"customfaceattributes\" "
                + attributes + " custom:attr1=\"blah\"/>";
        fbIn.readXML(str);

        StringBuilder buf = new StringBuilder();
        fbIn.appendXML(buf, new XMLFormatting(), ImmutableList.of(new XMLNameSpace("custom", "customfaceattributes")));

        fbOut.readXML(buf.toString());
        assertEquals("blah", fbOut.getCustomStringParameterValue("customfaceattributes:attr1"));
        assertTrue(fbOut.specifiesCustomParameter("customfaceattributes:attr1"));
    }

    public void testWriteCustomStringAttributeNoPrefix(E fbIn, E fbOut, String id) throws IOException
    {
        testWriteCustomStringAttributeNoPrefix(fbIn, fbOut, id, "");
    }
    
    public void testWriteCustomStringAttributeNoPrefix(E fbIn, E fbOut, String id, String attributes) throws IOException
    {
        BMLInfo.addCustomFeedbackStringAttribute(fbIn.getClass(), "customfaceattributes", "attr1");
        String str = "<" + fbIn.getXMLTag() + " " + TestUtil.getDefNS() + " id=\"" + id + "\" " + attributes
                + " xmlns:custom=\"customfaceattributes\" custom:attr1=\"blah\"/>";
        fbIn.readXML(str);

        StringBuilder buf = new StringBuilder();
        fbIn.appendXML(buf);

        fbOut.readXML(buf.toString());

        assertEquals("blah", fbOut.getCustomStringParameterValue("customfaceattributes:attr1"));
        assertTrue(fbOut.specifiesCustomParameter("customfaceattributes:attr1"));
    }

    public void testWriteCustomStringAttributeNoPrefix2(E fbIn, E fbOut, String id) throws IOException
    {
        testWriteCustomStringAttributeNoPrefix2(fbIn, fbOut, id, "");
    }
    
    public void testWriteCustomStringAttributeNoPrefix2(E fbIn, E fbOut, String id, String attributes) throws IOException
    {
        BMLInfo.addCustomFeedbackStringAttribute(fbIn.getClass(), "http://customfaceattributes.com", "attr1");
        String str = "<" + fbIn.getXMLTag() + " " + TestUtil.getDefNS() + " id=\"" + id + "\" " + attributes
                + " xmlns:custom=\"http://customfaceattributes.com\" custom:attr1=\"blah\"/>";
        fbIn.readXML(str);

        StringBuilder buf = new StringBuilder();
        fbIn.appendXML(buf);

        fbOut.readXML(buf.toString());

        assertEquals("blah", fbOut.getCustomStringParameterValue("http://customfaceattributes.com:attr1"));
        assertTrue(fbOut.specifiesCustomParameter("http://customfaceattributes.com:attr1"));
    }
}
