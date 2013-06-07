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
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import saiba.bml.parser.BMLParser;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

/**
 * This class represents a block of behaviour. This is represented in BML by the <code>&lt;bml&gt;</code>-tag.
 * 
 * @author PaulRC
 */
public class BehaviourBlock extends BMLElement
{
    public ArrayList<RequiredBlock> requiredBlocks;

    public ArrayList<ConstraintBlock> constraintBlocks;

    public ArrayList<Behaviour> behaviours;

    ClassToInstanceMap<BMLBehaviorAttributeExtension> bmlBehaviorAttributeExtensions;

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

    protected void addBMLBehaviorAttributeExtension(BMLBehaviorAttributeExtension ext)
    {
        bmlBehaviorAttributeExtensions.put(ext.getClass(), ext);
    }

    @Override
    public String getBmlId()
    {
        return id;
    }

    private BMLBlockComposition composition = CoreComposition.UNKNOWN;

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
    public StringBuilder appendAttributeString(StringBuilder buf)
    {
        appendAttribute(buf, "id", id);
        appendAttribute(buf, "composition", composition.toString().toLowerCase(Locale.US));
        return super.appendAttributes(buf);
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        id = getRequiredAttribute("id", attrMap, tokenizer);
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
        super.decodeAttributes(attrMap, tokenizer);
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
            }
            if (tag.equals(ConstraintBlock.xmlTag()))
            {
                constraintBlocks.add(new ConstraintBlock(id, tokenizer));                
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
            ensureDecodeProgress(tokenizer);
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
}
