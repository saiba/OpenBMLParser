/*******************************************************************************
 * Copyright (C) 2009 Human Media Interaction, University of Twente, the Netherlands
 * 
 * This file is part of the Elckerlyc BML realizer.
 * 
 * Elckerlyc is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Elckerlyc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Elckerlyc.  If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/
package saiba.bml.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static saiba.bml.parser.ParserTestUtil.assertEqualAfterConstraints;
import static saiba.bml.parser.ParserTestUtil.assertEqualConstraints;
import hmi.util.Resources;
import hmi.xml.XMLFormatting;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import saiba.bml.BMLInfo;
import saiba.bml.core.BMLBehaviorAttributeExtension;
import saiba.bml.core.BMLBlockComposition;
import saiba.bml.core.BehaviourBlock;
import saiba.bml.core.CoreComposition;
import saiba.utils.TestUtil;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Unit test cases for the BMLParser
 * 
 * @author Herwin
 * 
 */
public class ParserTest
{
    private Resources res;

    private BMLParser parser;

    private List<ExpectedConstraint> expectedConstraints;
    private List<ExpectedConstraint> expectedReqConstraints;
    private static final int PARSE_TIMEOUT = 300;

    @Before
    public void setup()
    {

        res = new Resources("bmltest");
        expectedConstraints = new ArrayList<ExpectedConstraint>();
        expectedReqConstraints = new ArrayList<ExpectedConstraint>();
        parser = new BMLParser();
    }

    private void readXML(String file) throws IOException
    {
        parser.clear();
        BehaviourBlock block = new BehaviourBlock();
        block.readXML(res.getReader(file));
        parser.addBehaviourBlock(block);
    }

    private void read(String bml)
    {
        read(bml, new BehaviourBlock());
    }

    private void readNotStrict(String bml)
    {
        read(bml, new BehaviourBlock(false));
    }
    
    private void read(String bml, BehaviourBlock block)
    {
        parser.clear();
        block.readXML(bml);
        parser.addBehaviourBlock(block);
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testRequiredConstraints() throws IOException
    {
        readXML("testspeech_synctimed2x_required.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 10));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "s1", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech2", "s1", -10));
        expectedConstraints.add(expected1);
        assertEqualConstraints(expectedConstraints, parser.getConstraints());

        ExpectedConstraint expectedReq = new ExpectedConstraint();
        expectedReq.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "s1", 10));
        expectedReq.expectedSyncs.add(new ExpectedSync("bml1", "speech2", "s1", 0));
        expectedReqConstraints.add(expectedReq);
        assertEqualConstraints(expectedReqConstraints, parser.getRequiredConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testRequiredBehavior() throws IOException
    {
        readXML("testrequiredbeh.xml");
        assertEquals(4, parser.getBehaviours().size());
        assertTrue(parser.getBehaviour("bml1", "h1").isRequired());
        assertTrue(parser.getBehaviour("bml1", "h2").isRequired());
        assertFalse(parser.getBehaviour("bml1", "h3").isRequired());
        assertFalse(parser.getBehaviour("bml1", "h4").isRequired());

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 4));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h1", "start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h2", "start", -2));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h3", "start", -4));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h4", "start", -6));
        expectedConstraints.add(expected1);
        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void afterConstraintTest() throws IOException
    {
        readXML("after.xml");

        ExpectedAfterConstraint expected = new ExpectedAfterConstraint();
        expected.expectedSync.add(new ExpectedSync("bml1", "nod1", "stroke", 0));
        expected.expectedSync.add(new ExpectedSync("bml1", "beat1", "stroke", 2));
        expected.expectedRef = new ExpectedSync("bml1", "speech1", "sync4", 0);
        assertEqualAfterConstraints(ImmutableList.of(expected), parser.getAfterConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void afterConstraintTestRefOffset() throws IOException
    {
        readXML("after2.xml");
        ExpectedAfterConstraint expected = new ExpectedAfterConstraint();
        expected.expectedRef = new ExpectedSync("bml1", "speech1", "sync4", 0);        
        expected.expectedSync.add(new ExpectedSync("bml1", "nod1", "stroke", -1));
        expected.expectedSync.add(new ExpectedSync("bml1", "beat1", "stroke", 1));
        assertEqualAfterConstraints(ImmutableList.of(expected), parser.getAfterConstraints());
    }
    
    @Test(timeout = PARSE_TIMEOUT)
    public void afterUnificationTest() throws IOException
    {
        readXML("afterunify.xml");
        
        ExpectedAfterConstraint expected = new ExpectedAfterConstraint();
        expected.expectedRef = new ExpectedSync("bml1", "speech1", "sync4", 0);        
        expected.expectedSync.add(new ExpectedSync("bml1", "nod1", "stroke", 0));
        expected.expectedSync.add(new ExpectedSync("bml1", "beat1", "stroke", 2));
        expected.expectedSync.add(new ExpectedSync("bml1", "beat1", "end", 5));
        expected.expectedSync.add(new ExpectedSync("bml1", "nod1", "end", 6));
        assertEqualAfterConstraints(ImmutableList.of(expected), parser.getAfterConstraints());
    }
    
    @Test(timeout = PARSE_TIMEOUT)
    public void afterUnificationOffsetTest() throws IOException
    {
        readXML("afterunify_offset.xml");
        
        ExpectedAfterConstraint expected = new ExpectedAfterConstraint();
        expected.expectedRef = new ExpectedSync("bml1", "speech1", "sync4", 0);        
        expected.expectedSync.add(new ExpectedSync("bml1", "nod1", "stroke", 0));
        expected.expectedSync.add(new ExpectedSync("bml1", "beat1", "stroke", 2));
        expected.expectedSync.add(new ExpectedSync("bml1", "beat1", "end", -5));
        expected.expectedSync.add(new ExpectedSync("bml1", "nod1", "end", -4));
        assertEqualAfterConstraints(ImmutableList.of(expected), parser.getAfterConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void afterConstraintRequiredTest() throws IOException
    {
        readXML("after_required.xml");

        ExpectedAfterConstraint expected = new ExpectedAfterConstraint();
        expected.expectedSync.add(new ExpectedSync("bml1", "nod1", "stroke", 0));
        expected.expectedSync.add(new ExpectedSync("bml1", "beat1", "stroke", 2));
        expected.expectedRef = new ExpectedSync("bml1", "speech1", "sync4", 0);
        assertEqualAfterConstraints(ImmutableList.of(expected), parser.getAfterConstraints());

        ExpectedAfterConstraint expectedReq = new ExpectedAfterConstraint();
        expectedReq.expectedSync.add(new ExpectedSync("bml1", "nod1", "stroke", 0));
        expectedReq.expectedRef = new ExpectedSync("bml1", "speech1", "sync4", 0);
        assertEqualAfterConstraints(ImmutableList.of(expectedReq), parser.getRequiredAfterConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void beforeConstraintTest() throws IOException
    {
        readXML("before.xml");
        
        ExpectedAfterConstraint expected1 = new ExpectedAfterConstraint();
        expected1.expectedSync.add(new ExpectedSync("bml1", "speech1", "sync4", 0));
        expected1.expectedRef = new ExpectedSync("bml1", "nod1", "stroke", 0);
        
        ExpectedAfterConstraint expected2 = new ExpectedAfterConstraint();
        expected2.expectedSync.add(new ExpectedSync("bml1", "speech1", "sync4", -2));
        expected2.expectedRef = new ExpectedSync("bml1", "beat1", "stroke", 0);
        
        assertEqualAfterConstraints(ImmutableList.of(expected1, expected2), parser.getAfterConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintSpeechRelTest() throws IOException
    {
        readXML("testspeechrel_offsets.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 4));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "nod2", "start", 2));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "speech2", "start", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "end", 5));
        expectedConstraints.add(expected2);

        ExpectedConstraint expected3 = new ExpectedConstraint();
        expected3.expectedSyncs.add(new ExpectedSync("bml1", "nod2", "end", 0));
        expected3.expectedSyncs.add(new ExpectedSync("bml1", "speech2", "end", 3));
        expectedConstraints.add(expected3);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testWaitDoubleSync() throws IOException
    {
        readXML("waitdoublesync.xml");
        List<ExpectedConstraint> expectedConstraints = new ArrayList<ExpectedConstraint>();

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "w1", "start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "w1", "end", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "end", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "speech2", "start", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintNodOffsetTest() throws IOException
    {
        readXML("testnod_offset.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 1));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "nod2", "start", -1));
        expectedConstraints.add(expected1);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintTimeShiftTest() throws IOException
    {
        readXML("testtimeshift.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "end", 0));
        expectedConstraints.add(expected1);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintNodInvalidTimeTest() throws IOException
    {
        readXML("testnod_invalidtime.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 2));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "end", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testPostureShift() throws IOException
    {
        readXML("testpostureshift.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "shift1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 3));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "shift1", "end", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }
    
    @Test(timeout = PARSE_TIMEOUT)
    public void testGazeShift() throws IOException
    {
        readXML("testgazeshift.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "shift1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 3));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "shift1", "end", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintNodAbsoluteTimeTest() throws IOException
    {
        readXML("testnod_absolute.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 2));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "end", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintNod2xAbsoluteTimeTest() throws IOException
    {
        readXML("testnod2x_absolute.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 1));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 3));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "nod2", "start", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintSpeechEndTimedTest() throws IOException
    {
        readXML("testspeech_endtimed.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 5));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "end", 0));
        expectedConstraints.add(expected1);
        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintNodTest() throws IOException
    {
        readXML("testnod.xml");
        List<Constraint> constraints = parser.getConstraints();
        assertEquals(0, constraints.size());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testSpeech2linked() throws IOException
    {
        readXML("testspeech_2linked.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "end", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech2", "start", 0));
        expectedConstraints.add(expected1);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintNodUnkownAttributeTest() throws IOException
    {
        readXML("testnod_unknownattributes.xml");
        List<Constraint> constraints = parser.getConstraints();
        assertEquals(0, constraints.size());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintOffsetTest() throws IOException
    {
        readXML("testoffset.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h1", "start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "g1", "end", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 3));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "g2", "start", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "g2", "end", -1));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "g1", "start", -1));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintOffset2Test() throws IOException
    {
        readXML("testoffset2.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h1", "start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "g1", "end", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 3));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "g2", "start", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "g2", "end", -2));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "g1", "start", -1));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintOffset3Test() throws IOException
    {
        readXML("testoffset3.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 3));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "g2", "start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "g2", "end", -2));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "g1", "start", -1));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "gaze1", "start", 2));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "h1", "start", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "g1", "end", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "gaze1", "end", -3));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintOffsetChainTest() throws IOException
    {
        readXML("testoffsetchain.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 4));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h1", "start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h2", "start", -2));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h3", "start", -4));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h4", "start", -6));
        expectedConstraints.add(expected1);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintOffsetChainReversedTest() throws IOException
    {
        readXML("testoffsetchainreversed.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 10));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h1", "start", 6));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h2", "start", 4));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h3", "start", 2));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "h4", "start", 0));
        expectedConstraints.add(expected1);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintSpeech3xTest() throws IOException
    {
        readXML("testspeech3x.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 4));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech10", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "speech11", "start", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "speech10", "end", 4));
        expectedConstraints.add(expected2);

        ExpectedConstraint expected3 = new ExpectedConstraint();
        expected3.expectedSyncs.add(new ExpectedSync("bml1", "speech12", "start", 0));
        expected3.expectedSyncs.add(new ExpectedSync("bml1", "speech11", "end", 4));
        expectedConstraints.add(expected3);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintEmptyTest() throws IOException
    {
        readXML("empty.xml");
        List<Constraint> constraints = parser.getConstraints();
        assertEquals(0, constraints.size());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void speechinvalidTimeSyncTest() throws IOException
    {
        readXML("testspeech_invalidtimesync.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 0.01));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "s1", 0));
        expectedConstraints.add(expected1);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintSpeechInvalidTimeSync2Test() throws IOException
    {
        readXML("testspeech_invalidtimesync2.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 10));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "s1", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintSpeechAndNodSyncTimedTest() throws IOException
    {
        readXML("testspeechandnod_synctimed.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 10));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "s1", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintSpeechSyncTimedTest() throws IOException
    {
        readXML("testspeech_synctimed.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 10));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "s1", 0));
        expectedConstraints.add(expected1);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintSpeechSyncTimedTest2x() throws IOException
    {
        readXML("testspeech_synctimed2x.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 10));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "s1", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech2", "s1", -10));
        expectedConstraints.add(expected1);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintSpeechSyncAtStartTest() throws IOException
    {
        readXML("testspeech_syncatstart.xml");
        assertEquals(0, parser.getConstraints().size());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintSpeechSyncAtStartAndToBeatTest() throws IOException
    {
        readXML("testspeech_syncatstartandtobeat.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "welkom", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "g1", "start", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "g1", "end", -2));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "welkom", "deicticheart1", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT, expected = XMLScanException.class)
    public void constraintSpeechUnknownBehaviorTest() throws IOException
    {
        readXML("testspeech_unknownbehavior.xml");
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintSpeechUnknownSyncTest() throws IOException
    {
        readXML("testspeech_unknownsync.xml");

        ExpectedConstraint expected = new ExpectedConstraint();
        expected.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 0));
        expected.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "unknown", 0));
        expectedConstraints.add(expected);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintAbsTest() throws IOException
    {
        readXML("testabs.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 1));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 1));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "g1", "start", 0));
        expectedConstraints.add(expected2);

        ExpectedConstraint expected3 = new ExpectedConstraint();
        expected3.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 1));
        expected3.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "start", 0));
        expectedConstraints.add(expected3);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintGazeReadyTimedTest() throws IOException
    {
        readXML("testgazereadytimed.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 3));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "gaze1", "ready", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 10));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "gaze1", "end", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void constraintGazeOffsetTest() throws IOException
    {
        readXML("testoffsetgaze.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "gaze1", "ready", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 2));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "gaze1", "start", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void beatreadytimed() throws IOException
    {
        readXML("testbeatreadytimed.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 1));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "beat1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 2));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "beat1", "ready", 0));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testnods() throws IOException
    {
        readXML("testnods.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 1));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "tilt1", "start", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "end", 1));
        expectedConstraints.add(expected2);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testSpeechNodTimedToSync() throws IOException
    {
        readXML("testspeech_nodtimedtosync.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 6));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "syncstart1", 0));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 0));
        expectedConstraints.add(expected2);

        ExpectedConstraint expected3 = new ExpectedConstraint();
        expected3.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 9));
        expected3.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "end", 0));
        expectedConstraints.add(expected3);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testSpeechNodTimedToSyncOffset() throws IOException
    {
        readXML("testspeech_nodtimedtosyncoffset.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 6));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "speech1", "syncstart1", 1));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 0));
        expectedConstraints.add(expected2);

        ExpectedConstraint expected3 = new ExpectedConstraint();
        expected3.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 9));
        expected3.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "end", 0));
        expectedConstraints.add(expected3);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testSpeechNodTimedToSyncCapitalization() throws IOException
    {
        readXML("testspeech_nodtimedtosync_capitalization.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("BMLWithCapitalizedStuff", null, "bml:start", 6));
        expected1.expectedSyncs.add(new ExpectedSync("BMLWithCapitalizedStuff", "speech1WithCapitalizedStuff", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("BMLWithCapitalizedStuff", "speech1WithCapitalizedStuff", "syncStart_1", 0));
        expected2.expectedSyncs.add(new ExpectedSync("BMLWithCapitalizedStuff", "nod1WithCapitalizedStuff", "start", 0));
        expectedConstraints.add(expected2);

        ExpectedConstraint expected3 = new ExpectedConstraint();
        expected3.expectedSyncs.add(new ExpectedSync("BMLWithCapitalizedStuff", null, "bml:start", 9));
        expected3.expectedSyncs.add(new ExpectedSync("BMLWithCapitalizedStuff", "nod1WithCapitalizedStuff", "end", 0));
        expectedConstraints.add(expected3);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testbeatandnod() throws IOException
    {
        readXML("testbeatandnod.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 3));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "beat1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 7));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "beat1", "end", 0));
        expectedConstraints.add(expected2);

        ExpectedConstraint expected3 = new ExpectedConstraint();
        expected3.expectedSyncs.add(new ExpectedSync("bml1", "beat1", "strokeEnd", 0));
        expected3.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 0));
        expectedConstraints.add(expected3);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testnodandbeat() throws IOException
    {
        readXML("testnodandbeat.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 3));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "start", 0));
        expectedConstraints.add(expected1);

        ExpectedConstraint expected2 = new ExpectedConstraint();
        expected2.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 5));
        expected2.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "end", 0));
        expectedConstraints.add(expected2);

        ExpectedConstraint expected3 = new ExpectedConstraint();
        expected3.expectedSyncs.add(new ExpectedSync("bml1", "nod1", "stroke", 0));
        expected3.expectedSyncs.add(new ExpectedSync("bml1", "beat1", "start", 0));
        expectedConstraints.add(expected3);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void testspeechgestures() throws IOException
    {
        readXML("testspeechgestures.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "g1", "start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "g1", "end", -2));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "welkom", "deicticheart1", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "transleft", "end", -1));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "transleft", "start", 1));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "relaxleft", "end", -4.8));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "relaxleft", "start", -2.5));
        expectedConstraints.add(expected1);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test(timeout = PARSE_TIMEOUT, expected = XMLScanException.class)
    public void testinvalidXML() throws IOException
    {
        readXML("testinvalidxml.xml");
    }

    @Test(timeout = PARSE_TIMEOUT, expected = XMLScanException.class)
    public void testinvalidXML2() throws IOException
    {
        readXML("testinvalidxml2.xml");
    }

    @Test(timeout = PARSE_TIMEOUT)
    public void headselftimed() throws IOException
    {
        readXML("testhead_selftimed.xml");

        ExpectedConstraint expected1 = new ExpectedConstraint();
        expected1.expectedSyncs.add(new ExpectedSync("bml1", null, "bml:start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "shake1", "start", 0));
        expected1.expectedSyncs.add(new ExpectedSync("bml1", "shake1", "end", -1));
        expectedConstraints.add(expected1);

        assertEqualConstraints(expectedConstraints, parser.getConstraints());
    }

    @Test
    public void testNoDirectLink()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" lexeme=\"BEAT\"/><gesture id=\"g2\" lexeme=\"BEAT\"/></bml>");
        assertFalse(parser.directLink("bml1", "g1", "bml1", "g2"));
    }

    @Test
    public void testDirectLink()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS()
                + "><gesture id=\"g1\" start=\"g2:start\" lexeme=\"BEAT\"/><gesture id=\"g2\" lexeme=\"BEAT\"/></bml>");
        assertTrue(parser.directLink("bml1", "g1", "bml1", "g2"));
    }

    @Test
    public void testNoDirectGround()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" lexeme=\"BEAT\"/></bml>");
        assertFalse(parser.directGround("bml1", "g1"));
    }

    @Test
    public void testDirectGround()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture stroke=\"2\" id=\"g1\" lexeme=\"BEAT\"/></bml>");
        assertTrue(parser.directGround("bml1", "g1"));
    }

    @Test
    public void testNoDirectAfterGround()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" lexeme=\"BEAT\"/></bml>");
        assertFalse(parser.directAfterGround("bml1", "g1"));
    }

    @Test
    public void testDirectAfterGround()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" lexeme=\"BEAT\"/>"
                + "<constraint><after ref=\"1\"><sync ref=\"g1:end\"/></after></constraint>" + "</bml>");
        assertTrue(parser.directAfterGround("bml1", "g1"));
    }

    @Test
    public void testIsNotConnected()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" lexeme=\"BEAT\"/><gesture id=\"g2\" lexeme=\"BEAT\"/></bml>");
        assertFalse(parser.isConnected("bml1", "g1", "bml1", "g2"));
    }

    @Test
    public void testIsConnectedDirect()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS()
                + "><gesture id=\"g1\" start=\"g2:start\" lexeme=\"BEAT\"/><gesture id=\"g2\" lexeme=\"BEAT\"/></bml>");
        assertTrue(parser.isConnected("bml1", "g1", "bml1", "g2"));
    }

    @Test
    public void testIsConnectedTwoSteps()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" start=\"g2:start\" lexeme=\"BEAT\"/>"
                + "<gesture id=\"g2\" lexeme=\"BEAT\"/><gesture id=\"g3\" start=\"g2:stroke\" lexeme=\"BEAT\"/></bml>");
        assertTrue(parser.isConnected("bml1", "g1", "bml1", "g3"));
    }

    @Test
    public void testIsConnectedThreeSteps()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" start=\"g2:start\" lexeme=\"BEAT\"/>"
                + "<gesture id=\"g2\" lexeme=\"BEAT\"/><gesture id=\"g3\" start=\"g2:stroke\" lexeme=\"BEAT\"/>"
                + "<gesture id=\"g4\" ready=\"g3:strokeStart\" lexeme=\"BEAT\"/></bml>");
        assertTrue(parser.isConnected("bml1", "g1", "bml1", "g4"));
    }

    @Test
    public void testIsNotGrounded()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" lexeme=\"BEAT\"/></bml>");
        assertFalse(parser.isGrounded("bml1", "g1"));
    }

    @Test
    public void testIsGroundedDirect()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" stroke=\"1\" lexeme=\"BEAT\"/></bml>");
        assertTrue(parser.isGrounded("bml1", "g1"));
    }

    @Test
    public void testIsGroundedTwoSteps()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" start=\"g2:start\" lexeme=\"BEAT\"/>"
                + "<gesture id=\"g2\" strokeStart=\"3\" lexeme=\"BEAT\"/></bml>");
        assertTrue(parser.isGrounded("bml1", "g1"));
    }

    @Test
    public void testIsGroundedFourSteps()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" start=\"g2:start\" lexeme=\"BEAT\"/>"
                + "<gesture id=\"g2\" lexeme=\"BEAT\"/><gesture id=\"g3\" start=\"g2:stroke\" lexeme=\"BEAT\"/>"
                + "<gesture id=\"g4\" ready=\"g3:strokeStart\" start=\"0\" lexeme=\"BEAT\"/></bml>");
        assertTrue(parser.isGrounded("bml1", "g1"));
    }

    @Test
    public void testNoDependencies()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "/>");
        assertTrue(parser.getDependencies("bml1").isEmpty());
    }

    @Test
    public void testLinkDependency()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" stroke=\"bml2:beh1:start\" lexeme=\"BEAT\"/></bml>");
        assertThat(parser.getDependencies("bml1"), IsIterableContainingInAnyOrder.containsInAnyOrder("bml2"));
    }

    @Test
    public void testAfterDependency()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture id=\"g1\" lexeme=\"BEAT\"/>"
                + "<constraint><after ref=\"bml2:g1:start\"><sync ref=\"g1:end\"/><sync ref=\"bml3:g1:end\"/></after></constraint>"
                + "</bml>");
        assertThat(parser.getDependencies("bml1"), IsIterableContainingInAnyOrder.containsInAnyOrder("bml2", "bml3"));
    }

    private class BMLXBMLBehaviorAttributes implements BMLBehaviorAttributeExtension
    {
        @Override
        public void decodeAttributes(BehaviourBlock behavior, HashMap<String, String> attrMap, XMLTokenizer tokenizer)
        {
            
        }

        @Override
        public BMLBlockComposition handleComposition(String sm)
        {
            return CoreComposition.UNKNOWN;
        }

        @Override
        public Set<String> getOtherBlockDependencies()
        {
            return ImmutableSet.of("bmlx");
        }

        public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
        {
            return buf;
        }
    }

    private class BMLYBMLBehaviorAttributes implements BMLBehaviorAttributeExtension
    {
        @Override
        public void decodeAttributes(BehaviourBlock behavior, HashMap<String, String> attrMap, XMLTokenizer tokenizer)
        {
            
        }

        @Override
        public BMLBlockComposition handleComposition(String sm)
        {
            return CoreComposition.UNKNOWN;
        }

        @Override
        public Set<String> getOtherBlockDependencies()
        {
            return ImmutableSet.of("bmly");
        }

        public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
        {
            return buf;
        }
    }

    @Test
    public void testBlockDependency()
    {
        BehaviourBlock block = new BehaviourBlock(new BMLXBMLBehaviorAttributes(), new BMLYBMLBehaviorAttributes());
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "/>", block);
        assertThat(parser.getDependencies("bml1"), IsIterableContainingInAnyOrder.containsInAnyOrder("bmlx", "bmly"));
    }

    @Test
    public void testBehaviorDependency()
    {
        BMLInfo.addBehaviourType(StubBehaviour.XMLTAG, StubBehaviour.class);
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><stub id=\"s1\"/></bml>");
        assertThat(parser.getDependencies("bml1"), IsIterableContainingInAnyOrder.containsInAnyOrder("bmlx"));
    }
    
    @Test(timeout = PARSE_TIMEOUT, expected=XMLScanException.class)
    public void testIgnoreUnknownBehaviourStrict()
    {
        read("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><unknown xmlns=\"unknown\" id=\"s1\"/></bml>");
    }
    
    @Test(timeout = PARSE_TIMEOUT)
    public void testIgnoreUnknownBehaviourNotStrict()
    {
        readNotStrict("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><unknown xmlns=\"unknown\" id=\"s1\"/></bml>");
    }
    
    @Test(timeout = PARSE_TIMEOUT)
    public void testIgnoreBmlBehaviorWithAlternativeNamespaceNotStrict()
    {
        readNotStrict("<bml id=\"bml1\" " + TestUtil.getDefNS() + "><gesture xmlns=\"unknown\" id=\"s1\"/></bml>");
    }
}
