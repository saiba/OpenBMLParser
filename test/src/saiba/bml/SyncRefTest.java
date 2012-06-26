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
package saiba.bml;

import static org.junit.Assert.*;

import org.junit.Test;

import saiba.bml.parser.InvalidSyncRefException;
import saiba.bml.parser.SyncRef;

import static org.hamcrest.CoreMatchers.*;

/**
 * Unit test cases for SyncRef
 * @author Herwin
 * 
 */
public class SyncRefTest
{
    private static final double SYNC_PRECISION = 0.00001;

    @Test
    public void testSyncRef1() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bml1", "0.1");
        assertEquals(0.1, s.offset, SYNC_PRECISION);
        assertEquals("bml", s.sourceId);
        assertEquals("start", s.syncId);
    }

    @Test
    public void testSyncRef2() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bml1", "beh1:tm");
        assertEquals(0, s.offset, SYNC_PRECISION);
        assertEquals("beh1", s.sourceId);
        assertEquals("tm", s.syncId);
    }

    @Test
    public void testSyncRef3() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bml1", "beh1:relax+2");
        assertEquals(2, s.offset, SYNC_PRECISION);
        assertEquals("beh1", s.sourceId);
        assertEquals("relax", s.syncId);
    }

    @Test(expected = InvalidSyncRefException.class)
    public void testSyncRef3b() throws InvalidSyncRefException
    {
        new SyncRef("bml1", "beh1:relax:2");
    }

    @Test
    public void testSyncRef4() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bml1", "beh2:stroke:2");
        assertEquals(0, s.offset, SYNC_PRECISION);
        assertEquals("beh2",s.sourceId);
        assertEquals("stroke", s.syncId);
        assertEquals(2, s.iteration);
    }

    @Test
    public void testSyncRef5() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bml1", "beh2:stroke:2-3.1");
        assertEquals("beh2", s.sourceId);
        assertEquals("stroke", s.syncId);
        assertEquals(2, s.iteration);
        assertEquals(-3.1, s.offset, SYNC_PRECISION);
    }

    @Test
    public void testSyncRef6() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bml1", "beh2  : stroke : 2 - 3.1 ");
        assertEquals("beh2", s.sourceId);
        assertEquals("stroke", s.syncId);
        assertEquals(2, s.iteration);
        assertEquals(-3.1, s.offset, SYNC_PRECISION);
    }

    @Test
    public void testSyncRef7() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bml1", "beh2:start-2");
        assertEquals("beh2", s.sourceId);
        assertEquals("start", s.syncId);
        assertEquals(-1, s.iteration);
        assertEquals(-2f, s.offset, SYNC_PRECISION);
    }

    @Test
    public void testSyncRef8() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bml1", "beat1:strokeStart");
        assertEquals("beat1", s.sourceId);
        assertEquals("strokeStart", s.syncId);
        assertEquals(-1, s.iteration);
        assertEquals(0f, s.offset, SYNC_PRECISION);
    }

    @Test
    public void testSyncRef9() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bml1", "beat5:beat3");
        assertTrue(s.sourceId.equals("beat5"));
        assertTrue(s.syncId.equals("beat3"));
        assertTrue(s.iteration == -1f);
        assertTrue(s.offset == 0f);
    }

    @Test(expected = InvalidSyncRefException.class)
    public void testSyncRef10() throws InvalidSyncRefException
    {
        new SyncRef("bml1", "beat1:custom-sync1");
    }

    @Test(expected = InvalidSyncRefException.class)
    public void testSyncRef11() throws InvalidSyncRefException
    {
        new SyncRef("bml1", "beh2:end+x");
    }

    @Test
    public void testSyncRef12() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bml1", "beh2:end+");
        assertTrue(s.sourceId.equals("beh2"));
        assertTrue(s.syncId.equals("end+"));
        assertTrue(s.iteration == -1f);
        assertTrue(s.offset == 0f);
    }

    @Test
    public void testSyncRefWithBML() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bmlDef", "bml1:beh1:stroke");
        assertEquals("bml1", s.bbId);
        assertEquals("beh1", s.sourceId);
        assertEquals("stroke", s.syncId);
        assertEquals(-1f, s.iteration, SYNC_PRECISION);
        assertEquals(0, s.offset, SYNC_PRECISION);
    }

    @Test
    public void testSyncRefWithBMLAndStrokeAndOffset() throws InvalidSyncRefException
    {
        SyncRef s = new SyncRef("bmlDef", "bml1:beh1:stroke:1-3");
        assertEquals("bml1", s.bbId);
        assertEquals("beh1", s.sourceId);
        assertEquals("stroke", s.syncId);
        assertEquals(1f, s.iteration, SYNC_PRECISION);
        assertEquals(-3, s.offset, SYNC_PRECISION);
    }

    @Test(expected = InvalidSyncRefException.class)
    public void testSyncRefWithBMLAndInvalidStrokeIteration() throws InvalidSyncRefException
    {
        new SyncRef("bmlDef", "bml1:beh1:stroke:-1");
    }

    @Test(expected = InvalidSyncRefException.class)
    public void testSyncRefWithBMLAndInvalidStrokeIteration1() throws InvalidSyncRefException
    {
        new SyncRef("bmlDef", "bml1:beh1:stroke:blah");
    }

    @Test(expected = InvalidSyncRefException.class)
    public void testSyncRefWithInvalidStart() throws InvalidSyncRefException
    {
        new SyncRef("bmlDef", ":beh1:sync1");
    }

    @Test(expected = InvalidSyncRefException.class)
    public void testSyncRefWithBMLInvalid() throws InvalidSyncRefException
    {
        new SyncRef("bmlDef", "bml1:blah:beh1:stroke:2");
    }

    @Test(expected = InvalidSyncRefException.class)
    public void testIncomplete1() throws InvalidSyncRefException
    {
        new SyncRef("bmlDef", "beh1");
    }

    @Test
    public void testEquals() throws InvalidSyncRefException
    {
        SyncRef s1 = new SyncRef("bml1", "0.1");
        SyncRef s2 = new SyncRef("bml1", "0.1");
        assertEquals(s1, s2);
    }

    @Test
    public void testEquals1() throws InvalidSyncRefException
    {
        SyncRef s1 = new SyncRef("bml1", "bml:start+0.1");
        SyncRef s2 = new SyncRef("bml1", "0.1");
        assertEquals(s1,s2);
    }
    
    @Test
    public void testEquals2() throws InvalidSyncRefException
    {
        SyncRef s1 = new SyncRef("bml1", "beh2:stroke:2-3.1");
        SyncRef s2 = new SyncRef("bml1", "beh2:stroke:2-3.1");
        assertEquals(s1,s2);
    }

    @Test
    public void testEquals3() throws InvalidSyncRefException
    {
        SyncRef s1 = new SyncRef("bml1", "beh2:relax");
        SyncRef s2 = new SyncRef("bml1", "beh2:relax");
        assertEquals(s1, s2);
    }
    
    @Test
    public void testNotEquals1() throws InvalidSyncRefException
    {
        SyncRef s1 = new SyncRef("bml1", "beh2:stroke:2-3.1");
        SyncRef s2 = new SyncRef("bml1", "beh2:stroke:2-3.0");
        assertThat(s1, not(equalTo(s2)));
    }

    @Test
    public void testNotEquals2() throws InvalidSyncRefException
    {
        SyncRef s1 = new SyncRef("bml1", "bml:start+0.1");
        SyncRef s2 = new SyncRef("bml1", "bml:start");
        assertThat(s1, not(equalTo(s2)));
    }

    @Test
    public void testNotEquals3() throws InvalidSyncRefException
    {
        SyncRef s1 = new SyncRef("bml1", "beh2:stroke:2-3.1");
        SyncRef s2 = new SyncRef("bml1", "beh2:stroke:1-3.1");
        assertThat(s1, not(equalTo(s2)));
    }

    @Test
    public void testNotEquals4() throws InvalidSyncRefException
    {
        SyncRef s1 = new SyncRef("bml1", "beh2:stroke");
        SyncRef s2 = new SyncRef("bml1", "beh2:relax");
        assertThat(s1, not(equalTo(s2)));
    }

   

    @Test
    public void testGetRefString1() throws InvalidSyncRefException
    {
        SyncRef s1 = new SyncRef("bml1", "0.1");
        assertEquals("bml1:bml:start+0.1", s1.toString());
    }

    @Test
    public void testGetRefString2() throws InvalidSyncRefException
    {
        SyncRef s1 = new SyncRef("bml1", "beh2:stroke:2-3.1");
        assertEquals(s1.toString(), "bml1:beh2:stroke:2-3.1");
    }

    @Test
    public void testToRelativeString() throws InvalidSyncRefException
    {
        SyncRef s1 = new SyncRef("bml1", "2");
        assertEquals("2.0", s1.toString("bml1"));
    }

}
