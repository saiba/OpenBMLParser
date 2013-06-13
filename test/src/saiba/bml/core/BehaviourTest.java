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

import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit test cases for the abstract behaviour class
 * @author hvanwelbergen
 * 
 */
public class BehaviourTest
{
    private static final double PRECISION = 0.001;

    private static class StubBehaviour extends Behaviour
    {

        public StubBehaviour(String bmlId)
        {
            super(bmlId);
        }

        @Override
        public float getFloatParameterValue(String name)
        {
            if (name.equals("test")) return 1;
            return super.getFloatParameterValue(name);
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
    public void testGetFloatThroughString()
    {
        StubBehaviour b = new StubBehaviour("bml1");
        b.readXML("<stubbehaviour " + TestUtil.getDefNS() + "id=\"beh1\"/>");
        assertEquals(1, Float.parseFloat(b.getStringParameterValue("test")), PRECISION);
    }

    @Test
    public void testId()
    {
        StubBehaviour b = new StubBehaviour("bml1");
        b.readXML("<stubbehaviour " + TestUtil.getDefNS() + "id=\"beh1\"/>");
        assertEquals("bml1", b.bmlId);
        assertEquals("beh1", b.id);
    }

    @Test
    public void testIdAndBmlId()
    {
        StubBehaviour b = new StubBehaviour("bml1");
        b.readXML("<stubbehaviour " + TestUtil.getDefNS() + "id=\"bml2:beh1\"/>");
        assertEquals("bml2", b.bmlId);
        assertEquals("beh1", b.id);
    }
}
