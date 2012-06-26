package saiba.bml.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit test cases for the abstract behaviour class
 * @author hvanwelbergen
 *
 */
public class BehaviourTest
{
    private static class StubBehaviour extends Behaviour
    {

        public StubBehaviour(String bmlId)
        {
            super(bmlId);            
        }

        @Override
        public float getFloatParameterValue(String name)
        {
            return 0;
        }

        @Override
        public String getStringParameterValue(String name)
        {
            return null;
        }

        @Override
        public boolean specifiesParameter(String name)
        {
            return false;
        }      
        
        private static final String XMLTAG = "stubbehaviour";
        public static String xmlTag()
        {
            return XMLTAG;
        }
        @Override
        public String getXMLTag()
        {
            return XMLTAG;
        }
        @Override
        public void addDefaultSyncPoints()
        {
                 
        }
    }
    
    @Test
    public void testId()
    {
        StubBehaviour b = new StubBehaviour("bml1");
        b.readXML("<stubbehaviour "+TestUtil.getDefNS()+"id=\"beh1\"/>");
        assertEquals("bml1", b.bmlId);
        assertEquals("beh1", b.id);
    }
    
    @Test
    public void testIdAndBmlId()
    {
        StubBehaviour b = new StubBehaviour("bml1");
        b.readXML("<stubbehaviour "+TestUtil.getDefNS()+"id=\"bml2:beh1\"/>");
        assertEquals("bml2", b.bmlId);
        assertEquals("beh1", b.id);
    }
}
