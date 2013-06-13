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
package saiba.bml.core.ext;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import saiba.bml.core.Behaviour;
import saiba.bml.parser.SyncPoint;

import com.google.common.collect.ImmutableList;

/**
 * This class represents faceFacs behaviour. This is represented in BML by the <code>&lt;faceFacs&gt;</code> -tag.
 * @author Herwin
 * 
 */
public class FaceFacsBehaviour extends Behaviour
{
    private float amount;
    private Side side;
    private int au;

    public FaceFacsBehaviour(String bmlId, XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }

    private static enum Side
    {
        LEFT, RIGHT, BOTH;
    }

    @Override
    public boolean satisfiesConstraint(String name, String value)
    {
        if (name.equals("amount"))
        {
            return Float.parseFloat(value) == amount;
        }
        else if (name.equals("au"))
        {
            return Integer.parseInt(value) == au;
        }
        else if (name.equals("side"))
        {
            return Side.valueOf(value) == side;
        }
        return super.satisfiesConstraint(name, value);
    }

    @Override
    public float getFloatParameterValue(String name)
    {
        if (name.equals("amount"))
        {
            return amount;
        }
        else if (name.equals("au"))
        {
            return au;
        }
        return super.getFloatParameterValue(name);
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        appendAttribute(buf, "amount", amount);
        appendAttribute(buf, "side", side.toString());
        appendAttribute(buf, "au", au);
        return super.appendAttributeString(buf, fmt);
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        amount = getOptionalFloatAttribute("amount", attrMap, 0.5f);
        side = Side.valueOf(getOptionalAttribute("side", attrMap, "BOTH"));
        au = getRequiredIntAttribute("au", attrMap, tokenizer);
        super.decodeAttributes(attrMap, tokenizer);
    }

    @Override
    public String getStringParameterValue(String name)
    {
        if (name.equals("amount"))
        {
            return "" + amount;
        }
        else if (name.equals("au"))
        {
            return "" + au;
        }
        else if (name.equals("side"))
        {
            return side.toString();
        }
        return super.getStringParameterValue(name);
    }

    @Override
    public boolean specifiesParameter(String name)
    {
        if (name.equals("amount") || name.equals("side") || name.equals("au")) return true;
        else return super.specifiesParameter(name);
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "faceFacs";

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

    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start", "attackPeak", "relax", "end");

    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }

    @Override
    public void addDefaultSyncPoints()
    {
        for (String s : getDefaultSyncPoints())
        {
            addSyncPoint(new SyncPoint(bmlId, id, s));
        }
    }

    static final String BMLTNAMESPACE = "http://www.bml-initiative.org/bml/coreextensions-1.0";

    @Override
    public String getNamespace()
    {
        return BMLTNAMESPACE;
    }
}
