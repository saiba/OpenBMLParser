package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit test cases for the locomotion behavior
 * @author welberge
 */
public class LocomotionBehaviourTest extends AbstractBehaviourTest
{
    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<locomotion "+TestUtil.getDefNS()+"id=\"loc1\" target=\"bluebox\" " +
            extraAttributeString+"/>";
        return new LocomotionBehaviour("bml1", new XMLTokenizer(str));
    }
    
    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new LocomotionBehaviour("bml1", new XMLTokenizer(bmlString));
    }  
    
    @Test
    public void testReadLocomotion() throws IOException
    {
        String str = "<locomotion "+TestUtil.getDefNS()+"id=\"loc1\" target=\"bluebox\" start=\"1\" manner=\"RUN\"/>";
        LocomotionBehaviour beh = new LocomotionBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("loc1", beh.id);
        assertEquals("bml1", beh.bmlId);
        assertEquals("bluebox",beh.getStringParameterValue("target"));
        assertEquals("RUN", beh.getStringParameterValue("manner"));
    }
    
    @Test
    public void writeReadXML() throws IOException
    {
        String str = "<locomotion "+TestUtil.getDefNS()+"id=\"loc1\" target=\"bluebox\" start=\"1\" manner=\"RUN\"/>";
        LocomotionBehaviour behIn = new LocomotionBehaviour("bml1", new XMLTokenizer(str));
        StringBuilder buf = new StringBuilder();
        
        behIn.appendXML(buf); 
        LocomotionBehaviour behOut = new LocomotionBehaviour("bml1", new XMLTokenizer(buf.toString()));
        assertEquals("loc1", behOut.id);
        assertEquals("bml1", behOut.bmlId);
        assertEquals("bluebox",behOut.getStringParameterValue("target"));
        assertEquals("RUN", behOut.getStringParameterValue("manner"));
    }

      
}
