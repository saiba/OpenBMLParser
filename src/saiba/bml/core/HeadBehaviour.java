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
import java.util.HashMap;
import java.util.List;

import saiba.bml.parser.SyncPoint;

import com.google.common.collect.ImmutableList;

/**
 * This class represents head behaviour. This is represented in BML by the <code>&lt;head&gt;</code>
 * -tag.
 * 
 * @author PaulRC
 */
public class HeadBehaviour extends Behaviour
{
    private int repeats;
    private String lexeme;
    private float amount;

    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start","ready","strokeStart", "stroke","strokeEnd","relax","end");

    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }
    
    @Override
    public void addDefaultSyncPoints()
    {
        for(String s:getDefaultSyncPoints())
        {
            addSyncPoint(new SyncPoint(bmlId, id, s));
        }        
    }

    
    @Override
    public String getStringParameterValue(String name)
    {
        if (name.equals("repetition")) return "" + repeats;
        if (name.equals("amount")) return "" + amount;
        if (name.equals("lexeme")) return lexeme;        
        return super.getStringParameterValue(name);
    }

    @Override
    public boolean satisfiesConstraint(String name, String value)
    {
        if (name.equals("repetition"))
        {
            return Integer.parseInt(value) == repeats;
        }
        else if (name.equals("lexeme"))
        {
            return lexeme.equals(value);
        }            
        return super.satisfiesConstraint(name, value);
    }

    public HeadBehaviour(String bmlId,XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        appendAttribute(buf, "lexeme", lexeme);
        appendAttribute(buf, "repetition", repeats);
        appendAttribute(buf, "amount", amount);        
        return super.appendAttributeString(buf, fmt);
    }

    @Override
    public float getFloatParameterValue(String name)
    {
        if (name.equals("repetition"))
        {
            return repeats;
        }
        else if (name.equals("amount"))
        {
            return amount;
        }
        return super.getFloatParameterValue(name);
    }

    @Override
    public boolean specifiesParameter(String name)
    {
        if (name.equals("repetition") || name.equals("amount")||name.equals("lexeme")) return true;
        return super.specifiesParameter(name);
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        repeats = getOptionalIntAttribute("repetition", attrMap, 1);
        if (repeats > 1)
        {
            for (int i = 1; i < repeats; i++)
            {
                addSyncPoint(new SyncPoint(bmlId,id, "stroke", i));
            }
        }

        amount = getOptionalFloatAttribute("amount", attrMap, 0.5f);
        lexeme = getRequiredAttribute("lexeme", attrMap,tokenizer);

        super.decodeAttributes(attrMap, tokenizer);
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "head";

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
