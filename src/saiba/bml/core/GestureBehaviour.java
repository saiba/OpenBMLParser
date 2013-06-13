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

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import saiba.bml.parser.SyncPoint;

import com.google.common.collect.ImmutableList;

/**
 * This class represents gestures. This is represented in BML by the <code>&lt;gesture&gt;</code> -tag.
 * 
 * @author PaulRC
 */
public class GestureBehaviour extends Behaviour
{
    @Override
    public void addDefaultSyncPoints()
    {
        for (String s : getDefaultSyncPoints())
        {
            addSyncPoint(new SyncPoint(bmlId, id, s));
        }
    }

    private Mode mode;

    // private int repetition;

    private String target; // World ID.

    private String lexeme = "";

    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start", "ready", "strokeStart", "stroke", "strokeEnd", "relax",
            "end");

    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }

    public GestureBehaviour(String bmlId, XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }

    @Override
    public boolean satisfiesConstraint(String name, String value)
    {
        if (name.equals("mode")) return mode.toString().equals(value);
        if (name.equals("lexeme")) return lexeme.equals(value);
        return super.satisfiesConstraint(name, value);
    }

    @Override
    public String getStringParameterValue(String name)
    {
        if (name.equals("mode")) return mode.toString();
        if (name.equals("lexeme")) return lexeme;
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
        if (name.equals("mode")) return true;
        // if (name.equals("repetition")) return true;
        if (name.equals("lexeme")) return true;
        return super.specifiesParameter(name);
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        if (!mode.equals(Mode.UNSPECIFIED))
        {
            appendAttribute(buf, "mode", mode.toString());
        }
        // appendAttribute(buf, "repetition", repetition);
        appendAttribute(buf, "target", target);
        appendAttribute(buf, "lexeme", lexeme);
        return super.appendAttributeString(buf, fmt);
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        mode = Mode.valueOf(getOptionalAttribute("mode", attrMap, Mode.UNSPECIFIED.toString()));
        // repetition = getOptionalIntAttribute("repetition", attrMap, 1);
        /*
         * if (repetition > 1)
         * {
         * for (int i = 1; i < repetition; i++)
         * {
         * addSyncPoint(new SyncPoint(bmlId,id, "stroke", i));
         * }
         * }
         */

        lexeme = getRequiredAttribute("lexeme", attrMap, tokenizer);
        super.decodeAttributes(attrMap, tokenizer);
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "gesture";

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
