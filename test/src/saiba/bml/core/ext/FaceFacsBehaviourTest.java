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
package saiba.bml.core.ext;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.bml.core.AbstractBehaviourTest;
import saiba.bml.core.Behaviour;

/**
 * Unit tests for the FaceFacesBehaviour
 * @author Herwin
 * 
 */
public class FaceFacsBehaviourTest extends AbstractBehaviourTest
{
    private static final double PARAMETER_PRECISION = 0.0001;

    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<faceFacs id=\"face1\" au=\"1\" side=\"BOTH\" xmlns=\"http://www.bml-initiative.org/bml/coreextensions-1.0\" "
                +extraAttributeString+"/>";
        return new FaceFacsBehaviour(bmlId,new XMLTokenizer(str));        
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new FaceFacsBehaviour(bmlId,new XMLTokenizer(bmlString));
    }
    
    @Test
    public void testReadXML() throws IOException
    {
        String str = "<faceFacs id=\"face1\" xmlns=\"http://www.bml-initiative.org/bml/coreextensions-1.0\" "
                + "amount=\"0.25\" start=\"1\" au=\"1\" side=\"BOTH\" />";
        FaceFacsBehaviour beh = new FaceFacsBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("face1", beh.id);
        assertEquals("bml1", beh.getBmlId());
        assertEquals("BOTH", beh.getStringParameterValue("side"));
        assertEquals(1, beh.getFloatParameterValue("au"), PARAMETER_PRECISION);
        assertEquals(1, beh.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }

    @Test
    public void testWriteXML() throws IOException
    {
        String str = "<faceFacs id=\"face1\" xmlns=\"http://www.bml-initiative.org/bml/coreextensions-1.0\" "
                + "amount=\"0.25\" start=\"1\" au=\"1\" side=\"BOTH\" />";
        FaceFacsBehaviour behIn = new FaceFacsBehaviour("bml1", new XMLTokenizer(str));
        StringBuilder buf = new StringBuilder();
        behIn.appendXML(buf, new XMLFormatting(), "xmlns", "http://www.bml-initiative.org/bml/coreextensions-1.0");
        FaceFacsBehaviour behOut = new FaceFacsBehaviour("bml1", new XMLTokenizer(buf.toString()));

        assertEquals("face1", behOut.id);
        assertEquals("bml1", behOut.getBmlId());
        assertEquals("BOTH", behOut.getStringParameterValue("side"));
        assertEquals(1, behOut.getFloatParameterValue("au"), PARAMETER_PRECISION);
        assertEquals(1, behOut.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }

    
}
