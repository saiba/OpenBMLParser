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

import saiba.bml.parser.BMLParser;
import saiba.bml.parser.InvalidSyncRefException;
import saiba.bml.parser.SyncRef;

/**
 * After contraint
 * @author paulrc
 */
public class After extends BMLElement
{
    private ArrayList<Sync> syncs;

    private SyncRef ref;
    public String bmlId;

    @Override
    public String getBmlId()
    {
        return bmlId;
    }
    
    public After(String bmlId)
    {
        syncs = new ArrayList<Sync>();
        ref = null;
        this.bmlId = bmlId;
    }

    public After(String bmlId,XMLTokenizer tokenizer) throws IOException
    {
        this(bmlId);
        readXML(tokenizer);
    }

    /**
     * @return the syncs, excluding the reference after sync
     */
    public ArrayList<Sync> getSyncs()
    {
        return syncs;
    }

    /**
     * Get the reference sync
     */
    public SyncRef getRef()
    {
        return ref;
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        String r = getRequiredAttribute("ref", attrMap, tokenizer);
        try
        {
            ref = new SyncRef(bmlId, r);
        }
        catch (InvalidSyncRefException e)
        {
            throw new XMLScanException("Invalid sync ref",e);            
        }
        super.decodeAttributes(attrMap, tokenizer);
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(Sync.xmlTag())) syncs.add(new Sync(bmlId,tokenizer));
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
        appendAttribute(buf, "ref", ref.toString());
        return super.appendAttributeString(buf);
    }
    
    public void constructConstraints(BMLParser scheduler)
    {
        scheduler.constructConstraints(this);
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "after";

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
