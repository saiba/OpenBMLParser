package saiba.bml.feedback;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit tests for XMLBMLBlockProgressTest 
 * @author Herwin
 *
 */
public class BMLBlockProgressFeedbackTest
{
    private static final double PRECISION = 0.00001;
    
    @Test
    public void testReadXML()
    {
        String str = "<blockProgress "+TestUtil.getDefNS()+"id=\"bml1:start\" globalTime=\"10\" characterId=\"doctor\"/>";
        BMLBlockProgressFeedback progress = new BMLBlockProgressFeedback();        
        progress.readXML(str);
        assertEquals("bml1",progress.getBmlId());
        assertEquals("start",progress.getSyncId());
        assertEquals(10, progress.getGlobalTime(), PRECISION);
        assertEquals("doctor", progress.getCharacterId());
    }
    
    @Test
    public void testWriteXML()
    {
        String str = "<blockProgress "+TestUtil.getDefNS()+"id=\"bml1:start\" globalTime=\"10\" characterId=\"doctor\"/>";
        
        BMLBlockProgressFeedback progressIn = new BMLBlockProgressFeedback();        
        progressIn.readXML(str);
        StringBuilder buf = new StringBuilder();
        progressIn.appendXML(buf);        
        
        BMLBlockProgressFeedback progressOut = new BMLBlockProgressFeedback();  
        progressOut.readXML(buf.toString());
        assertEquals("bml1",progressOut.getBmlId());
        assertEquals("start",progressOut.getSyncId());
        assertEquals(10, progressOut.getGlobalTime(), PRECISION);
        assertEquals("doctor", progressOut.getCharacterId());
    }
}
