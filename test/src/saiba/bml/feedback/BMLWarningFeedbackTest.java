package saiba.bml.feedback;

import static org.junit.Assert.*;

import org.junit.Test;

import saiba.bml.feedback.BMLWarningFeedback;
import saiba.utils.TestUtil;

/**
 * unit tests for XMLBMLWarning
 * @author Herwin
 *
 */
public class BMLWarningFeedbackTest
{
    @Test
    public void testReadXML()
    {
        String str = "<warningFeedback "+TestUtil.getDefNS()+"id=\"bml1\" characterId=\"doctor\" type=\"PARSING_FAILURE\">content</warningFeedback>";
        BMLWarningFeedback warning = new BMLWarningFeedback();
        warning.readXML(str);
        assertEquals("bml1",warning.getId());
        assertEquals("doctor",warning.getCharacterId());
        assertEquals(BMLWarningFeedback.PARSING_FAILURE,warning.getType());
        assertEquals("content",warning.getDescription());
    }
    
    @Test
    public void testWriteXML()
    {
        String str = "<warningFeedback "+TestUtil.getDefNS()+"id=\"bml1\" characterId=\"doctor\" type=\"PARSING_FAILURE\">content</warningFeedback>";
        BMLWarningFeedback warningIn = new BMLWarningFeedback();
        warningIn.readXML(str);
        StringBuilder buf = new StringBuilder();
        warningIn.appendXML(buf);     
        
        BMLWarningFeedback warningOut = new BMLWarningFeedback();
        warningOut.readXML(buf.toString());
        assertEquals("bml1",warningOut.getId());
        assertEquals("doctor",warningOut.getCharacterId());
        assertEquals(BMLWarningFeedback.PARSING_FAILURE,warningOut.getType());
        assertEquals("content",warningOut.getDescription());
    }
}
