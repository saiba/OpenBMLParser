package saiba.bml.core.ext;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.bml.core.AbstractBehaviourTest;
import saiba.bml.core.Behaviour;

/**
 * Unit tests for the FaceFacesBehaviour
 * @author Herwin
 * 
 */
public class FaceFacsBehaviourTest extends AbstractBehaviourTest
{
    private static final double PARAMETER_PRECISION = 0.0001;

    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<faceFacs id=\"face1\" au=\"1\" side=\"BOTH\" xmlns=\"http://www.bml-initiative.org/bml/coreextensions-1.0\" "
                +extraAttributeString+"/>";
        return new FaceFacsBehaviour(bmlId,new XMLTokenizer(str));        
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new FaceFacsBehaviour(bmlId,new XMLTokenizer(bmlString));
    }
    
    @Test
    public void testReadXML() throws IOException
    {
        String str = "<faceFacs id=\"face1\" xmlns=\"http://www.bml-initiative.org/bml/coreextensions-1.0\" "
                + "amount=\"0.25\" start=\"1\" au=\"1\" side=\"BOTH\" />";
        FaceFacsBehaviour beh = new FaceFacsBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("face1", beh.id);
        assertEquals("bml1", beh.getBmlId());
        assertEquals("BOTH", beh.getStringParameterValue("side"));
        assertEquals(1, beh.getFloatParameterValue("au"), PARAMETER_PRECISION);
        assertEquals(1, beh.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }

    @Test
    public void testWriteXML() throws IOException
    {
        String str = "<faceFacs id=\"face1\" xmlns=\"http://www.bml-initiative.org/bml/coreextensions-1.0\" "
                + "amount=\"0.25\" start=\"1\" au=\"1\" side=\"BOTH\" />";
        FaceFacsBehaviour behIn = new FaceFacsBehaviour("bml1", new XMLTokenizer(str));
        StringBuilder buf = new StringBuilder();
        behIn.appendXML(buf, new XMLFormatting(), "xmlns", "http://www.bml-initiative.org/bml/coreextensions-1.0");
        FaceFacsBehaviour behOut = new FaceFacsBehaviour("bml1", new XMLTokenizer(buf.toString()));

        assertEquals("face1", behOut.id);
        assertEquals("bml1", behOut.getBmlId());
        assertEquals("BOTH", behOut.getStringParameterValue("side"));
        assertEquals(1, behOut.getFloatParameterValue("au"), PARAMETER_PRECISION);
        assertEquals(1, behOut.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }

    
}
