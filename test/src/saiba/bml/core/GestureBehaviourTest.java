package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit tests for the GestureBehaviour
 * @author welberge
 * 
 */
public class GestureBehaviourTest extends AbstractBehaviourTest
{
    private static final double PARAMETER_PRECISION = 0.0001;

    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<gesture "+TestUtil.getDefNS()+"id=\"g1\" lexeme=\"BEAT\" " + extraAttributeString + "/>";
        return new GestureBehaviour(bmlId, new XMLTokenizer(str));
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new GestureBehaviour(bmlId, new XMLTokenizer(bmlString));
    }

    @Test
    public void testReadXML() throws IOException
    {
        String str = "<gesture "+TestUtil.getDefNS()+"start=\"1\" id=\"g1\" mode=\"LEFT_HAND\" lexeme=\"BEAT\"/>";
        GestureBehaviour beh = new GestureBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("g1", beh.id);
        assertEquals("bml1", beh.bmlId);
        assertEquals("LEFT_HAND", beh.getStringParameterValue("mode"));
        assertEquals("BEAT", beh.getStringParameterValue("lexeme"));
        assertEquals(1, beh.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }

    @Test
    public void testWriteXML() throws IOException
    {
        String str = "<gesture "+TestUtil.getDefNS()+"start=\"1\" id=\"g1\" mode=\"LEFT_HAND\" lexeme=\"BEAT\"/>";
        GestureBehaviour behIn = new GestureBehaviour("bml1", new XMLTokenizer(str));
        StringBuilder buf = new StringBuilder();
        behIn.appendXML(buf);

        GestureBehaviour behOut = new GestureBehaviour("bml1", new XMLTokenizer(buf.toString()));
        assertEquals("g1", behOut.id);
        assertEquals("bml1", behOut.bmlId);
        assertEquals("LEFT_HAND", behOut.getStringParameterValue("mode"));
        assertEquals("BEAT", behOut.getStringParameterValue("lexeme"));
        assertEquals(1, behOut.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }

}
