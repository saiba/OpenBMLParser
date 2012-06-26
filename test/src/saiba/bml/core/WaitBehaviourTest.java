package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit test cases for the WaitBehavior
 * @author Herwin
 */
public class WaitBehaviourTest extends AbstractBehaviourTest
{
    private static final double PARAMETER_PRECISION = 0.0001;
    
    @Override
    protected WaitBehaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<wait "+TestUtil.getDefNS()+" id=\"wait1\" " +
            extraAttributeString+" max-wait=\"2\"/>";
        return new WaitBehaviour("bml1", new XMLTokenizer(str));
    }
    
    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new WaitBehaviour("bml1", new XMLTokenizer(bmlString));
    }
    
    @Test
    public void testReadXML() throws IOException
    {
        WaitBehaviour beh = createBehaviour("bml1", "start=\"1\"");
        assertEquals("wait1", beh.id);
        assertEquals("bml1", beh.bmlId);
        assertEquals(2,beh.getFloatParameterValue("max-wait"),PARAMETER_PRECISION);
        assertEquals(1,beh.getSyncPoints().get(0).getRef().offset,PARAMETER_PRECISION);
    }
    
    @Test
    public void testWriteXML() throws IOException
    {
        WaitBehaviour behIn = createBehaviour("bml1", "start=\"1\"");
        StringBuilder buf = new StringBuilder();
        
        behIn.appendXML(buf);
        WaitBehaviour behOut = new WaitBehaviour("bml1", new XMLTokenizer(buf.toString()));
        assertEquals("wait1", behOut.id);
        assertEquals("bml1", behOut.bmlId);
        assertEquals(2,behOut.getFloatParameterValue("max-wait"),PARAMETER_PRECISION);
        assertEquals(1,behOut.getSyncPoints().get(0).getRef().offset,PARAMETER_PRECISION);
    }

    

    
}
