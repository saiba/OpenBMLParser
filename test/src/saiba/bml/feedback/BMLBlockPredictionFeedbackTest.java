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
    private static final double TIME_PRECISION = 0.0001;
    @Test
    public void testReadXML()
    {
        BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback();
        String str = "<bml "+TestUtil.getDefNS()+" id=\"bml1\" globalStart=\"1\" globalEnd=\"2\"/>";
        fb.readXML(str);        
        assertEquals("bml1",fb.getId());
        assertEquals(1,fb.getGlobalStart(),TIME_PRECISION);
        assertEquals(2,fb.getGlobalEnd(),TIME_PRECISION);
    }
    
    @Test(expected=XMLScanException.class)
    public void testEndOrStartRequired()
    {
        BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback();
        String str = "<bml id=\"bml1\"/>";
        fb.readXML(str);        
    }
    
    @Test
    public void testWriteXML() throws SAXException, IOException
    {
        BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback("bml1",1,2);
        StringBuilder buf = new StringBuilder();
        fb.appendXML(buf);        
        XMLAssert.assertXMLEqual("<bml "+TestUtil.getDefNS()+" id=\"bml1\" globalStart=\"1.0\" globalEnd=\"2.0\"/>", buf.toString());        
    }
}
