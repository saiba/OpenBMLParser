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

import saiba.bml.parser.BMLParser;

/**
 * The Synchronize BML Element
 * @author paulrc
 */
public class Synchronize extends BMLElement
{
    private ArrayList<Sync> syncs;
    public String bmlId;

    public Synchronize(String bmlId)
    {
        this.bmlId = bmlId;
        syncs = new ArrayList<Sync>();
    }

    @Override
    public String getBmlId()
    {
        return bmlId;
    }

    public Synchronize(String bmlId, XMLTokenizer tokenizer) throws IOException
    {
        this(bmlId);
        readXML(tokenizer);
    }

    /**
     * @return the syncs
     */
    public ArrayList<Sync> getSyncs()
    {
        return syncs;
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        // super.decodeAttributes(attrMap, tokenizer);
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(Sync.xmlTag())) syncs.add(new Sync(bmlId, tokenizer));
            ensureDecodeProgress(tokenizer);
        }
    }

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        appendXMLStructureList(buf, fmt, syncs);
        return buf;
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf)
    {
        return buf;
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "synchronize";

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

    public void constructConstraints(BMLParser scheduler)
    {
        scheduler.constructConstraints(this);
    }
    
    private boolean required = false;

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }
}
