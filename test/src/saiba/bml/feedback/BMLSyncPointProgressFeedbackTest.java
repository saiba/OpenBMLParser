package saiba.bml.feedback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * unit test cases for the XMLBMLSyncPointProgress
 * @author Herwin
 */
public class BMLSyncPointProgressFeedbackTest
{
    private static final double PRECISION = 0.00001;
    
    @Test
    public void testReadXML()
    {
        String str = "<syncPointProgress "+TestUtil.getDefNS()+" characterId=\"doctor\" id=\"bml1:gesture1:stroke\" time=\"10\" globalTime=\"111\"/>";
        BMLSyncPointProgressFeedback progress = new BMLSyncPointProgressFeedback();
        progress.readXML(str);
        assertEquals("doctor", progress.getCharacterId());
        assertEquals(10, progress.getTime(),PRECISION);
        assertEquals(111, progress.getGlobalTime(),PRECISION);        
    }
    
    @Test
    public void testWriteXML()
    {
        String str = "<syncPointProgress "+TestUtil.getDefNS()+"characterId=\"doctor\" id=\"bml1:gesture1:stroke\" time=\"10\" globalTime=\"111\"/>";
        BMLSyncPointProgressFeedback progressIn = new BMLSyncPointProgressFeedback();
        progressIn.readXML(str);
        StringBuilder buf = new StringBuilder();
        progressIn.appendXML(buf);        
        
        BMLSyncPointProgressFeedback progressOut = new BMLSyncPointProgressFeedback();  
        progressOut.readXML(buf.toString());        
        assertEquals("doctor", progressOut.getCharacterId());
        assertEquals(10, progressOut.getTime(),PRECISION);
        assertEquals(111, progressOut.getGlobalTime(),PRECISION);        
    }
    
    @Test
    public void testWriteXMLNoCharacterId()
    {
        String str = "<syncPointProgress "+TestUtil.getDefNS()+"id=\"bml1:gesture1:stroke\" time=\"10\" globalTime=\"111\"/>";
        BMLSyncPointProgressFeedback progressIn = new BMLSyncPointProgressFeedback();
        progressIn.readXML(str);
        StringBuilder buf = new StringBuilder();
        progressIn.appendXML(buf);        
        
        BMLSyncPointProgressFeedback progressOut = new BMLSyncPointProgressFeedback();  
        progressOut.readXML(buf.toString());        
        assertNull(progressOut.getCharacterId());
        assertEquals(10, progressOut.getTime(),PRECISION);
        assertEquals(111, progressOut.getGlobalTime(),PRECISION);        
    }
}
