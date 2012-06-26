package saiba.bml.feedback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import saiba.bml.core.Behaviour;
import saiba.bml.parser.SyncPoint;
import saiba.utils.TestUtil;

/**
 * Unit testcases for BMLPredictionFeedback
 * @author hvanwelbergen
 * 
 */
public class BMLPredictionFeedbackTest
{
    private static final double PREDICTION_PRECISION = 0.0001;
    
    private void assertEqualBlockFeedbackElement(BMLBlockPredictionFeedback fb1, BMLBlockPredictionFeedback fb2)
    {
        assertEquals(fb1.getId(),fb2.getId());
        assertEquals(fb1.getGlobalStart(),fb2.getGlobalStart(),PREDICTION_PRECISION);
        assertEquals(fb1.getGlobalEnd(),fb2.getGlobalEnd(),PREDICTION_PRECISION);
    }
    
    private void assertSyncsWithBMLOffset(List<SyncPoint> syncs, String bmlId, String behaviourId)
    {
        for(SyncPoint s:syncs)
        {
            assertEquals(behaviourId,s.getBehaviourId());
            assertEquals(bmlId,s.getBmlId());
            assertNotNull("Null syncref for "+s,s.getRef());
            assertEquals("bml",s.getRef().sourceId);
            assertEquals("start",s.getRef().syncId);
        }
    }
    
    private void assertSyncsIds(List<SyncPoint> syncs, String ...names)
    {
        int i=0;
        for(SyncPoint s:syncs)
        {
            assertEquals(names[i],s.getName());
            i++;
        }
    }
    
    private void assertSyncsTimes(List<SyncPoint> syncs, int ...times)
    {
        int i=0;
        for(SyncPoint s:syncs)
        {
            assertEquals(s.getRef().offset,times[i],PREDICTION_PRECISION);
            i++;
        }
    }
    
    @Test
    public void testReadXML()
    {
        String feedback = "<predictionFeedback "+TestUtil.getDefNS()+">"
                + "<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" " +
                		"id=\"bml1\" globalStart=\"1\" globalEnd=\"7\"/>"
                + "<gesture id=\"bml1:gesture1\" lexeme=\"BEAT\" start=\"0\" ready=\"1\" strokeStart=\"3\" " +
                "stroke=\"4\" strokeEnd=\"5\" relax=\"6\" end=\"7\"/>"
                + "<head id=\"bml1:head1\" lexeme=\"NOD\" start=\"0\" ready=\"1\" " +
                "strokeStart=\"3\" stroke=\"4\" strokeEnd=\"5\" relax=\"6\" end=\"7\"/>"
                + "</predictionFeedback>";
        BMLPredictionFeedback fb = new BMLPredictionFeedback();
        fb.readXML(feedback);
        assertEquals(1,fb.getBmlBlockPredictions().size());
        assertEquals(2,fb.getBmlBehaviorPredictions().size());
        assertEqualBlockFeedbackElement(new BMLBlockPredictionFeedback("bml1",1,7), fb.getBmlBlockPredictions().get(0));
        
        Behaviour b1 = fb.getBmlBehaviorPredictions().get(0);
        assertEquals("gesture1",b1.id);
        assertEquals("bml1", b1.getBmlId());
        assertSyncsWithBMLOffset(b1.getSyncPoints(),"bml1","gesture1");
        assertSyncsIds(b1.getSyncPoints(),"start", "ready","strokeStart","stroke","strokeEnd","relax", "end");
        assertSyncsTimes(b1.getSyncPoints(),0,1,3,4,5,6,7);
        
        Behaviour b2 = fb.getBmlBehaviorPredictions().get(1);
        assertEquals("head1",b2.id);
        assertEquals("bml1", b2.getBmlId());
        assertSyncsWithBMLOffset(b2.getSyncPoints(),"bml1","head1");
        assertSyncsIds(b2.getSyncPoints(),"start","ready","strokeStart","stroke","strokeEnd","relax","end");
        assertSyncsTimes(b2.getSyncPoints(),0,1,3,4,5,6,7);
    }
    
    @Test
    public void testReadXMLWithSpeechSync()
    {
        String feedback = "<predictionFeedback "+TestUtil.getDefNS()+">"
                + "<speech id=\"bml1:s1\" start=\"1\" end=\"10\"><text>Hello <sync id=\"sync1\" ref=\"2\"/> world</text></speech>"
                + "</predictionFeedback>";
        BMLPredictionFeedback fb = new BMLPredictionFeedback();
        fb.readXML(feedback);        
        
        assertEquals(0,fb.getBmlBlockPredictions().size());
        assertEquals(1,fb.getBmlBehaviorPredictions().size());      
        Behaviour b1 = fb.getBmlBehaviorPredictions().get(0);
        assertEquals("s1",b1.id);
        assertEquals("bml1", b1.getBmlId());
        assertEquals(3,b1.getSyncPoints().size());
        assertSyncsWithBMLOffset(b1.getSyncPoints(),"bml1","s1");
        assertSyncsIds(b1.getSyncPoints(),"start","end","sync1");
        assertSyncsTimes(b1.getSyncPoints(),1,10,2);
    }
    
    @Test
    public void testWriteXMLBlock()
    {
        BMLPredictionFeedback fbIn = new BMLPredictionFeedback();
        fbIn.setCharacterId("doctor");
        BMLBlockPredictionFeedback bpf = new BMLBlockPredictionFeedback("bml1",10,20);
        fbIn.addBMLBlockPrediction(bpf);        
        StringBuilder buf = new StringBuilder();

        fbIn.appendXML(buf);        
        BMLPredictionFeedback fbOut = new BMLPredictionFeedback();
        fbOut.readXML(buf.toString());
        assertEquals("doctor",fbOut.getCharacterId());
        assertEquals(1,fbOut.getBmlBlockPredictions().size());
        assertEquals("bml1",fbOut.getBmlBlockPredictions().get(0).getId());
        assertEquals(10,fbOut.getBmlBlockPredictions().get(0).getGlobalStart(),PREDICTION_PRECISION);
        assertEquals(20,fbOut.getBmlBlockPredictions().get(0).getGlobalEnd(),PREDICTION_PRECISION);
    }
}
