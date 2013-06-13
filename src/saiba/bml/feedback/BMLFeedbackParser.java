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

import hmi.xml.XMLTokenizer;

import java.io.IOException;

/**
 * Parsers feedback String into BMLBlockPredictionFeedback, BMLBlockProgressFeedback, BMLPredictionFeedback, 
 * BMLWarningFeedback or BMLSyncPointProgressFeedback
 * @author Herwin
 */
public class BMLFeedbackParser
{
    private BMLFeedbackParser(){}
    
    /**
     * @param str the feedback String
     * @return the corresponding instance of feedback, null if none matching 
     * @throws IOException 
     */
    public static final BMLFeedback parseFeedback(String str) throws IOException
    {
        XMLTokenizer tok = new XMLTokenizer(str);
       
        if(tok.atSTag(BMLBlockProgressFeedback.xmlTag()))
        {
            BMLBlockProgressFeedback fb = new BMLBlockProgressFeedback();
            fb.readXML(tok);
            return fb;                        
        }
        else if(tok.atSTag(BMLPredictionFeedback.xmlTag()))
        {
            BMLPredictionFeedback fb = new BMLPredictionFeedback();
            fb.readXML(tok);
            return fb;                        
        }
        else if(tok.atSTag(BMLWarningFeedback.xmlTag()))
        {
            BMLWarningFeedback fb = new BMLWarningFeedback();
            fb.readXML(tok);
            return fb;                        
        }
        else if(tok.atSTag(BMLSyncPointProgressFeedback.xmlTag()))
        {
            BMLSyncPointProgressFeedback fb = new BMLSyncPointProgressFeedback();
            fb.readXML(tok);
            return fb;                        
        }
        return null;
    }
}
