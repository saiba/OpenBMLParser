/*******************************************************************************
 * Copyright (c) 2013 University of Twente, Bielefeld University
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package saiba.bml.feedback;

import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;

public class BMLBlockProgressFeedback extends XMLStructureAdapter implements BMLFeedback
{
    private String bmlId;
    private String syncId;
    private double globalTime;
    private String characterId;
    
    public BMLBlockProgressFeedback()
    {
        
    }
    
    public BMLBlockProgressFeedback(String bmlId, String syncId, double globalTime)
    {
        this(bmlId,syncId,globalTime,null);        
    }
    
    public BMLBlockProgressFeedback(String bmlId, String syncId, double globalTime, String characterId)
    {
        this.bmlId = bmlId;
        this.syncId = syncId;
        this.globalTime = globalTime;
        this.characterId = characterId;
    }
    
    public void setCharacterId(String characterId)
    {
        this.characterId = characterId;
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        String id = getRequiredAttribute("id", attrMap, tokenizer);
        String[] idSplit = id.split(":");
        bmlId = idSplit[0];
        syncId = idSplit[1];
        globalTime = getRequiredFloatAttribute("globalTime", attrMap, tokenizer);
        characterId = getOptionalAttribute("characterId", attrMap);        
    }
    
    @Override
    public StringBuilder appendAttributes(StringBuilder buf)
    {
        if(characterId!=null)
        {
            appendAttribute(buf, "characterId", characterId);
        }
        appendAttribute(buf, "id", bmlId+":"+syncId);
        appendAttribute(buf, "globalTime", globalTime);
        return buf;
    }

    
    public String getBmlId()
    {
        return bmlId;
    }

    public String getSyncId()
    {
        return syncId;
    }

    public double getGlobalTime()
    {
        return globalTime;
    }

    public String getCharacterId()
    {
        return characterId;
    }

    /**
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "blockProgress";

    /**
     * The XML Stag for XML encoding -- use this static method when you want to
     * see if a given String equals the xml tag for this class
     */
    public static String xmlTag()
    {
        return XMLTAG;
    }

    /**
     * The XML Stag for XML encoding -- use this method to find out the run-time
     * xml tag of an object
     */
    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
    public static final String BMLNAMESPACE = "http://www.bml-initiative.org/bml/bml-1.0";

    @Override
    public String getNamespace()
    {
        return BMLNAMESPACE;
    }
}
