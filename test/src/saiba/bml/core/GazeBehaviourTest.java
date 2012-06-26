package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit test cases for the GazeBehaviour
 * @author Herwin
 */
public class GazeBehaviourTest extends AbstractBehaviourTest
{
    private static final double PARAMETER_PRECISION = 0.0001;

    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<gaze "+TestUtil.getDefNS()+"id=\"gaze1\" target=\"bluebox\" " + extraAttributeString + " influence=\"NECK\"/>";
        return new GazeBehaviour(bmlId, new XMLTokenizer(str));
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new GazeBehaviour(bmlId, new XMLTokenizer(bmlString));
    }

    @Test
    public void testReadXML() throws IOException
    {
        String str = "<gaze "+TestUtil.getDefNS()+"id=\"gaze1\" target=\"bluebox\" offsetAngle=\"10\" offsetDirection=\"RIGHT\""
                + " start=\"1\" influence=\"NECK\"/>";
        GazeBehaviour beh = new GazeBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("gaze1", beh.id);
        assertEquals("bml1", beh.bmlId);
        assertEquals("bluebox", beh.getStringParameterValue("target"));
        assertEquals("NECK", beh.getStringParameterValue("influence"));
        assertEquals("RIGHT", beh.getStringParameterValue("offsetDirection"));
        assertEquals(10, beh.getFloatParameterValue("offsetAngle"), PARAMETER_PRECISION);
        assertEquals(1, beh.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }

    @Test
    public void writeReadXML() throws IOException
    {
        String str = "<gaze "+TestUtil.getDefNS()+"id=\"gaze1\" target=\"bluebox\" offsetAngle=\"10\" offsetDirection=\"RIGHT\""
                + " start=\"1\" influence=\"NECK\"/>";
        GazeBehaviour behIn = new GazeBehaviour("bml1", new XMLTokenizer(str));

        StringBuilder buf = new StringBuilder();

        behIn.appendXML(buf);
        GazeBehaviour behOut = new GazeBehaviour("bml1", new XMLTokenizer(buf.toString()));
        assertEquals("gaze1", behOut.id);
        assertEquals("bml1", behOut.bmlId);
        assertEquals("bluebox", behOut.getStringParameterValue("target"));
        assertEquals("NECK", behOut.getStringParameterValue("influence"));
        assertEquals("RIGHT", behOut.getStringParameterValue("offsetDirection"));
        assertEquals(10, behOut.getFloatParameterValue("offsetAngle"), PARAMETER_PRECISION);
        assertEquals(1, behOut.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }
}
