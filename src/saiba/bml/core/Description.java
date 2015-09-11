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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import lombok.extern.slf4j.Slf4j;
import saiba.bml.BMLInfo;

/**
 * This class represents descriptions. These are used to have more specific levels of descriptions
 * than the standard 0 in BML. It is represented with the <code>&lt;description&gt;</code>-tag.
 * 
 * @author PaulRC
 */
@Slf4j
public class Description extends BMLElement
{
    public int priority;
    private String bmlId;

    private String type;

    public boolean isParsed;

    public Behaviour behaviour;

    @Override
    public String getBmlId()
    {
        return bmlId;
    }

    public Description(String bmlId)
    {
        this.bmlId = bmlId;
        isParsed = false;
    }

    public Description(String bmlId, XMLTokenizer tokenizer) throws IOException
    {
        this(bmlId);
        isParsed = false;
        readXML(tokenizer);
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf)
    {
        appendAttribute(buf, "priority", priority);
        appendAttribute(buf, "type", type);
        return buf;
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        priority = getRequiredIntAttribute("priority", attrMap, tokenizer);
        type = getRequiredAttribute("type", attrMap, tokenizer);

        // super.decodeAttributes(attrMap, tokenizer);
    }

    @Override
    public boolean hasContent()
    {
        return true;
    }

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        if (behaviour != null)
        {
            buf = behaviour.appendXML(buf, fmt);
        }
        return super.appendContent(buf, fmt);
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        if (tokenizer.atSTag())
        {
            Class<? extends Behaviour> desc = BMLInfo.getDescriptionExtensions().get(type);
            if (desc != null)
            {
                if (BMLInfo.supportedExtensions.contains(desc))
                {
                    behaviour = null;                    
                    try
                    {
                        Constructor<? extends Behaviour> c = desc.getConstructor(new Class[] { String.class, String.class, XMLTokenizer.class });
                        behaviour = c.newInstance(bmlId, id, tokenizer);
                    }
                    catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
                    {
                        String cause = e.getMessage()==null?"":e.getMessage();
                        String causeOfCause = e.getCause()==null?"":e.getCause().getMessage();                        
                        throw new XMLScanException("Error parsing/constructing description.\n"+cause+"\n"+causeOfCause+"\n", e);
                    }
                    isParsed = true;
                }
            }
        }

        if (behaviour == null)
        {
            String content = tokenizer.getXMLSection();
            log.info("skipped content: {}", content);
        }
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "description";

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
