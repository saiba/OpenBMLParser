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
package saiba.bml.core;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import saiba.bml.BMLInfo;
import saiba.bml.parser.BMLParser;
import saiba.bml.parser.InvalidSyncRefException;
import saiba.bml.parser.SyncPoint;

import com.google.common.collect.ImmutableSet;

/**
 * This class is a superclass for all behaviors. It has no representation in BML. It holds common
 * variables. Note that not all elements of BML have this class as superclass: there are tags that
 * are not behaviors.
 * 
 * @author PaulRC
 */
public abstract class Behaviour extends BMLElement
{
    private ArrayList<SyncPoint> syncPoints;

    private ArrayList<Description> descriptions;

    public Behaviour descBehaviour = null;

    protected String bmlId;

    private boolean required = false;
    private CustomAttributeHandler caHandler = new CustomAttributeHandler();

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public float getFloatParameterValue(String name)
    {
        return caHandler.getCustomFloatParameterValue(name);
    }

    public String getStringParameterValue(String name)
    {
        if (caHandler.specifiesCustomStringParameter(name))
        {
            return caHandler.getCustomStringParameterValue(name);
        }
        return "" + getFloatParameterValue(name);
    }

    public boolean specifiesParameter(String name)
    {
        if(caHandler.specifiesCustomParameter(name))return true;
        return false;
    }

    public boolean satisfiesConstraint(String name, String value)
    {
        return caHandler.satisfiesCustomConstraint(name, value);
    }

    /**
     * Gets the set of external BML blocks that this behavior may affect/depend upon.
     */
    public Set<String> getOtherBlockDependencies()
    {
        return ImmutableSet.of();
    }

    public boolean isRequired()
    {
        return required;
    }

    @Override
    public String getBmlId()
    {
        return bmlId;
    }

    public Behaviour(String bmlId)
    {
        syncPoints = new ArrayList<SyncPoint>();
        descriptions = new ArrayList<Description>();
        this.bmlId = bmlId;
        addDefaultSyncPoints();
    }

    public abstract void addDefaultSyncPoints();

    public void addSyncPoint(SyncPoint syncPoint)
    {
        syncPoints.add(syncPoint);
    }

    public void removeSyncPoints(List<SyncPoint> points)
    {
        syncPoints.removeAll(points);
    }

    public void addSyncPoints(List<SyncPoint> points)
    {
        syncPoints.addAll(points);
    }

    public ArrayList<SyncPoint> getSyncPoints()
    {
        return syncPoints;
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        appendAttribute(buf, "id", bmlId + ":" + id);

        // Append attribute notation of sync-points.
        Iterator<SyncPoint> iter = syncPoints.iterator();
        while (iter.hasNext())
        {
            SyncPoint s = iter.next();
            if (s.getIteration() != -1) continue; // SyncPoints with iteration have no attribute
                                                  // counterparts.
            String ref = s.getRefString();
            if (ref.equals("")) continue; // Do not output empty sync point attributes.
            appendAttribute(buf, s.getName(), ref);
        }

        caHandler.appendCustomAttributeString(buf, fmt);

        return super.appendAttributeString(buf, fmt);
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        id = getRequiredAttribute("id", attrMap, tokenizer);
        String[] idSplit = id.split(":");
        if (idSplit.length == 2)
        {
            this.id = idSplit[1];
            this.bmlId = idSplit[0];
        }

        caHandler.decodeCustomAttributes(attrMap, tokenizer, BMLInfo.getCustomFloatAttributes(getClass()),
                BMLInfo.getCustomStringAttributes(getClass()), this);

        for (SyncPoint s : syncPoints)
        {
            s.setBehaviourId(id);
            s.setBMLId(bmlId);
        }

        // Decode attribute notation of standard sync-points.
        for (SyncPoint s : syncPoints)
        {
            String ref = getOptionalAttribute(s.getName(), attrMap);
            if (ref != null)
            {
                try
                {
                    s.setRefString(ref);
                }
                catch (InvalidSyncRefException e)
                {
                    throw new XMLScanException("Invalid sync ref", e);
                }
            }
        }
        super.decodeAttributes(attrMap, tokenizer);
    }

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        appendXMLStructureList(buf, fmt, descriptions);
        return buf;
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        int descPriority = 0;
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(Description.xmlTag()))
            {
                Description d = new Description(bmlId);
                d.id = id;
                d.readXML(tokenizer);
                if (d.isParsed)
                {
                    if (d.priority > descPriority)
                    {
                        descPriority = d.priority;
                        descBehaviour = d.behaviour;
                        descBehaviour.id = id;
                        descBehaviour.bmlId = bmlId;
                    }
                }
                descriptions.add(d);
            }
            ensureDecodeProgress(tokenizer);
        }
    }

    @Override
    public boolean hasContent()
    {
        return (descriptions.size() > 0);
    }

    public void constructConstraints(BMLParser scheduler)
    {
        scheduler.constructConstraints(this);
    }
}
