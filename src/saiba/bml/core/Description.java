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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import saiba.bml.BMLInfo;

/**
 * This class represents descriptions. These are used to have more specific levels of descriptions
 * than the standard 0 in BML. It is represented with the <code>&lt;description&gt;</code>-tag.
 * 
 * @author PaulRC
 */
public class Description extends BMLElement
{
    public int priority;    
    private String bmlId;
    
    private String type;

    private String content;

    public boolean isParsed;

    public Behaviour behaviour;

    private Logger logger = LoggerFactory.getLogger(Description.class.getName());

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
        return buf.append(content);
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
                        Constructor<? extends Behaviour> c = desc.getConstructor(new Class[]{String.class, XMLTokenizer.class});
                        behaviour = c.newInstance(bmlId,tokenizer);                        
                        isParsed = true;
                    }
                    catch (InstantiationException e)
                    {
                        logger.warn("InstantiationException when trying to initialize Description " + type + " description level ignored.",
                                e);
                        behaviour = null;

                    }
                    catch (IllegalAccessException e)
                    {
                        logger.warn("IllegalAccessException when trying to initialize Description " + type + " description level ignored.",
                                e);
                        behaviour = null;
                    }
                    catch (IllegalArgumentException e)
                    {
                        logger.warn("IllegalArgumentException when trying to initialize Description " + type + " description level ignored.",
                                e);
                        behaviour = null;
                    }
                    catch (InvocationTargetException e)
                    {
                        logger.warn("InvocationTargetException when trying to initialize Description " + type + " description level ignored.",
                                e);
                        e.printStackTrace();
                        behaviour = null;
                    }
                    catch (SecurityException e)
                    {
                        logger.warn("SecurityException when trying to initialize Description " + type + " description level ignored.",
                                e);
                        behaviour = null;
                    }
                    catch (NoSuchMethodException e)
                    {
                        logger.warn("NoSuchMethodException when trying to initialize Description " + type + " description level ignored.",
                                e);
                        behaviour = null;
                    }
                }
            }
        }

        if (behaviour == null)
        {
            content = tokenizer.getXMLSection();
            logger.info("skipped content: {}", content);
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
