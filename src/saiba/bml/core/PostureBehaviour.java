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
import java.util.List;

import saiba.bml.parser.SyncPoint;

import com.google.common.collect.ImmutableList;

/**
 * This class represents body behaviour. This is represented in BML by the <code>&lt;body&gt;</code> -tag.
 * 
 * @author PaulRC
 */
public class PostureBehaviour extends Behaviour
{
    private Stance stance;
    private List<Pose> poseParts = new ArrayList<Pose>();

    public List<Pose> getPoseParts()
    {
        return poseParts;
    }

    @Override
    public void addDefaultSyncPoints()
    {
        for (String s : getDefaultSyncPoints())
        {
            addSyncPoint(new SyncPoint(bmlId, id, s));
        }
    }

    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start", "ready", "relax", "end");

    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }

    private String getPoseLexeme(String part)
    {
        for (Pose p : poseParts)
        {
            if (p.getPart().equals(part))
            {
                return p.getLexeme();
            }
        }
        return null;
    }
    
    @Override
    public String getStringParameterValue(String name)
    {
        if (name.equals("stance") && stance != null) return stance.getStanceType();
        String poseLexeme = getPoseLexeme(name);
        if(poseLexeme!=null)return poseLexeme;
        return super.getStringParameterValue(name);
    }

    @Override
    public float getFloatParameterValue(String name)
    {
        return super.getFloatParameterValue(name);
    }

    @Override
    public boolean specifiesParameter(String name)
    {
        if (name.equals("stance") && stance != null) return true;
        if (getPoseLexeme(name)!=null) return true;
        return super.specifiesParameter(name);
    }

    @Override
    public boolean satisfiesConstraint(String name, String value)
    {
        if (name.equals("stance") && stance != null) return stance.getStanceType().equals(value);
        String poseLexeme = getPoseLexeme(name);
        if(poseLexeme!=null)
        {
            return value.equals(poseLexeme);
        }
        return super.satisfiesConstraint(name, value);
    }

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        if (stance != null)
        {
            stance.appendXML(buf, fmt);
        }
        for (Pose p : poseParts)
        {
            p.appendXML(buf, fmt);
        }
        return super.appendContent(buf, fmt); // Description is registered at Behavior.
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (!tokenizer.atETag())
        {
            if (tokenizer.atSTag(Pose.xmlTag()))
            {
                Pose p = new Pose();
                p.readXML(tokenizer);
                poseParts.add(p);
            }
            else if (tokenizer.atSTag(Stance.xmlTag()))
            {
                stance = new Stance();
                stance.readXML(tokenizer);
            }
            else
            {
                super.decodeContent(tokenizer);
            }

            ensureDecodeProgress(tokenizer);
        }
    }

    public PostureBehaviour(String bmlId, XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }    


    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "posture";

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
    public boolean hasContent()
    {
        return true;
    }
}
