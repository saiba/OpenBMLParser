package saiba.bml.core;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import saiba.bml.parser.SyncPoint;

import com.google.common.collect.ImmutableList;

/**
 * * This class represents head behaviour. This is represented in BML by the <code>&lt;headDirectionShift&gt;</code>
 * -tag.
 * @author welberge
 *
 */
public class HeadDirectionShiftBehaviour extends Behaviour
{
    private String target;
    
    public HeadDirectionShiftBehaviour(String bmlId,XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }

    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start","ready","strokeStart", "stroke","strokeEnd","relax","end");

    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }
    
    @Override
    public float getFloatParameterValue(String name)
    {
        return super.getFloatParameterValue(name);
    }

    @Override
    public String getStringParameterValue(String name)
    {
        if(name.equals("target"))return target;
        return super.getStringParameterValue(name);
    }

    @Override
    public boolean specifiesParameter(String name)
    {
        if(name.equals("target"))return true;
        return super.specifiesParameter(name);
    }

    @Override
    public void addDefaultSyncPoints()
    {
        for(String s:getDefaultSyncPoints())
        {
            addSyncPoint(new SyncPoint(bmlId, id, s));
        }        
    }
    
    @Override
    public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        appendAttribute(buf, "target", target);
        return super.appendAttributeString(buf, fmt);
    }
    
    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        target = getRequiredAttribute("target", attrMap, tokenizer);        
        super.decodeAttributes(attrMap, tokenizer);
    }
    
    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "headDirectionShift";

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
