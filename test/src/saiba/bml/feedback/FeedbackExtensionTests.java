package saiba.bml.feedback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import saiba.bml.BMLInfo;
import saiba.utils.TestUtil;

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
        String str = "<"+fb.getXMLTag()+" "+ TestUtil.getDefNS()
                + " id=\""+id+"\" "+ attributes +" xmlns:custom=\"customfaceattributes\" custom:attr2=\"10\" />";
        fb.readXML(str);
        assertEquals(10, fb.getCustomFloatParameterValue("customfaceattributes:attr2"), PRECISION);
        assertTrue(fb.specifiesCustomParameter("customfaceattributes:attr2"));
    }
}
