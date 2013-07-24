/*******************************************************************************
 * Copyright (c) 2013 University of Twente, Bielefeld University
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void testNoFloatAttribute() throws IOException
    {
        BMLInfo.addCustomFloatAttribute(createBehaviour("bml1","").getClass(), "customfaceattributes","attr2");
        Behaviour beh = createBehaviour("bml1", "xmlns:custom=\"customfaceattributes\"");
        assertFalse(beh.specifiesParameter("customfaceattributes:attr2"));        
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
    public void testNoCustomStringAttribute() throws IOException
    {
        BMLInfo.addCustomStringAttribute(createBehaviour("bml1","").getClass(), "customfaceattributes","attr1");
        Behaviour beh = createBehaviour("bml1", "xmlns:custom=\"customfaceattributes\" ");    
        assertFalse(beh.specifiesParameter("customfaceattributes:attr2"));        
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
    
    @Test
    public void testWriteCustomStringAttributeNoPrefix()throws IOException
    {
        BMLInfo.addCustomStringAttribute(createBehaviour("bml1","").getClass(), "customfaceattributes","attr1");
        Behaviour behIn = createBehaviour("bml1", "xmlns:custom=\"customfaceattributes\" custom:attr1=\"blah\"");
        
        StringBuilder buf = new StringBuilder();        
        behIn.appendXML(buf);
        
        Behaviour behOut = parseBehaviour("bml1",buf.toString());
        assertEquals("blah", behOut.getStringParameterValue("customfaceattributes:attr1"));    
        assertTrue(behOut.specifiesParameter("customfaceattributes:attr1"));
    }
    
    
    @Test
    public void testWriteCustomStringAttributeNoPrefix2()throws IOException
    {
        BMLInfo.addCustomStringAttribute(createBehaviour("bml1","").getClass(), "http://customfaceattributes.com","attr1");
        Behaviour behIn = createBehaviour("bml1", "xmlns:custom=\"http://customfaceattributes.com\" custom:attr1=\"blah\"");
        
        StringBuilder buf = new StringBuilder();        
        behIn.appendXML(buf);
        System.out.println(buf);
        
        Behaviour behOut = parseBehaviour("bml1",buf.toString());
        assertEquals("blah", behOut.getStringParameterValue("http://customfaceattributes.com:attr1"));    
        assertTrue(behOut.specifiesParameter("http://customfaceattributes.com:attr1"));
    }
}
