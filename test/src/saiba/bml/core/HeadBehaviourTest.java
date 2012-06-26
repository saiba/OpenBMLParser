package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit tests for the HeadBehaviour
 * @author welberge
 *
 */
public class HeadBehaviourTest extends AbstractBehaviourTest
{
    private static final double PARAMETER_PRECISION = 0.0001;
    
    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<head "+TestUtil.getDefNS()+"id=\"head1\" lexeme=\"NOD\" " +
            extraAttributeString+"/>";
        return new HeadBehaviour("bml1", new XMLTokenizer(str));
    }
    
    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new HeadBehaviour("bml1", new XMLTokenizer(bmlString));
    }
    
    @Test
    public void testReadXML() throws IOException
    {
        String str = "<head "+TestUtil.getDefNS()+"id=\"head1\" repetition=\"2\" amount=\"0.3\" start=\"1\" lexeme=\"NOD\"/>";
        HeadBehaviour beh = new HeadBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("head1", beh.id);
        assertEquals("bml1", beh.bmlId);
        assertEquals("NOD",beh.getStringParameterValue("lexeme"));
        assertEquals(0.3, beh.getFloatParameterValue("amount"), PARAMETER_PRECISION);
        assertEquals(2, beh.getFloatParameterValue("repetition"), PARAMETER_PRECISION);
        assertEquals(1,beh.getSyncPoints().get(0).getRef().offset,PARAMETER_PRECISION);
    }
    
    @Test
    public void testWriteXML() throws IOException
    {
        String str = "<head "+TestUtil.getDefNS()+"id=\"head1\" repetition=\"2\" amount=\"0.3\" start=\"1\" lexeme=\"NOD\"/>";
        HeadBehaviour behIn = new HeadBehaviour("bml1", new XMLTokenizer(str));
        
        StringBuilder buf = new StringBuilder();        
        behIn.appendXML(buf);        
        HeadBehaviour behOut = new HeadBehaviour("bml1", new XMLTokenizer(buf.toString()));
        assertEquals("head1", behOut.id);
        assertEquals("bml1", behOut.bmlId);
        assertEquals("NOD",behOut.getStringParameterValue("lexeme"));
        assertEquals(0.3, behOut.getFloatParameterValue("amount"), PARAMETER_PRECISION);
        assertEquals(2, behOut.getFloatParameterValue("repetition"), PARAMETER_PRECISION);
        assertEquals(1,behOut.getSyncPoints().get(0).getRef().offset,PARAMETER_PRECISION);
    }

   

    
}
