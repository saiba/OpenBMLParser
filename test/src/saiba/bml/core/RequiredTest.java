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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import org.hamcrest.Matchers;
import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit test cases for the required block
 * @author welberge
 * 
 */
public class RequiredTest
{
    private RequiredBlock reqBlock = new RequiredBlock("bml1");

    @Test
    public void testEmptyRequired()
    {
        reqBlock.readXML("<required"+TestUtil.getDefNS()+"/>");
        assertThat(reqBlock.behaviours, Matchers.<Behaviour> empty());
        assertThat(reqBlock.constraintBlocks, Matchers.<ConstraintBlock> empty());
    }

    @Test
    public void testRequiredBehaviors()
    {
        reqBlock.readXML("<required"+TestUtil.getDefNS()+">" + "<gaze id=\"gaze1\" type=\"AT\" target=\"person1\"/>"
                + "<gaze id=\"gaze2\" type=\"AT\" target=\"person1\"/>" + "</required>");
        assertThat(reqBlock.behaviours, hasSize(2));
        assertThat(reqBlock.constraintBlocks, Matchers.<ConstraintBlock> empty());
    }

    @Test
    public void testRequiredConstraints()
    {
        reqBlock.readXML("<required"+TestUtil.getDefNS()+">" + "<constraint id=\"c1\"/>" + "<constraint id=\"c2\"><synchronize ref=\"beh3:start\">"
                + "<sync ref=\"beh1:start\"/><sync ref=\"beh2:start\"/>" + "</synchronize></constraint>" + "</required>");
        assertThat(reqBlock.behaviours, Matchers.<Behaviour> empty());
        assertThat(reqBlock.constraintBlocks, hasSize(2));
    }

    @Test
    public void testRequiredConstraintsAndBehaviors()
    {
        reqBlock.readXML("<required"+TestUtil.getDefNS()+">" + "<constraint id=\"c1\"/>" + "<gaze id=\"gaze1\" type=\"AT\" target=\"person1\"/>"
                + "<constraint id=\"c2\"><synchronize ref=\"beh3:start\">" + "<sync ref=\"beh1:start\"/><sync ref=\"beh2:start\"/>"
                + "</synchronize></constraint>" + "<gaze id=\"gaze1\" type=\"AT\" target=\"person1\"/>" + "</required>");
        assertThat(reqBlock.constraintBlocks, hasSize(2));
        assertThat(reqBlock.behaviours, hasSize(2));
    }
}
