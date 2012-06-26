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
