package saiba.bml.core;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import saiba.bml.parser.SyncPoint;

import com.google.common.collect.ImmutableList;

/**
 * This class represents pointing behaviour. This is represented in BML by the <code>&lt;pointing&gt;</code>
 * -tag.
 * 
 * @author welberge
 */
public class PointingBehaviour extends Behaviour
{
    private Mode mode;
    private String target;
    
    public PointingBehaviour(String bmlId,XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
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
        if(name.equals("mode"))return mode.toString();
        return super.getStringParameterValue(name);
    }

    @Override
    public boolean specifiesParameter(String name)
    {
        if(name.equals("target"))return true;
        if(name.equals("mode"))return true;
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
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        target = getRequiredAttribute("target", attrMap, tokenizer);
        mode = Mode.valueOf(getOptionalAttribute("mode",attrMap,Mode.UNSPECIFIED.toString()));
        super.decodeAttributes(attrMap,tokenizer);
    }
    
    @Override
    public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        appendAttribute(buf, "target", target);
        if(mode != Mode.UNSPECIFIED)
        {
            appendAttribute(buf, "mode", mode.toString());
        }
        return super.appendAttributeString(buf, fmt);
    }
    
    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start","ready","strokeStart", "stroke","strokeEnd","relax","end");
    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "pointing";
    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }

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
