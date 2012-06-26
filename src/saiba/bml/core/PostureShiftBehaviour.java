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

import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * This class represents postureShift behaviour. This is represented in BML by the <code>&lt;postureShift&gt;</code>
 * -tag.
 * 
 * @author PaulRC
 */
public class PostureShiftBehaviour extends PostureBehaviour 
{
    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start","end");
    
    public PostureShiftBehaviour(String bmlId,XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId,tokenizer);        
    }
    
    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "postureShift";

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
