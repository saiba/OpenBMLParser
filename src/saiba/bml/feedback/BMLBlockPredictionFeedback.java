package saiba.bml.feedback;

import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;

/**
 * XML parser for a bml block prediction feedback element
 * @author hvanwelbergen
 * 
 */
public final class BMLBlockPredictionFeedback extends XMLStructureAdapter 
{
    public static final double UNKNOWN_TIME = -Double.MAX_VALUE;
    private String id;
    private double globalStart, globalEnd;

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
    }

    @Override
    public StringBuilder appendAttributes(StringBuilder buf)
    {
        appendAttribute(buf, "id", id);
        if(globalStart!=UNKNOWN_TIME)
        {
            appendAttribute(buf, "globalStart", globalStart);
        }
        if(globalEnd!=UNKNOWN_TIME)
        {
            appendAttribute(buf, "globalEnd", globalEnd);
        }
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
