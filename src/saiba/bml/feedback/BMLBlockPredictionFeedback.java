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

import hmi.xml.XMLFormatting;
import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;

import lombok.Delegate;
import saiba.bml.BMLInfo;
import saiba.bml.core.CustomAttributeHandler;

/**
 * XML parser for a bml block prediction feedback element
 * @author hvanwelbergen
 * 
 */
public final class BMLBlockPredictionFeedback extends XMLStructureAdapter implements BMLFeedback
{
    public static final double UNKNOWN_TIME = -Double.MAX_VALUE;
    private String id;
    private double globalStart, globalEnd;

    @Delegate
    private CustomAttributeHandler caHandler = new CustomAttributeHandler();

    public String getId()
    {
        return id;
    }

    public double getGlobalStart()
    {
        return globalStart;
    }

    public double getGlobalEnd()
    {
        return globalEnd;
    }

    public BMLBlockPredictionFeedback()
    {

    }

    public BMLBlockPredictionFeedback(String id, double globalStart, double globalEnd)
    {
        this.id = id;
        this.globalStart = globalStart;
        this.globalEnd = globalEnd;
    }

    @Override
    public boolean hasContent()
    {
        return false;
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        id = getRequiredAttribute("id", attrMap, tokenizer);
        globalStart = getOptionalDoubleAttribute("globalStart", attrMap, UNKNOWN_TIME);
        globalEnd = getOptionalDoubleAttribute("globalEnd", attrMap, UNKNOWN_TIME);
        if (globalStart == UNKNOWN_TIME && globalEnd == UNKNOWN_TIME)
        {
            throw new XMLScanException("<bml> feedback element should specify at least one of globalStart, globalEnd");
        }
        caHandler.decodeCustomAttributes(attrMap, tokenizer, BMLInfo.getCustomFeedbackFloatAttributes(getClass()),
                BMLInfo.getCustomFeedbackStringAttributes(getClass()), this);
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        appendAttribute(buf, "id", id);
        if (globalStart != UNKNOWN_TIME)
        {
            appendAttribute(buf, "globalStart", globalStart);
        }
        if (globalEnd != UNKNOWN_TIME)
        {
            appendAttribute(buf, "globalEnd", globalEnd);
        }
        caHandler.appendCustomAttributeString(buf, fmt);
        return buf;
    }

    /**
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "bml";

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
