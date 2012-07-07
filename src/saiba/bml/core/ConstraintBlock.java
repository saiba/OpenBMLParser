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
 * BML constraint block
 * @author paulrc
 */
public class ConstraintBlock extends BMLElement
{
    public ArrayList<Synchronize> synchronizes;

    public ArrayList<Before> before;

    public ArrayList<After> after;
    public final String bmlId;

    public ConstraintBlock(String bmlId)
    {
        this.bmlId = bmlId;
        synchronizes = new ArrayList<Synchronize>();
        before = new ArrayList<Before>();
        after = new ArrayList<After>();
    }

    @Override
    public String getBmlId()
    {
        return bmlId;
    }

    public ConstraintBlock(String bmlId, XMLTokenizer tokenizer) throws IOException
    {
        this(bmlId);
        readXML(tokenizer);
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        super.decodeAttributes(attrMap, tokenizer);
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(Synchronize.xmlTag()))
            {
                synchronizes.add(new Synchronize(bmlId, tokenizer));
            }
            else if (tag.equals(After.xmlTag()))
            {
                after.add(new After(bmlId, tokenizer));
            }
            else if (tag.equals(Before.xmlTag()))
            {
                before.add(new Before(bmlId, tokenizer));
            }
            ensureDecodeProgress(tokenizer);
        }
    }

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        appendXMLStructureList(buf, fmt, synchronizes);
        appendXMLStructureList(buf, fmt, after);
        appendXMLStructureList(buf, fmt, before);
        return buf;
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf)
    {
        return super.appendAttributeString(buf);
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "constraint";

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
        for (Synchronize sync : synchronizes)
        {
            sync.constructConstraints(scheduler);
        }
        for (After aft : after)
        {
            aft.constructConstraints(scheduler);
        }
        for (Before bef : before)
        {
            bef.constructConstraints(scheduler);
        }
    }
}
