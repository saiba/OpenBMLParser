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
import hmi.xml.XMLNameSpace;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import saiba.bml.parser.BMLParser;
import saiba.bml.parser.SyncPoint;
import saiba.bml.parser.SyncRef;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MutableClassToInstanceMap;

/**
 * This class represents a block of behaviour. This is represented in BML by the <code>&lt;bml&gt;</code>-tag.
 * 
 * @author PaulRC
 */
@Slf4j
public class BehaviourBlock extends BMLElement
{
    public ArrayList<RequiredBlock> requiredBlocks;

    public ArrayList<ConstraintBlock> constraintBlocks;

    public ArrayList<Behaviour> behaviours;

    @Setter
    @Getter
    private String characterId = null;
    
    private ClassToInstanceMap<BMLBehaviorAttributeExtension> bmlBehaviorAttributeExtensions;
    private boolean strict = true;
    
    public BehaviourBlock(boolean strict, BMLBehaviorAttributeExtension... bmlBehaviorAttributeExtensions)
    {
        this(bmlBehaviorAttributeExtensions);
        this.strict = strict;        
    }
    
    public BehaviourBlock(BMLBehaviorAttributeExtension... bmlBehaviorAttributeExtensions)
    {
        this.bmlBehaviorAttributeExtensions = MutableClassToInstanceMap.create();
        for (BMLBehaviorAttributeExtension ext : bmlBehaviorAttributeExtensions)
        {
            this.bmlBehaviorAttributeExtensions.put(ext.getClass(), ext);
        }
        requiredBlocks = new ArrayList<RequiredBlock>();
        constraintBlocks = new ArrayList<ConstraintBlock>();
        behaviours = new ArrayList<Behaviour>();
    }

    public void addBMLBehaviorAttributeExtension(BMLBehaviorAttributeExtension ext)
    {
        bmlBehaviorAttributeExtensions.put(ext.getClass(), ext);
    }

    public void addAllBMLBehaviorAttributeExtensions(Collection<BMLBehaviorAttributeExtension> exts)
    {
        for (BMLBehaviorAttributeExtension ext: exts)
        {
            addBMLBehaviorAttributeExtension(ext);
        }        
    }

    
    @Override
    public String getBmlId()
    {
        return id;
    }

    private void setSyncPointBMLId(String newId, Collection<SyncPoint> syncs)
    {
        for(SyncPoint s:syncs)
        {
            if(s.getBmlId().equals(id))
            {
                s.setBMLId(newId);
            }
            setBMLId(newId, s.getRef());
        }
    }
    
    private void setBMLId(String newId, SyncRef r)
    {
        if (r != null && r.bbId.equals(id))
        {
            r.bbId = newId;
        }
    }

    private void setBMLId(String newId, Collection<Sync> syncs)
    {
        for (Sync sync : syncs)
        {
            if (sync.bmlId.equals(id))
            {
                sync.bmlId = newId;
            }
            setBMLId(newId, sync.ref);
        }
    }

    public void setBmlId(String newId)
    {
        for (Behaviour beh : behaviours)
        {
            beh.bmlId = newId;
            setSyncPointBMLId(newId,beh.getSyncPoints());
        }
        for (ConstraintBlock cb : constraintBlocks)
        {
            cb.bmlId = newId;
            for (Synchronize s : cb.synchronizes)
            {
                s.bmlId = newId;
                setBMLId(newId, s.getSyncs());
            }
            for (After a : cb.after)
            {
                a.bmlId = newId;
                setBMLId(newId, a.getRef());
                setBMLId(newId, a.getSyncs());
            }
            for (Before b : cb.before)
            {
                b.bmlId = newId;
                setBMLId(newId, b.getRef());
                setBMLId(newId, b.getSyncs());
            }
        }
        id = newId;
    }

    private BMLBlockComposition composition = CoreComposition.UNKNOWN;

    public void setComposition(BMLBlockComposition c)
    {
        composition = c;
    }

    public BMLBlockComposition getComposition()
    {
        return composition;
    }

    public BehaviourBlock(XMLTokenizer tokenizer) throws IOException
    {
        this();
        readXML(tokenizer);
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "bml";

    /**
     * The XML Stag for XML encoding -- use this static method when you want to see if a given
     * String equals the xml tag for this class
     */
    public static String xmlTag()
    {
        return XMLTAG;
    }

    /**
     * The XML Stag for XML encoding -- use this method to find out the run-time xml tag of an
     * object
     */
    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        appendAttribute(buf, "id", id);
        appendAttribute(buf, "composition", composition.toString());
        if(characterId!=null)
        {
            appendAttribute(buf,"characterId",characterId);
        }
        for(BMLBehaviorAttributeExtension ext:bmlBehaviorAttributeExtensions.values())
        {
            ext.appendAttributeString(buf, fmt);
        }
        return super.appendAttributes(buf);
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        id = getRequiredAttribute("id", attrMap, tokenizer);
        characterId = getOptionalAttribute("characterId",attrMap, null);
        String sm = getOptionalAttribute("composition", attrMap, "MERGE");
        
        
        composition = CoreComposition.parse(sm);

        for (BMLBehaviorAttributeExtension ext : bmlBehaviorAttributeExtensions.values())
        {
            ext.decodeAttributes(this, attrMap, tokenizer);
            if (composition == CoreComposition.UNKNOWN)
            {
                composition = ext.handleComposition(sm);
            }
        }
        if(!attrMap.isEmpty())
        {
            throw new XMLScanException("Invalid attribute(s) "+attrMap.keySet()+" in bml block");
        }        
    }

    public Set<String> getOtherBlockDependencies()
    {
        Set<String> deps = new HashSet<String>();
        for (BMLBehaviorAttributeExtension ext : bmlBehaviorAttributeExtensions.values())
        {
            deps.addAll(ext.getOtherBlockDependencies());
        }
        return deps;
    }

    public <E extends BMLBehaviorAttributeExtension> E getBMLBehaviorAttributeExtension(Class<E> c)
    {
        return bmlBehaviorAttributeExtensions.getInstance(c);
    }

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        appendXMLStructureList(buf, fmt, requiredBlocks);
        appendXMLStructureList(buf, fmt, constraintBlocks);
        appendXMLStructureList(buf, fmt, behaviours);
        return buf;
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(RequiredBlock.xmlTag()))
            {
                RequiredBlock rb = new RequiredBlock(id, tokenizer);
                requiredBlocks.add(rb);
                behaviours.addAll(rb.behaviours);
                constraintBlocks.addAll(rb.constraintBlocks);
                continue;
            }
            if (tag.equals(ConstraintBlock.xmlTag()))
            {
                constraintBlocks.add(new ConstraintBlock(id, tokenizer));
                continue;
            }

            Behaviour b = BehaviourParser.parseBehaviour(id, tokenizer);
            if (b != null)
            {
                if (b.descBehaviour != null)
                {
                    behaviours.add(b.descBehaviour);
                }
                else
                {
                    behaviours.add(b);
                }
            }
            else
            {
                if(strict)
                {
                    throw new XMLScanException("Invalid behavior/construct "+tag+" in BML block "+getBmlId());
                }
                String skippedContent = tokenizer.getXMLSection();
                log.info("skipped content: {}", skippedContent);
            }
        }
    }    

    /**
     * Recursively calls resolveIDs(Scheduler, Breadcrumb) on top level behaviours and on
     * required-blocks.
     */
    public void registerElementsById(BMLParser scheduler)
    {
        // Add this behaviour block.
        scheduler.registerBMLElement(this);

        // Register top level behaviours.
        for (Behaviour b : behaviours)
            b.registerElementsById(scheduler);
    }

    public void constructConstraints(BMLParser scheduler)
    {
        // Top level behaviours.
        for (Behaviour b : behaviours)
        {
            b.constructConstraints(scheduler);
        }

        // Constraint blocks.
        for (ConstraintBlock ci : constraintBlocks)
        {
            ci.constructConstraints(scheduler);
        }
    }
    
    public String toBMLString(XMLNameSpace... xmlNamespaces)
    {
        return toBMLString(ImmutableList.copyOf(xmlNamespaces));        
    }
    
    public String toBMLString(List<XMLNameSpace> xmlNamespaceList)
    {
        StringBuilder buf = new StringBuilder();
        appendXML(buf, new XMLFormatting(), xmlNamespaceList);
        return buf.toString();
    }
}
