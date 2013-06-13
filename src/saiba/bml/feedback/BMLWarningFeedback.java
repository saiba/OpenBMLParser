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

import hmi.xml.CharDataConversion;
import hmi.xml.XMLFormatting;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

/**
 * Warning feedback
 * @author Herwin
 * 
 */
public class BMLWarningFeedback extends XMLStructureAdapter implements BMLFeedback
{
    public static final String PARSING_FAILURE = "PARSING_FAILURE";
    public static final String NO_SUCH_TARGET = "NO_SUCH_TARGET";
    public static final String IMPOSSIBLE_TO_SCHEDULE = "IMPOSSIBLE_TO_SCHEDULE";
    public static final String BEHAVIOR_TYPE_NOT_SUPPORTED = "BEHAVIOR_TYPE_NOT_SUPPORTED";
    public static final String CUSTOM_BEHAVIOUR_NOT_SUPPORTED = "CUSTOM_BEHAVIOUR_NOT_SUPPORTED";
    public static final String CUSTOM_ATTRIBUTE_NOT_SUPPORTED = "CUSTOM_ATTRIBUTE_NOT_SUPPORTED";
    public static final String CANNOT_CREATE_BEHAVIOR = "CANNOT_CREATE_BEHAVIOR";

    private String characterId;
    private String type;
    private String id;
    private String description;

    public BMLWarningFeedback()
    {
        
    }
    
    public BMLWarningFeedback(String id, String type, String description)
    {
        this.id = id;
        this.type = type;
        this.description = description;
    }
    
    public String getCharacterId()
    {
        return characterId;
    }

    public String getType()
    {
        return type;
    }

    public String getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        if (tokenizer.atCharData()) description = CharDataConversion.decode(tokenizer.takeTrimmedCharData());
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        characterId = getOptionalAttribute("characterId", attrMap);
        id = getRequiredAttribute("id", attrMap, tokenizer);
        type = getRequiredAttribute("type", attrMap, tokenizer);
    }

    @Override
    public StringBuilder appendAttributes(StringBuilder buf)
    {
        if(characterId!=null)
        {
            appendAttribute(buf, "characterId", characterId);
        }
        appendAttribute(buf, "id", id);
        appendAttribute(buf, "type", type);
        return buf;
    }

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        buf.append(CharDataConversion.encode(description));
        return buf;
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "warningFeedback";

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

    public void setCharacterId(String characterId)
    {
        this.characterId = characterId;
    }
    
    public static final String BMLNAMESPACE = "http://www.bml-initiative.org/bml/bml-1.0";

    @Override
    public String getNamespace()
    {
        return BMLNAMESPACE;
    }
}
