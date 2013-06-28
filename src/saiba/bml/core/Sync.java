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

import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import saiba.bml.parser.InvalidSyncRefException;
import saiba.bml.parser.SyncRef;

/**
 * This class represents a time marker. This is represented in BML by the <code>&lt;sync&gt;</code>
 * -tag and can exist only within <code>&lt;speech&gt;</code>.
 * 
 * @author PaulRC
 * @see bml.SpeechBehaviour
 */
public class Sync extends BMLElement
{
    public String bmlId;    
    
    public SyncRef ref;

    public Sync(String bmlId)
    {
        this.bmlId = bmlId;
        ref = null;
    }

    @Override
    public String getBmlId()
    {
        return bmlId;
    }

    public Sync(String bmlId, XMLTokenizer tokenizer) throws IOException
    {
        this(bmlId);
        readXML(tokenizer);
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf)
    {
        if (ref != null) appendAttribute(buf, "ref", ref.toString());
        if (id != null) appendAttribute(buf, "id", id);
        return buf;
        // return super.appendAttributeString(buf);
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        String ref = getOptionalAttribute("ref", attrMap);
        if (ref != null)
        {
            try
            {
                this.ref = new SyncRef(bmlId, ref);
            }
            catch (InvalidSyncRefException e)
            {
                throw new XMLScanException("Invalid sync ref");
            }
        }
        id = getOptionalAttribute("id", attrMap);        
    }

    @Override
    public boolean hasContent()
    {
        return false;
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "sync";

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
}
