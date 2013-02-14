package saiba.bml.feedback;

import static org.junit.Assert.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit tests for the BMLFeedbackParser
 * @author Herwin
 *
 */
public class BMLFeedbackParserTest
{
    @Test
    public void testInvalid() throws IOException
    {
        assertNull(BMLFeedbackParser.parseFeedback("blah"));
    }
    
    private void assertFeedbackType(Class<?>feedbackType, String str) throws IOException
    {
        BMLFeedback fb = BMLFeedbackParser.parseFeedback(str);
        assertThat(fb,instanceOf(feedbackType));
    }
    
    @Test
    public void testBMLBlockProgressFeedback() throws IOException
    {
        String str = "<blockProgress "+TestUtil.getDefNS()+
                "id=\"bml1:start\" globalTime=\"10\" characterId=\"doctor\"/>";
        assertFeedbackType(BMLBlockProgressFeedback.class, str);
    }
    
    @Test
    public void testBMLPredictionFeedback() throws IOException
    {
        String feedback = "<predictionFeedback "+TestUtil.getDefNS()+">"
                + "<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" " +
                        "id=\"bml1\" globalStart=\"1\" globalEnd=\"7\"/>"
                + "<gesture id=\"bml1:gesture1\" lexeme=\"BEAT\" start=\"0\" ready=\"1\" strokeStart=\"3\" " +
                "stroke=\"4\" strokeEnd=\"5\" relax=\"6\" end=\"7\"/>"
                + "<head id=\"bml1:head1\" lexeme=\"NOD\" start=\"0\" ready=\"1\" " +
                "strokeStart=\"3\" stroke=\"4\" strokeEnd=\"5\" relax=\"6\" end=\"7\"/>"
                + "</predictionFeedback>";
        assertFeedbackType(BMLPredictionFeedback.class, feedback);
    }
    
    @Test
    public void testBMLSyncPointProgressFeedback() throws IOException
    {
        String str = "<syncPointProgress "+TestUtil.getDefNS()+
                " characterId=\"doctor\" id=\"bml1:gesture1:stroke\" time=\"10\" globalTime=\"111\"/>";
        assertFeedbackType(BMLSyncPointProgressFeedback.class, str);
    }
    
    @Test
    public void testBMLWarningFeedback() throws IOException
    {
        String str = "<warningFeedback "+TestUtil.getDefNS()+"id=\"bml1\" characterId=\"doctor\" type=\"PARSING_FAILURE\">content</warningFeedback>";
        assertFeedbackType(BMLWarningFeedback.class, str);
    }
}


