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
import java.util.Iterator;
import java.util.List;

import saiba.bml.parser.BMLParser;

/**
 * This class represents a block of required behaviours or constraints. This is represented in
 * BML by the <code>&lt;required&gt;</code>-tag.
 * 
 * @author PaulRC
 */
public class RequiredBlock extends BMLElement
{
    public List<ConstraintBlock> constraintBlocks;

    public List<Behaviour> behaviours;
    public final String bmlId;

    public RequiredBlock(String bmlId)
    {
        this.bmlId = bmlId;
        behaviours = new ArrayList<Behaviour>();
        constraintBlocks = new ArrayList<ConstraintBlock>();
    }

    public String getBmlId()
    {
        return bmlId;
    }

    public RequiredBlock(String bmlId, XMLTokenizer tokenizer) throws IOException
    {
        this(bmlId);
        readXML(tokenizer);
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "required";

    /**
     * The XML Stag for XML encoding -- use this static method when you want to
     * see if a given String equals the xml tag for this class
     */
    public static String xmlTag()
    {
        return XMLTAG;
    }

    /**
     * The XML Stag for XML encoding -- use this method to find out the run-time
     * xml tag of an object
     */
    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf)
    {
        return buf;
    }

    @Override
    public boolean decodeAttribute(String attrName, String valCode, XMLTokenizer tokenizer)
    {
        return super.decodeAttribute(attrName, valCode, tokenizer);
    }

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        appendXMLStructureList(buf, fmt, behaviours);
        appendXMLStructureList(buf, fmt, constraintBlocks);
        return buf;
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(ConstraintBlock.xmlTag()))
            {
                constraintBlocks.add(new ConstraintBlock(bmlId, tokenizer));
            }
            else
            {
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
            }
            ensureDecodeProgress(tokenizer);
        }        
    }

    public void constructConstraints(BMLParser scheduler)
    {
        // Behaviours.
        Iterator<Behaviour> bi = behaviours.iterator();
        while (bi.hasNext())
            bi.next().constructConstraints(scheduler);
    }
}
