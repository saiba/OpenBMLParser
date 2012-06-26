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

import hmi.util.StringUtil;

import com.google.common.base.Objects;

/**
 * SyncRef String parser
 * 
 * @author welberge
 */
public class SyncRef
{
    public String bbId; // ID of the behavior block

    public String sourceId; // ID of the behaviour

    public String syncId; // Name of the sync within the behaviour (start, stroke, end, ...)

    public int iteration = -1; // Optional iteration

    public double offset = 0; // Offset in seconds from the specified sync

    /**
     * Gets the offset from a syncRef String
     * 
     * @return
     * @throws InvalidSyncRefException
     */
    private String parseOffset(String syncRef) throws InvalidSyncRefException
    {
        String offsetStr = "";
        int plusMin = 0;

        // check for offset only
        if (StringUtil.isNumeric(syncRef.trim()))
        {
            offsetStr = syncRef.trim();
            offset = Double.parseDouble(offsetStr);
        }

        if (offsetStr.length() == 0)
        {
            // check for -offset
            String split[] = syncRef.split("-");
            if (split.length > 1)
            {
                offsetStr = split[split.length - 1];
                if (StringUtil.isNumeric(offsetStr.trim()))
                {
                    offset = -Double.parseDouble(offsetStr.trim());
                    plusMin = 1;
                }
                else
                {
                    throw new InvalidSyncRefException("Invalid SyncRef: " + syncRef
                            + ", syncref contains - without offset");
                }
            }
        }
        // no -offset, check for +offset
        if (offsetStr.length() == 0)
        {
            String split[] = syncRef.split("\\+");
            if (split.length > 1)
            {
                offsetStr = split[split.length - 1];
                if (StringUtil.isNumeric(offsetStr.trim()))
                {
                    offset = Double.parseDouble(offsetStr.trim());
                    plusMin = 1;
                }
                else
                {
                    throw new InvalidSyncRefException("Invalid SyncRef: " + syncRef
                            + ", syncref contains + without offset");
                }
            }
        }
        return syncRef.substring(0, syncRef.length() - (offsetStr.length() + plusMin));
    }

    /**
     * Parses a reference to a synchronization point. [bmlId:]offset | [bmlId:](source_id:sync_id
     * [ws*(+|-)ws*offset] )* | [bmlId:](source_id:stroke:stroke_id[ws*(+|-)ws*offset]) source_id :
     * IDREF | self |bml offset: float stroke_id: unsigned integer
     * 
     * @param syncRef
     */
    public SyncRef(String defaultBMLId, String syncRef) throws InvalidSyncRefException
    {
        bbId = defaultBMLId;
        String remainder = parseOffset(syncRef);
        if (remainder.length() == 0)
        {
            sourceId = "bml";
            syncId = "start";
            return;
        }
        if(remainder.startsWith(":"))
        {
            throw new InvalidSyncRefException("Invalid SyncRef: " + syncRef);
        }
        if(remainder.endsWith(":"))
        {
            throw new InvalidSyncRefException("Invalid SyncRef: " + syncRef);
        }

        String split[] = remainder.split(":");
        if (split.length < 2)
        {
            throw new InvalidSyncRefException("Invalid SyncRef: " + syncRef);
        }
        if (split.length > 4)
        {
            throw new InvalidSyncRefException("Invalid SyncRef: " + syncRef);
        }

        //find iteration (if any)
        if (split.length >= 3)
        {
            if (StringUtil.isPostiveInteger(split[split.length-1].trim()))
            {
                iteration = Integer.parseInt(split[split.length-1].trim());
                }
            else if (split.length > 3)
                {
                    throw new InvalidSyncRefException("Invalid SyncRef: " + syncRef
                            + " , iteration number must be a positive integer.");
                }
            }
        
        if (split.length == 3 && iteration==-1 || split.length > 3)
        {
            bbId = split[0].trim();
            sourceId = split[1].trim();
            syncId = split[2].trim();
        }
            else
            {
            sourceId = split[0].trim();
            syncId = split[1].trim();
            }
        if(iteration!=-1 && !syncId.equals("stroke"))
        {
            throw new InvalidSyncRefException("Invalid SyncRef: " + syncRef
                    + " , iteration number must be a positive integer.");
        }
    }

    /**
     * SyncRefs are equal if their sourceId, syncId, offset and strokeNr (if syncId=="stroke") match
     */
    @Override
    public boolean equals(Object s)
    {
        if (s instanceof SyncRef)
        {
            SyncRef sr = (SyncRef) s;
            return sr.offset == offset && sr.sourceId.equals(sourceId) && sr.syncId.equals(syncId)
                    && sr.iteration == iteration && sr.bbId.equals(bbId);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(offset, sourceId, syncId, iteration, bbId);
    }

    private String getSourceAndSyncString()
    {
        String s = sourceId + ":" + syncId;
        if (iteration != -1)
        {
            s = s + ":" + iteration;
        }
        if (offset != 0)
        {
            if (offset > 0)
            {
                s = s + '+';
            }
            s = s + offset;
        }
        return s;
    }
    
    /**
     * Get the String representation of the SyncRef, relative to bmlId
     */
    public String toString(String bmlId)
    {
        if(bbId.equals(bmlId) && sourceId.equals("bml") && syncId.equals("start")) //bmlId:bml:start => offset
        {
            return ""+offset;
        }
        String s = "";
        if(!bbId.equals(bmlId))
        {
            s = bbId+":";
        }        
        s += getSourceAndSyncString();
        return s;
    }    
    
    /**
     * Get the String representation of the SyncRef
     */
    @Override
    public String toString()
    {
        return bbId+":"+getSourceAndSyncString();        
    }
}
