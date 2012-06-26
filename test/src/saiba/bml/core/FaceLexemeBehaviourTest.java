package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit test cases for the FaceLexemeBehaviour
 * @author Herwin
 */
public class FaceLexemeBehaviourTest extends AbstractBehaviourTest
{
    private static final double PARAMETER_PRECISION = 0.0001;
    
    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<faceLexeme "+TestUtil.getDefNS()+"amount=\"0.25\" lexeme=\"lex1\" id=\"beh1\""+ 
        extraAttributeString+"/>";        
        return new FaceLexemeBehaviour("bml1", new XMLTokenizer(str));
    }
    
    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new FaceLexemeBehaviour("bml1", new XMLTokenizer(bmlString));
    }
    
    @Test
    public void testReadXML() throws IOException
    {
        String str = "<faceLexeme "+TestUtil.getDefNS()+"id=\"face1\" amount=\"0.25\" start=\"1\" lexeme=\"lex1\"/>";
        FaceLexemeBehaviour beh = new FaceLexemeBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("face1", beh.id);
        assertEquals("bml1", beh.bmlId);
        assertEquals("lex1",beh.getStringParameterValue("lexeme"));
        assertEquals(0.25f,beh.getFloatParameterValue("amount"),PARAMETER_PRECISION);
        assertEquals(1,beh.getSyncPoints().get(0).getRef().offset,PARAMETER_PRECISION);
    }
    
    @Test
    public void writeReadXML() throws IOException
    {
        String str = "<faceLexeme "+TestUtil.getDefNS()+"id=\"face1\" type=\"LEXICALIZED\" amount=\"0.25\" start=\"1\" lexeme=\"lex1\"/>";
        FaceLexemeBehaviour behIn = new FaceLexemeBehaviour("bml1", new XMLTokenizer(str));
        
        StringBuilder buf = new StringBuilder();        
        behIn.appendXML(buf);        
        FaceLexemeBehaviour behOut = new FaceLexemeBehaviour("bml1", new XMLTokenizer(buf.toString()));
        assertEquals("face1", behOut.id);
        assertEquals("bml1", behOut.bmlId);
        assertEquals("lex1",behOut.getStringParameterValue("lexeme"));
        assertEquals(0.25f,behOut.getFloatParameterValue("amount"),PARAMETER_PRECISION);
        assertEquals(1,behOut.getSyncPoints().get(0).getRef().offset,PARAMETER_PRECISION);        
    }

    
   
}
