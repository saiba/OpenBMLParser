package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import hmi.xml.XMLFormatting;
import hmi.xml.XMLNameSpace;

import java.io.IOException;

import org.junit.Test;

import saiba.bml.BMLInfo;

import com.google.common.collect.ImmutableList;

/**
 * Shared testcases for all Behaviours
 * @author welberge
 *
 */
public abstract class AbstractBehaviourTest
{
    protected abstract Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException;
    protected abstract Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException;
    private static final double PARAMETER_PRECISION = 0.0001;
    
    @Test
    public void testCustomFloatAttribute() throws IOException
    {
        BMLInfo.addCustomFloatAttribute(createBehaviour("bml1","").getClass(), "customfaceattributes","attr2");
        Behaviour beh = createBehaviour("bml1", "xmlns:custom=\"customfaceattributes\" custom:attr2=\"10\"");
        assertEquals(10, beh.getFloatParameterValue("customfaceattributes:attr2"),PARAMETER_PRECISION);
        assertTrue(beh.specifiesParameter("customfaceattributes:attr2"));        
    }
    
    @Test
    public void testCustomStringAttribute() throws IOException
    {
        BMLInfo.addCustomStringAttribute(createBehaviour("bml1","").getClass(), "customfaceattributes","attr1");
        Behaviour beh = createBehaviour("bml1", "xmlns:custom=\"customfaceattributes\" custom:attr1=\"blah\"");        
        assertEquals("blah", beh.getStringParameterValue("customfaceattributes:attr1"));    
        assertTrue(beh.specifiesParameter("customfaceattributes:attr1"));
        assertTrue(beh.satisfiesConstraint("customfaceattributes:attr1", "blah"));
    }
    
    @Test 
    public void testWriteCustomFloatAttribute()throws IOException
    {
        BMLInfo.addCustomFloatAttribute(createBehaviour("bml1","").getClass(), "customfaceattributes","attr2");
        Behaviour behIn = createBehaviour("bml1", "xmlns:custom=\"customfaceattributes\" custom:attr2=\"10\"");
        
        StringBuilder buf = new StringBuilder();        
        behIn.appendXML(buf, new XMLFormatting(), ImmutableList.of(new XMLNameSpace("custom","customfaceattributes")));        
        
        Behaviour behOut = parseBehaviour("bml1",buf.toString());
        assertEquals(10, behOut.getFloatParameterValue("customfaceattributes:attr2"),PARAMETER_PRECISION);
        assertTrue(behOut.specifiesParameter("customfaceattributes:attr2"));        
    }
    
    @Test
    public void testWriteCustomStringAttribute()throws IOException
    {
        BMLInfo.addCustomStringAttribute(createBehaviour("bml1","").getClass(), "customfaceattributes","attr1");
        Behaviour behIn = createBehaviour("bml1", "xmlns:custom=\"customfaceattributes\" custom:attr1=\"blah\"");
        
        StringBuilder buf = new StringBuilder();        
        behIn.appendXML(buf, new XMLFormatting(), ImmutableList.of(new XMLNameSpace("custom","customfaceattributes")));
        
        Behaviour behOut = parseBehaviour("bml1",buf.toString());
        assertEquals("blah", behOut.getStringParameterValue("customfaceattributes:attr1"));    
        assertTrue(behOut.specifiesParameter("customfaceattributes:attr1"));
    }
}
