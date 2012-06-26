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

import saiba.bml.parser.BMLParser;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

/**
 * This is a superclass for representation of all elements that can exist in a BML-document.
 * 
 * @author PaulRC
 */
public class BMLElement extends XMLStructureAdapter
{
    public String id;
    public String getBmlId()
    {
        return "";
    }
    
    
    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "BMLStructureAdapter";

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

    /**
     * Registers the (full) id of this BMLElement with the scheduler.
     */
    public void registerElementsById(BMLParser scheduler)
    {
        // Register this BMLElement.
        scheduler.registerBMLElement(this);
    }

    /**
     * Detects loops in decodeContent()-methods.
     */
    private int prevLine = -1;

    private int prevChar = -1;

    protected void ensureDecodeProgress(XMLTokenizer tokenizer)
    {
        int curLine = tokenizer.getLine();
        int curChar = tokenizer.getCharPos();
        if (curLine == prevLine && curChar == prevChar)
        {
            throw new RuntimeException(
                    "Loop detected, no valid BML. Possibly, a STag was encountered that cannot be parsed. Line: "
                            + curLine + ", char: " + curChar);
        }
        else
        {
            prevLine = curLine;
            prevChar = curChar;
        }
    }
    
    public static final String BMLNAMESPACE = "http://www.bml-initiative.org/bml/bml-1.0";

    @Override
    public String getNamespace()
    {
        return BMLNAMESPACE;
    }
}
