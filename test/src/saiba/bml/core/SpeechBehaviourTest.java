package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit test cases for the SpeechBehaviour
 * @author Herwin
 */
public class SpeechBehaviourTest extends AbstractBehaviourTest
{
    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<speech "+TestUtil.getDefNS()+"id=\"speech1\" " +
            extraAttributeString+"><text>Hello <sync id=\"s1\"/> world</text></speech>";
        return new SpeechBehaviour("bml1", new XMLTokenizer(str));
    }
    
    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new SpeechBehaviour("bml1", new XMLTokenizer(bmlString));
    }   
    
    @Test
    public void testReadXML() throws IOException
    {
        String str = "<speech"+TestUtil.getDefNS()+" id=\"speech1\"><text>Hello <sync id=\"s1\"/> world</text></speech>";
        SpeechBehaviour beh = new SpeechBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("speech1", beh.id);
        assertEquals("bml1", beh.bmlId);
        assertEquals("s1",beh.getSyncPoints().get(2).getName());
        //assertEquals("Hello <sync id=\"s1\"/> world",beh.content);
    }
    
    @Test
    public void testWriteXML() throws IOException
    {
        String str = "<speech"+TestUtil.getDefNS()+"id=\"speech1\"><text>Hello <sync id=\"s1\"/> world</text></speech>";
        SpeechBehaviour behIn = new SpeechBehaviour("bml1", new XMLTokenizer(str));
             
        StringBuilder buf = new StringBuilder();        
        behIn.appendXML(buf);        
        SpeechBehaviour behOut = new SpeechBehaviour("bml1", new XMLTokenizer(buf.toString()));
        assertEquals("speech1", behOut.id);
        assertEquals("bml1", behOut.bmlId);
        assertEquals("s1",behOut.getSyncPoints().get(2).getName());
        //assertEquals("Hello <sync id=\"s1\"/> world",behOut.content);
    }

    
}
