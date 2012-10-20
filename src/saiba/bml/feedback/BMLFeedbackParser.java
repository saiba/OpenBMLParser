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
        if(tok.atSTag(BMLBlockPredictionFeedback.xmlTag()))
        {
            BMLBlockPredictionFeedback fb = new BMLBlockPredictionFeedback();
            fb.readXML(tok);
            return fb;                        
        }
        else if(tok.atSTag(BMLBlockProgressFeedback.xmlTag()))
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
