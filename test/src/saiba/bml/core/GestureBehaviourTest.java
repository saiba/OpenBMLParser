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
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit tests for the GestureBehaviour
 * @author welberge
 * 
 */
public class GestureBehaviourTest extends AbstractBehaviourTest
{
    private static final double PARAMETER_PRECISION = 0.0001;

    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<gesture "+TestUtil.getDefNS()+"id=\"g1\" lexeme=\"BEAT\" " + extraAttributeString + "/>";
        return new GestureBehaviour(bmlId, new XMLTokenizer(str));
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new GestureBehaviour(bmlId, new XMLTokenizer(bmlString));
    }

    @Test
    public void testReadXML() throws IOException
    {
        String str = "<gesture "+TestUtil.getDefNS()+"start=\"1\" id=\"g1\" mode=\"LEFT_HAND\" lexeme=\"BEAT\"/>";
        GestureBehaviour beh = new GestureBehaviour("bml1", new XMLTokenizer(str));
        assertEquals("g1", beh.id);
        assertEquals("bml1", beh.bmlId);
        assertEquals("LEFT_HAND", beh.getStringParameterValue("mode"));
        assertEquals("BEAT", beh.getStringParameterValue("lexeme"));
        assertEquals(1, beh.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }

    @Test
    public void testWriteXML() throws IOException
    {
        String str = "<gesture "+TestUtil.getDefNS()+"start=\"1\" id=\"g1\" mode=\"LEFT_HAND\" lexeme=\"BEAT\"/>";
        GestureBehaviour behIn = new GestureBehaviour("bml1", new XMLTokenizer(str));
        StringBuilder buf = new StringBuilder();
        behIn.appendXML(buf);

        GestureBehaviour behOut = new GestureBehaviour("bml1", new XMLTokenizer(buf.toString()));
        assertEquals("g1", behOut.id);
        assertEquals("bml1", behOut.bmlId);
        assertEquals("LEFT_HAND", behOut.getStringParameterValue("mode"));
        assertEquals("BEAT", behOut.getStringParameterValue("lexeme"));
        assertEquals(1, behOut.getSyncPoints().get(0).getRef().offset, PARAMETER_PRECISION);
    }

}
