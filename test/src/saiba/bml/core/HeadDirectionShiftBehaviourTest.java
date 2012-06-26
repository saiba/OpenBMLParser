package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit tests for the HeadDirectionShiftBehaviour
 * @author welberge
 *
 */
public class HeadDirectionShiftBehaviourTest extends AbstractBehaviourTest
{
    private static final double PARAMETER_PRECISION = 0.0001;
    
    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<headDirectionShift "+TestUtil.getDefNS()+"id=\"head1\" target=\"bluebox\" " +
            extraAttributeString+"/>";
        return new HeadDirectionShiftBehaviour(bmlId, new XMLTokenizer(str));
    }
    
    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new HeadDirectionShiftBehaviour(bmlId, new XMLTokenizer(bmlString));
    }
    
    @Test
    public void testReadXML() throws IOException
    {
        String str = "<headDirectionShift "+TestUtil.getDefNS()+"id=\"head1\" start=\"1\" target=\"bluebox\"/>";
        HeadDirectionShiftBehaviour beh = new HeadDirectionShiftBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("head1", beh.id);
        assertEquals("bml1", beh.bmlId);
        assertEquals("bluebox",beh.getStringParameterValue("target"));
        assertEquals(1,beh.getSyncPoints().get(0).getRef().offset,PARAMETER_PRECISION);
    }
    
    @Test
    public void testWriteXML() throws IOException
    {
        String str = "<headDirectionShift "+TestUtil.getDefNS()+"id=\"head1\" start=\"1\" target=\"bluebox\"/>";
        HeadDirectionShiftBehaviour behIn = new HeadDirectionShiftBehaviour("bml1", new XMLTokenizer(str));
        StringBuilder buf = new StringBuilder();
        behIn.appendXML(buf);        
        
        HeadDirectionShiftBehaviour behOut = new HeadDirectionShiftBehaviour("bml1", new XMLTokenizer(buf.toString()));
        assertEquals("head1", behOut.id);
        assertEquals("bml1", behOut.bmlId);
        assertEquals("bluebox",behOut.getStringParameterValue("target"));
        assertEquals(1,behOut.getSyncPoints().get(0).getRef().offset,PARAMETER_PRECISION);
    }

    

    
}
