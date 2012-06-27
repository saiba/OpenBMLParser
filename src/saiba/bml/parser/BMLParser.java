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


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import saiba.bml.core.BMLBehaviorAttributeExtension;
import saiba.bml.core.BMLElement;
import saiba.bml.core.Behaviour;
import saiba.bml.core.BehaviourBlock;
import saiba.bml.core.Sync;
import saiba.bml.core.Synchronize;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * This is the BML parser. It can be fed with objects of the type BehaviourBlock and will resolve the Behaviours
 * and Constraints of the block.
 * @author PaulRC
 */
public class BMLParser
{
    private ArrayList<BehaviourBlock> bbs;

    private HashMap<String, BMLElement> BMLElementsById;

    private ArrayList<Constraint> constraints;

    private static Logger logger = LoggerFactory.getLogger(BMLParser.class.getName());

    private final ImmutableSet<Class<? extends BMLBehaviorAttributeExtension>> behaviorAttributeExtensions;

    public BMLParser(ImmutableSet<Class<? extends BMLBehaviorAttributeExtension>> behaviorAttributeExtensions)
    {
        this.behaviorAttributeExtensions = behaviorAttributeExtensions;
        bbs = new ArrayList<BehaviourBlock>();
        BMLElementsById = new HashMap<String, BMLElement>();
        constraints = new ArrayList<Constraint>();
    }

    public BMLParser()
    {
        this(new ImmutableSet.Builder<Class<? extends BMLBehaviorAttributeExtension>>().build());
    }

    /**
     * Create a new behaviour block with the registered BMLBehaviorAttributeExtensions
     */
    public BehaviourBlock createBehaviourBlock() throws InstantiationException, IllegalAccessException
    {
        List<BMLBehaviorAttributeExtension> attrList = new ArrayList<BMLBehaviorAttributeExtension>();
        for (Class<? extends BMLBehaviorAttributeExtension> bbClass : behaviorAttributeExtensions)
        {
            attrList.add(bbClass.newInstance());
        }
        return new BehaviourBlock(attrList.toArray(new BMLBehaviorAttributeExtension[attrList.size()]));
    }

    /** clear all behaviors from the scheduler */
    public void clear()
    {
        bbs.clear();
        BMLElementsById.clear();
        constraints.clear();
    }

    private BehaviourBlock getBehaviourBlock(String bmlId)
    {
        for (BehaviourBlock bb : getBehaviourBlocks())
        {
            if (bb.getBmlId().equals(bmlId)) return bb;
        }
        return null;
    }

    public Behaviour getBehaviour(String bmlId, String behId)
    {
        BehaviourBlock bb = getBehaviourBlock(bmlId);
        if (bb == null) return null;
        for (Behaviour b : bb.behaviours)
        {
            if (b.id != null)
            {
                if (b.id.equals(behId)) return b;
            }
        }
        return null;
    }

    private List<Constraint> getSoftConstraints(String bmlId, String behId)
    {
        List<Constraint> constr = new ArrayList<Constraint>();
        for (Constraint c : getConstraints(bmlId, behId))
        {
            if (!c.isHard())
            {
                constr.add(c);
            }
        }
        return constr;
    }

    public List<Constraint> getConstraints(String bmlId, String behId)
    {
        List<Constraint> constr = new ArrayList<Constraint>();
        for (Constraint c : getConstraints())
        {
            if (c.containsBehaviour(bmlId, behId))
            {
                constr.add(c);
            }
        }
        return constr;
    }

    private List<Behaviour> getBehaviours(Constraint c)
    {
        List<Behaviour> behs = new ArrayList<Behaviour>();
        for (SyncPoint s : c.getTargets())
        {
            if (s.getBehaviourId() != null)
            {
                behs.add(getBehaviour(s.getBmlId(), s.getBehaviourId()));
            }
        }
        return behs;
    }

    public List<List<Behaviour>> getUngroundedLoops(String bmlId, String behId)
    {
        List<List<Behaviour>> list = new ArrayList<List<Behaviour>>();
        Behaviour currentBeh = getBehaviour(bmlId, behId);
        List<Constraint> constraints = getSoftConstraints(bmlId, behId);
        for (Constraint c : constraints)
        {
            List<Behaviour> behs = getBehaviours(c);
            behs.remove(currentBeh);
            for (Behaviour beh : behs)
            {
                if (!directGround(beh.getBmlId(), beh.id))
                {
                    ArrayList<Constraint> checkedC = new ArrayList<Constraint>();
                    checkedC.add(c);
                    getUngroundedLoops(currentBeh, beh, new ArrayList<Behaviour>(), checkedC, list);
                }
            }
        }
        List<List<Behaviour>> listNoDuplicates = new ArrayList<List<Behaviour>>();
        {
            for (List<Behaviour> behList : list)
            {
                if (!listNoDuplicates.contains(behList))
                {
                    Collections.reverse(behList);
                    listNoDuplicates.add(behList);
                }
            }
        }
        return listNoDuplicates;
    }

    private void getUngroundedLoops(Behaviour loopBeh, Behaviour currentBeh, List<Behaviour> checkedBehaviours,
            List<Constraint> checkedConstraints, List<List<Behaviour>> pathList)
    {
        List<Constraint> constraints = getSoftConstraints(currentBeh.getBmlId(), currentBeh.id);
        ArrayList<Behaviour> checkedBeh = new ArrayList<Behaviour>(checkedBehaviours);
        checkedBeh.add(currentBeh);
        constraints.removeAll(checkedConstraints);
        for (Constraint c : constraints)
        {
            List<Behaviour> behs = getBehaviours(c);
            behs.removeAll(checkedBeh);
            for (Behaviour beh : behs)
            {
                if (beh == loopBeh)
                {
                    pathList.add(checkedBeh);
                }
                else if (!directGround(beh.getBmlId(), beh.id))
                {
                    ArrayList<Constraint> checkedC = new ArrayList<Constraint>(checkedConstraints);
                    checkedC.add(c);
                    getUngroundedLoops(loopBeh, beh, checkedBeh, checkedC, pathList);
                }
            }
        }
    }

    /**
     * Behaviour b1 and b2 are connected with an at constraints
     */
    public boolean directLink(String bmlId1, String behId1, String bmlId2, String behId2)
    {
        for (Constraint c : getConstraints())
        {
            boolean containsB1 = false;
            boolean containsB2 = false;
            for (SyncPoint s : c.getTargets())
            {
                if (s.getBehaviourId() == null) continue;
                if (s.getBehaviourId().equals(behId1) && s.getBmlId().equals(bmlId1)) containsB1 = true;
                if (s.getBehaviourId().equals(behId2) && s.getBmlId().equals(bmlId2)) containsB2 = true;
            }
            if (containsB1 && containsB2) return true;
        }
        return false;
    }

    public boolean directGround(String bmlId1, String behId1)
    {
        for (Constraint c : getConstraints())
        {
            boolean isGrounded = false;
            boolean containsB1 = false;
            for (SyncPoint s : c.getTargets())
            {
                if (s.getBehaviourId() == null) isGrounded = true;
                else if (s.getBehaviourId().equals(behId1) && s.getBmlId().equals(bmlId1)) containsB1 = true;
            }
            if (containsB1 && isGrounded) return true;
        }
        return false;
    }

    /**
     * Adds a behavior block.
     * 
     * @param bb
     */
    public void addBehaviourBlock(BehaviourBlock bb)
    {
        bb.registerElementsById(this);
        constructConstraints(bb);
        bbs.add(bb);

        logger.debug("List of constraints: ");
        for (Constraint constraint : constraints)
        {
            logger.debug("\t {}", constraint);
        }
    }

    /**
     * This method constructs constraints synchronization points. The source SyncPoint are directly
     * known to each Behavior, while the target SyncPoint are constructed from the literal
     * references in them.
     * 
     * @param bb
     */
    private void constructConstraints(BehaviourBlock bb)
    {
        bb.constructConstraints(this);

        ArrayList<Constraint> cNewList = new ArrayList<Constraint>();

        double relOffset = 0;

        // merge constraints that share the same behavior sync with different offsets
        for (Constraint cOld : constraints)
        {
            boolean merged = false;
            Constraint cMerge = null;
            SyncPoint cSyncOld = null;

            // can be merged?
            for (SyncPoint syncOld : cOld.getTargets())
            {
                for (Constraint cNew : cNewList)
                {
                    for (SyncPoint syncNew : cNew.getTargets())
                    {
                        if (syncOld.equalsPoint(syncNew))
                        {
                            // merge cOld into cNew
                            relOffset = syncNew.getOffset() - syncOld.getOffset();
                            merged = true;
                            cMerge = cNew;
                            cSyncOld = syncOld;
                            break;
                        }
                    }
                    if (merged) break;
                }
            }

            if (merged)
            {
                for (SyncPoint syncOld : cOld.getTargets())
                {
                    if (syncOld != cSyncOld)
                    {
                        SyncPoint sNew = new SyncPoint(syncOld);
                        sNew.offset += relOffset;
                        cMerge.addTarget(sNew);
                        sNew.setConstraint(cMerge);
                    }
                }
            }
            if (!merged)
            {
                cNewList.add(cOld);
            }
        }
        constraints = cNewList;
    }

    public void constructConstraints(Behaviour behaviour)
    {
        // For each of the SyncPoints with a reference, construct the foreign SyncPoint
        ImmutableList<SyncPoint> syncPoints = ImmutableList.copyOf(behaviour.getSyncPoints());
        for (SyncPoint syncPoint : syncPoints)
        {
            String ref = syncPoint.getRefString();
            if (ref == null || ref.isEmpty()) continue; // Empty SyncPoint reference. No constraint needed.
            SyncPoint foreignSyncPoint;
            try
            {
                foreignSyncPoint = syncPoint.getForeignSyncPoint(this);
                constructConstraint(syncPoint, foreignSyncPoint);
            }
            catch (MissingSyncPointException e)
            {
                System.err.println(e.getMessage());
            }
        }
    }

    public void constructConstraints(Synchronize synchronize)
    {
        SyncPoint synchronizeRef = new SyncPoint(synchronize.bmlId, synchronize.getRef());
        SyncPoint left = synchronizeRef.getForeignSyncPoint(this);
        ArrayList<Sync> syncs = synchronize.getSyncs();
        for (Sync sync : syncs)
        {
            SyncPoint syncRef = new SyncPoint(synchronize.bmlId, sync.ref);
            SyncPoint right = syncRef.getForeignSyncPoint(this);
            constructConstraint(left, right);
        }
    }

    public void constructConstraint(SyncPoint left, SyncPoint right)
    {
        Constraint constraint;

        // The SyncPoints might already be present in (a) existing constraint(s).
        if (right.inConstraint() && left.inConstraint())
        {
            // Both are in a constraint. Merge them.
            Constraint masterConstraint = left.getConstraint();
            Constraint slaveConstraint = right.getConstraint();
            ArrayList<SyncPoint> targets = slaveConstraint.getTargets();
            for (SyncPoint target : targets)
            {
                target.setConstraint(masterConstraint);
                masterConstraint.addTarget(target);
            }
            constraints.remove(slaveConstraint);
        }
        else if (right.inConstraint())
        {
            constraint = right.getConstraint();
            constraint.addTarget(left);
            left.setConstraint(constraint);
        }
        else if (left.inConstraint())
        {
            constraint = left.getConstraint();
            constraint.addTarget(right);
            right.setConstraint(constraint);
        }
        else
        {
            constraint = new Constraint(left, right);
            left.setConstraint(constraint);
            right.setConstraint(constraint);
            constraints.add(constraint);
        }
    }

    public void registerBMLElement(BMLElement element)
    {
        if (element.id == null) return;

        String fullId = element.getBmlId() + ":" + element.id;

        logger.debug("Registering {} ({}) ", element.getClass().toString(), fullId);

        if (BMLElementsById.containsKey(fullId))
        {
            logger.warn("fullId {}  not unique", fullId);
        }
        else
        {
            BMLElementsById.put(fullId, element);
        }
    }

    public BMLElement getBMLElementById(String id)
    {
        return BMLElementsById.get(id);
    }

    /**
     * @return the constraints
     */
    public List<Constraint> getConstraints()
    {
        return constraints;
    }

    /**
     * Herwin: a bit of a hack, gets the behaviors in order of XML processing, to be used for
     * SmartBody reference scheduler
     */
    public List<Behaviour> getBehaviours()
    {
        ArrayList<Behaviour> behs = new ArrayList<Behaviour>();
        for (BehaviourBlock bb : bbs)
        {
            behs.addAll(bb.behaviours);
        }
        return behs;
    }

    /**
     * @return the bbs
     */
    public List<BehaviourBlock> getBehaviourBlocks()
    {
        return bbs;
    }

}
