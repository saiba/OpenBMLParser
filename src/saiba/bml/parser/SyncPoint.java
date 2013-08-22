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

import saiba.bml.BMLInfo;
import saiba.bml.core.Behaviour;
import hmi.xml.XMLScanException;

/**
 * <p>
 * Represents a synchronization point that belongs to a behavior. A synchronization point is nothing
 * more than a point in time (time may be unknown). It may be referred to within a constraint
 * (scheduler-wise, has not much to do with BML's &lt;constraint&gt;) or specify an additional
 * offset relative to the given point.
 * </p>
 * <p>
 * Represents e.g. a start="bmlx:behx:syncx" pair, where bmlId:behaviourId:name:offset is the left side
 * and syncRef is the right side.
 * </p>
 * @author PaulRC
 */
public class SyncPoint
{
    private final String name; // start, strokeStart, etc.
    private String behaviourId = null;
    private String bmlId;
    
    private int iteration = -1; // To be used with sync points that can be repeated.

    public double offset; // Offset.

    private SyncRef ref; // Filled with a BML-reference to a foreign sync point,
                         // including offset. Used for validation and recreation of
                         // sync-point attributes and construction of constraints.

    public void setBehaviourId(String behaviourId)
    {
        this.behaviourId = behaviourId;
    }

    public void setBMLId(String bmlId)
    {
        this.bmlId = bmlId;        
    }
    
    public String getBehaviourId()
    {
        return behaviourId;
    }

    public String getBmlId()
    {
        return bmlId;
    }

    private Constraint constraint;      // The (AT) constraint the SyncPoint is in.
    private AfterConstraint afterConstraint;   //set if the constraint is an after reference in constraint afterRef
    
    public SyncPoint(String bmlId,  String behaviorId, String name)
    {
        this.bmlId = bmlId;
        this.behaviourId = behaviorId;
        this.name = name;
    }

    public SyncPoint(SyncPoint s)
    {
        name = s.name;
        bmlId = s.bmlId;
        behaviourId = s.behaviourId;
        iteration = s.iteration;
        offset = s.offset;
        ref = s.ref;
        constraint = s.constraint;
    }

    public SyncPoint(String bmlId, String behaviorId, String name, int iteration)
    {
        this(bmlId,behaviorId, name);
        this.iteration = iteration;
    }

    /**
     * Check if this point is the same as p, not taking offset, ref and constraint into account
     */
    public boolean equalsPoint(SyncPoint p)
    {
        if (p.behaviourId == null && behaviourId == null)
        {
            // absolute sync or anticipator
            String str[] = p.name.split(":");
            if (str.length != 2 || str[0].equals("bml"))
            {
                // absolute sync, never equal
                return false;
            }
        }
        else if (p.behaviourId == null) return false;
        else if (behaviourId==null)return false;
        else if (!p.behaviourId.equals(behaviourId))
        {
            return false;
        }
        return p.name.equals(name) && p.iteration == iteration;
    }

    // To be used with <synchronize> when the origin of the SyncPoint is not important.
    public SyncPoint(String bmlId, SyncRef ref)
    {
        this.bmlId = bmlId;
        this.behaviourId = null;
        this.name = null;
        this.ref = ref;
    }

    public int getIteration()
    {
        return iteration;
    }

    public String getName()
    {
        return name;
    }

    public void setRefString(String ref) throws InvalidSyncRefException
    {
        this.ref = new SyncRef(bmlId, ref);
    }

    public SyncRef getRef()
    {
        return ref;
    }

    public String getRefString()
    {
        if (ref != null) return ref.toString(bmlId);
        else return "";
    }

    @Override
    public String toString()
    {
        StringBuffer retval = new StringBuffer();        
        retval.append(bmlId + ":" + behaviourId);
        retval.append(":" + name);
        if (iteration != -1) retval.append(":" + iteration);
        if (offset != 0.0f)
        {
            retval.append(offset > 0 ? '+' : "");
            retval.append(offset);
        }
        return retval.toString();
    }

    public void setConstraint(Constraint constraint)
    {
        this.constraint = constraint;
    }
    
    public void setAsRefForAfterConstraint(AfterConstraint constraint)
    {
        this.afterConstraint = constraint;
    }

    public boolean isRefInAfterConstraint()
    {
        return this.afterConstraint!=null;
    }
    
    public boolean inConstraint()
    {
        return this.constraint != null;
    }
    
    /**
     * Get the after constraint this constraint is a reference for.
     */
    public AfterConstraint getAfterConstraint()
    {
        return afterConstraint;
    }

    public Constraint getConstraint()
    {
        return this.constraint;
    }
    
    /**
     * Find the syncpoint corresponding with the syncref
     * @throws MissingSyncPointException
     */
    public SyncPoint getForeignSyncPoint(BMLParser parser)
    {
        // Check whether ref.sourceId (prepended with bbId) is present.
        Behaviour behaviour = null;
        if (!ref.sourceId.equals("bml"))
        {
            behaviour = (Behaviour) parser.getBMLElementById(ref.bbId + ':' + ref.sourceId);
        }

        if (behaviour == null)
        {
            // modified by Herwin: sync is an offset in bml block or an anticipator, return new Sync with null behaviour
            SyncPoint s;
            if (ref.syncId.equals(""))
            {
                s = new SyncPoint(ref.bbId, null, "start");
                s.offset = ref.offset;
            }
            else
            {
                if(BMLInfo.isExternalBlockId(ref.bbId)||ref.sourceId.equals("bml"))
                {
                    s = new SyncPoint(ref.bbId, null, ref.sourceId + ":" + ref.syncId);
                }
                else if(ref.bbId.equals(bmlId))
                {
                    throw new XMLScanException("Reference "+ref+" to behaviour "+ref.sourceId+" in the same block not found.");
                }
                else
                {
                    s = new SyncPoint(ref.bbId, ref.sourceId, ref.syncId);
                }
                s.offset = ref.offset;
            }
            return s;
        }

        // Find the SyncPoint for ref.syncId
        for (SyncPoint syncPoint : behaviour.getSyncPoints())
        {
            if (syncPoint.name.equals(ref.syncId)
                    && (syncPoint.iteration == ref.iteration || ref.iteration == -1)
                    && syncPoint.offset == ref.offset)
            {
                // We have a match. Return the current SyncPoint.
                return syncPoint;
            }
        }

        //sync doesn't exist yet, add it to the behavior
        SyncPoint s = new SyncPoint(behaviour.getBmlId(),behaviour.id, ref.syncId);
        s.offset = ref.offset;
        behaviour.addSyncPoint(s);

        return s;
    }

    /**
     * @return the offset
     */
    public double getOffset()
    {
        return offset;
    }
}
