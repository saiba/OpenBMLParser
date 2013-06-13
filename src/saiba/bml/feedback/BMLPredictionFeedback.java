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
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import saiba.bml.core.Behaviour;
import saiba.bml.core.BehaviourParser;

/**
 * Prediction feedback for the timing of BML blocks and behaviours. 
 * @author hvanwelbergen
 */
public final class BMLPredictionFeedback extends XMLStructureAdapter implements BMLFeedback
{
    private List<BMLBlockPredictionFeedback> bmlBlockPredictions = new ArrayList<BMLBlockPredictionFeedback>();
    private List<Behaviour> bmlBehaviorPredictions = new ArrayList<Behaviour>();
    private String characterId;    
    
    public void setCharacterId(String characterId)
    {
        this.characterId = characterId;
    }

    public void addBMLBlockPrediction(BMLBlockPredictionFeedback bfp)
    {
        bmlBlockPredictions.add(bfp);
    }
    
    public void addBehaviorPrediction(Behaviour b)
    {
        bmlBehaviorPredictions.add(b);
    }
    
    @Override
    public StringBuilder appendAttributeString(StringBuilder buf)
    {
        if(characterId!=null)
        {
            appendAttribute(buf, "characterId", characterId);
        }
        return super.appendAttributeString(buf);
    }
    
    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        characterId = getOptionalAttribute("characterId", attrMap, null);
    }
    
    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        appendXMLStructureList(buf, fmt, bmlBlockPredictions);
        appendXMLStructureList(buf, fmt, bmlBehaviorPredictions);        
        return buf;
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            if(tokenizer.getTagName().equals(BMLBlockPredictionFeedback.xmlTag()))
            {
                BMLBlockPredictionFeedback pred = new BMLBlockPredictionFeedback();
                pred.readXML(tokenizer);
                bmlBlockPredictions.add(pred);
            }
            else
            {
                Behaviour b = BehaviourParser.parseBehaviour(null, tokenizer);
                if(b!=null)
                {
                    bmlBehaviorPredictions.add(b);
                }
            }
        }
    }
    
    public List<BMLBlockPredictionFeedback> getBmlBlockPredictions()
    {
        return bmlBlockPredictions;
    }

    public List<Behaviour> getBmlBehaviorPredictions()
    {
        return bmlBehaviorPredictions;
    }

    public String getCharacterId()
    {
        return characterId;
    }

    /**
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "predictionFeedback";

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
