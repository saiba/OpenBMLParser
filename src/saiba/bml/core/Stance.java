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
package saiba.bml.core;

import java.util.HashMap;

import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

/**
 * This class represents the stance element in e.g. <code>&lt;posture&gt;</code> and 
 * <code>&lt;postureShift&gt;</code>. This is represented in BML by the <code>&lt;stance&gt;</code>
 * -tag.
 * 
 * @author welberge
 */
public class Stance extends XMLStructureAdapter
{
    private enum StanceType
    {
        SITTING, CROUCHING, STANDING, LYING;
    }
    private StanceType stance;
    
    public String getStanceType()
    {
        return stance.toString();
    }
    
    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        stance = StanceType.valueOf(getRequiredAttribute("type", attrMap, tokenizer));
        super.decodeAttributes(attrMap,tokenizer);
    }
    
    @Override
    public StringBuilder appendAttributeString(StringBuilder buf)
    {
        appendAttribute(buf, "type", stance.toString());
        return super.appendAttributeString(buf);
    }
    
    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "stance";

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
    
    public static final String BMLNAMESPACE = "http://www.bml-initiative.org/bml/bml-1.0";

    @Override
    public String getNamespace()
    {
        return BMLNAMESPACE;
    }
}
