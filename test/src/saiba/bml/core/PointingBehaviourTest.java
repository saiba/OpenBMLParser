package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit test cases for the pointing behaviour
 * @author Herwin
 *
 */
public class PointingBehaviourTest extends AbstractBehaviourTest
{
    private static final double PARAMETER_PRECISION = 0.0001;
    
    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<pointing "+TestUtil.getDefNS()+"id=\"point1\" target=\"bluebox\" " +
            extraAttributeString+" />";
        return new PointingBehaviour(bmlId, new XMLTokenizer(str));
    }
    
    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new PointingBehaviour(bmlId, new XMLTokenizer(bmlString));
    }
    
    @Test
    public void testReadXML() throws IOException
    {
        String str = "<pointing "+TestUtil.getDefNS()+"id=\"point1\" target=\"bluebox\" mode=\"RIGHT_HAND\" start=\"1\" />";
        PointingBehaviour beh = new PointingBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("point1", beh.id);
        assertEquals("bml1", beh.bmlId);
        assertEquals("bluebox",beh.getStringParameterValue("target"));
        assertEquals("RIGHT_HAND",beh.getStringParameterValue("mode"));
        assertEquals(1,beh.getSyncPoints().get(0).getRef().offset,PARAMETER_PRECISION);
    }
    
    @Test
    public void testWriteXML() throws IOException
    {
        String str = "<pointing "+TestUtil.getDefNS()+"id=\"point1\" target=\"bluebox\" mode=\"RIGHT_HAND\" start=\"1\" />";
        PointingBehaviour behIn = new PointingBehaviour("bml1", new XMLTokenizer(str));
        StringBuilder buf = new StringBuilder();
        
        behIn.appendXML(buf);        
        PointingBehaviour behOut = new PointingBehaviour("bml1", new XMLTokenizer(buf.toString()));
        assertEquals("point1", behOut.id);
        assertEquals("bml1", behOut.bmlId);
        assertEquals("bluebox",behOut.getStringParameterValue("target"));
        assertEquals("RIGHT_HAND",behOut.getStringParameterValue("mode"));
        assertEquals(1,behOut.getSyncPoints().get(0).getRef().offset,PARAMETER_PRECISION);
    }

    

    
}
