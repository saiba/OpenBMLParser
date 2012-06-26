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

/**
 * BML constraint
 * @author paulrc
 */
public class Constraint
{
    // private SyncPoint source; // Only applicable when type != Type.AT.
    private ArrayList<SyncPoint> targets;

    private enum Type
    {
        AT, BEFORE, AFTER
    }

    private Type type;

    public Constraint()
    {
        targets = new ArrayList<SyncPoint>();
        type = Type.AT;
    }

    public Constraint(SyncPoint target1, SyncPoint target2)
    {
        this();
        targets.add(target1);
        targets.add(target2);
    }

    public void addTarget(SyncPoint target)
    {
        if (!targets.contains(target)) targets.add(target);
    }

    public ArrayList<SyncPoint> getTargets()
    {
        return targets;
    }

    public boolean containsBehaviour(String bmlId, String behId)
    {
        for (SyncPoint s : getTargets())
        {
            if(s.getBehaviourId()!=null)
            {
                if(s.getBmlId().equals(bmlId)&&s.getBehaviourId().equals(behId))return true;
            }
        }
        return false;
    }
    
    public boolean isHard()
    {
        for (SyncPoint s : getTargets())
        {
            if(s.getBehaviourId()==null)return true;
        }
        return false;
    }
    
    @Override
    public String toString()
    {
        StringBuffer retval = new StringBuffer();
        retval.append("Type: " + type + ", targets: ");
        for (SyncPoint target : targets)
        {
            retval.append(target.toString() + ", ");
        }
        retval.delete(retval.length() - 2, retval.length());

        return retval.toString();
    }
}
