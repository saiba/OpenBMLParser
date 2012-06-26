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
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import saiba.bml.parser.InvalidSyncRefException;
import saiba.bml.parser.SyncPoint;

import com.google.common.collect.ImmutableList;

/**
 * This class represents speech behaviour. This is represented in BML by the
 * <code>&lt;speech&gt;</code>-tag.
 * 
 * @author PaulRC
 */
public class SpeechBehaviour extends Behaviour
{
    protected String content;

    private ArrayList<Sync> syncs = new ArrayList<Sync>();

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "speech";

    /**
     * The XML Stag for XML encoding -- use this static method when you want to see if a given
     * String equals the xml tag for this class
     */
    public static String xmlTag()
    {
        return XMLTAG;
    }
    
    @Override
    public void addDefaultSyncPoints()
    {
        for(String s:getDefaultSyncPoints())
        {
            addSyncPoint(new SyncPoint(bmlId, id, s));
        }        
    }

    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start","end");
    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }
    
    @Override
    public String getStringParameterValue(String name)
    {
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
        return super.specifiesParameter(name);
    }

    public SpeechBehaviour(String bmlId,XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf)
    {
        return super.appendAttributeString(buf);
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        super.decodeAttributes(attrMap, tokenizer);
    }

    @Override
    public boolean hasContent()
    {
        return true;
    }

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        if (content != null) buf.append("<text>"+content+"</text>");
        return super.appendContent(buf, fmt); // Description is registered at Behavior.
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (!tokenizer.atETag())
        {
            if (tokenizer.atSTag("text"))
            {
                SpeechText text = new SpeechText();
                text.readXML(tokenizer);
                content = text.content;
                syncs = text.syncs;
                for(Sync sync:syncs)
                {
                    SyncPoint s = new SyncPoint(bmlId, id, sync.id);        
                    if(sync.ref!=null)
                    {
                        try
                        {
                            s.setRefString(sync.ref.toString(bmlId));
                        }
                        catch (InvalidSyncRefException e)
                        {
                            throw new XMLScanException("",e);
                        }
                    }
                    addSyncPoint(s);
                }
            }
            else
            {
                super.decodeContent(tokenizer);
            }

            ensureDecodeProgress(tokenizer);
        }
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

    class SpeechText extends XMLStructureAdapter
    {
        public String content = "";

        public ArrayList<Sync> syncs = new ArrayList<Sync>();

        @Override
        public void decodeContent(XMLTokenizer tokenizer) throws IOException
        {
            while (!tokenizer.atETag())
            {
                if (tokenizer.atSTag("sync"))
                {
                    Sync s = new Sync(bmlId);
                    s.readXML(tokenizer);
                    content = content + s.toString();
                    syncs.add(s);
                }
                else if (tokenizer.atCDSect())
                {
                    content = content + tokenizer.takeCDSect();
                }
                else if (tokenizer.atCharData())
                {
                    content = content + tokenizer.takeCharData();
                }

                ensureDecodeProgress(tokenizer);
            }
        }

        /**
         * The XML Stag for XML encoding -- use this method to find out the run-time xml tag of an
         * object
         */
        @Override
        public String getXMLTag()
        {
            return "text";
        }
        public static final String BMLNAMESPACE = "http://www.bml-initiative.org/bml/bml-1.0";

        @Override
        public String getNamespace()
        {
            return BMLNAMESPACE;
        }
    }

    /**
     * @return the content
     */
    public String getContent()
    {
        return content;
    }

}
