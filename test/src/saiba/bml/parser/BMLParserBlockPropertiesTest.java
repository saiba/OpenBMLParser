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
package saiba.bml.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import saiba.bml.core.Behaviour;
import saiba.bml.core.BehaviourBlock;
import saiba.utils.TestUtil;

/**
 * Tests BML block properties
 * @author welberge
 * 
 */
public class BMLParserBlockPropertiesTest
{
    private BMLParser parser = new BMLParser();

    @Before
    public void setup()
    {

    }

    @Test
    public void testDirectLink()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.readXML("<bml "+TestUtil.getDefNS()+"id=\"bml1\"><gesture id=\"g1\" start=\"g2:start\" lexeme=\"BEAT\"/>"
                + "<gesture id=\"g2\" lexeme=\"BEAT\"/></bml>");
        parser.addBehaviourBlock(bb);
        assertTrue(parser.directLink("bml1", "g1", "bml1", "g2"));
    }

    @Test
    public void testNoDirectLink()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.readXML("<bml "+TestUtil.getDefNS()+"id=\"bml1\"><gesture id=\"g1\" lexeme=\"BEAT\"/>" + "<gesture id=\"g2\" lexeme=\"BEAT\"/></bml>");
        parser.addBehaviourBlock(bb);
        assertFalse(parser.directLink("bml1", "g1", "bml1", "g2"));
    }

    @Test
    public void testDirectGrounded()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.readXML("<bml "+TestUtil.getDefNS()+"id=\"bml1\"><gesture id=\"g1\" start=\"6\" lexeme=\"BEAT\"/></bml>");
        parser.addBehaviourBlock(bb);
        assertTrue(parser.directGround("bml1", "g1"));
    }

    @Test
    public void testNotDirectGrounded()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.readXML("<bml "+TestUtil.getDefNS()+"id=\"bml1\"><gesture id=\"g1\" start=\"g2:start\" lexeme=\"BEAT\"/>"
                + "<gesture id=\"g2\" lexeme=\"BEAT\"/></bml>");
        parser.addBehaviourBlock(bb);
        assertFalse(parser.directGround("bml1", "g1"));
    }

    @Test
    public void testGetUngroundedLoops()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.readXML("<bml "+TestUtil.getDefNS()+"id=\"bml1\"><gesture id=\"g1\" start=\"g2:start\" end=\"g2:end\" lexeme=\"BEAT\"/>"
                + "<gesture id=\"g2\" lexeme=\"BEAT\"/></bml>");
        parser.addBehaviourBlock(bb);
        List<List<Behaviour>> list = parser.getUngroundedLoops("bml1", "g1");
        assertEquals(1, list.size());
        assertThat(list.get(0), IsIterableContainingInAnyOrder.containsInAnyOrder(parser.getBehaviour("bml1", "g2")));
    }

    @Test
    public void testGetUngroundedLoops2()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.readXML("<bml "+TestUtil.getDefNS()+"id=\"bml1\"><gesture id=\"g1\" start=\"g2:start\" lexeme=\"BEAT\"/>"
                + "<gesture id=\"g2\" lexeme=\"BEAT\" end=\"g3:end\"/>" + 
                "<gesture id=\"g3\" stroke=\"g1:stroke\" lexeme=\"BEAT\"/></bml>");
        parser.addBehaviourBlock(bb);
        List<List<Behaviour>> list = parser.getUngroundedLoops("bml1", "g1");
        assertEquals(1, list.size());
        assertThat(list.get(0),
                IsIterableContainingInAnyOrder.containsInAnyOrder(parser.getBehaviour("bml1", "g2"), parser.getBehaviour("bml1", "g3")));

        list = parser.getUngroundedLoops("bml1", "g2");
        assertEquals(1, list.size());
        assertThat(list.get(0),
                IsIterableContainingInAnyOrder.containsInAnyOrder(parser.getBehaviour("bml1", "g1"), parser.getBehaviour("bml1", "g3")));

        list = parser.getUngroundedLoops("bml1", "g3");
        assertEquals(1, list.size());
        assertThat(list.get(0),
                IsIterableContainingInAnyOrder.containsInAnyOrder(parser.getBehaviour("bml1", "g1"), parser.getBehaviour("bml1", "g2")));
    }

    @Test
    public void testGetNoUngroundedLoopsWhenGrounded()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.readXML("<bml "+TestUtil.getDefNS()+"id=\"bml1\"><gesture id=\"g1\" start=\"g2:start\" lexeme=\"BEAT\"/>"
                + "<gesture start=\"1\" id=\"g2\" lexeme=\"BEAT\" end=\"g3:end\"/>"
                + "<gesture id=\"g3\" stroke=\"g1:stroke\" lexeme=\"BEAT\"/></bml>");
        parser.addBehaviourBlock(bb);
        List<List<Behaviour>> list = parser.getUngroundedLoops("bml1", "g1");
        assertEquals(0, list.size());

        list = parser.getUngroundedLoops("bml1", "g2");
        assertEquals(0, list.size());

        list = parser.getUngroundedLoops("bml1", "g3");
        assertEquals(0, list.size());
    }

    @Test
    public void testGetNoUngroundedLoopsWhenGrounded2()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.readXML("<bml "+TestUtil.getDefNS()+"id=\"bml1\"><gesture id=\"g1\" start=\"g2:start\" lexeme=\"BEAT\"/>"
                + "<gesture stroke=\"1\" id=\"g2\" lexeme=\"BEAT\" end=\"g3:end\"/>"
                + "<gesture id=\"g3\" stroke=\"g1:stroke\" lexeme=\"BEAT\"/></bml>");
        parser.addBehaviourBlock(bb);
        List<List<Behaviour>> list = parser.getUngroundedLoops("bml1", "g1");
        assertEquals(0, list.size());

        list = parser.getUngroundedLoops("bml1", "g2");
        assertEquals(1, list.size());
        assertThat(list.get(0),
                IsIterableContainingInAnyOrder.containsInAnyOrder(parser.getBehaviour("bml1", "g1"), parser.getBehaviour("bml1", "g3")));

        list = parser.getUngroundedLoops("bml1", "g3");
        assertEquals(0, list.size());
    }
}
