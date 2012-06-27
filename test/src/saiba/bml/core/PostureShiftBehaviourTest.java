package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * unit tests for the PostureBehaviour
 * @author welberge
 * 
 */
public class PostureShiftBehaviourTest extends AbstractBehaviourTest
{
    private static final double PARAMETER_PRECISION = 0.0001;

    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<postureShift "+TestUtil.getDefNS()+" id=\"posture1\" " + extraAttributeString + ">"
                + "<stance type=\"STANDING\"/><pose lexeme=\"LEANING_FORWARD\" " 
                + "part=\"WHOLE_BODY\"/></postureShift>";
        return new PostureShiftBehaviour("bml1", new XMLTokenizer(str));
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new PostureShiftBehaviour("bml1", new XMLTokenizer(bmlString));
    }

    @Test
    public void testReadXML() throws IOException
    {
        String str = "<postureShift "+TestUtil.getDefNS()+"start=\"1\" id=\"posture1\"><stance type=\"STANDING\"/><pose lexeme=\"LEANING_FORWARD\" "
                + "part=\"WHOLE_BODY\"/></postureShift>";
        PostureShiftBehaviour beh = new PostureShiftBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("posture1", beh.id);
        assertEquals("bml1", beh.bmlId);
        assertEquals("STANDING", beh.getStringParameterValue("stance"));
        Pose p = beh.getPoseParts().get(0);
        assertEquals("LEANING_FORWARD", p.getLexeme());
        assertEquals("WHOLE_BODY", p.getPart());
        assertEquals(1, beh.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }

    @Test
    public void testWriteXML() throws IOException
    {
        String str = "<postureShift "+TestUtil.getDefNS()+"start=\"1\" id=\"posture1\"><stance type=\"STANDING\"/><pose lexeme=\"LEANING_FORWARD\" "
                + "part=\"WHOLE_BODY\"/></postureShift>";
        PostureShiftBehaviour behIn = new PostureShiftBehaviour("bml1", new XMLTokenizer(str));
        StringBuilder buf = new StringBuilder();
        behIn.appendXML(buf);
        PostureShiftBehaviour behOut = new PostureShiftBehaviour("bml1", new XMLTokenizer(buf.toString()));

        assertEquals("posture1", behOut.id);
        assertEquals("bml1", behOut.bmlId);
        assertEquals("STANDING", behOut.getStringParameterValue("stance"));
        Pose p = behOut.getPoseParts().get(0);
        assertEquals("LEANING_FORWARD", p.getLexeme());
        assertEquals("WHOLE_BODY", p.getPart());
        assertEquals(1, behOut.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }

}
