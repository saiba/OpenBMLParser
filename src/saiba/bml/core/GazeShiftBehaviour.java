package saiba.bml.core;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import saiba.bml.parser.SyncPoint;

import com.google.common.collect.ImmutableList;

/**
 * This class represents gaze. This is represented in BML by the <code>&lt;gazeShift&gt;</code>-tag.
 */
public class GazeShiftBehaviour extends Behaviour
{
    @Override
    public void addDefaultSyncPoints()
    {
        for(String s:getDefaultSyncPoints())
        {
            addSyncPoint(new SyncPoint(bmlId, id, s));
        }        
    }
    private String target; // WorldID

    private float offsetAngle; // In degrees.

    private OffsetDirection offsetDirection = OffsetDirection.NONE;

    private String influence = ""; // Empty string is unknown.

    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start","end");
    
    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }

    public GazeShiftBehaviour(String bmlId,XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }
    
    @Override
    public boolean satisfiesConstraint(String name, String value)
    {
        if (name.equals("influence")) return influence.equals(value);
        if (name.equals("target")) return target.equals(value);
        if (name.equals("offsetDirection")) return offsetDirection.equals(value);
        return super.satisfiesConstraint(name, value);
    }

    @Override
    public float getFloatParameterValue(String name)
    {
        if (name.equals("offsetAngle")) return offsetAngle;
        return super.getFloatParameterValue(name);
    }

    @Override
    public String getStringParameterValue(String name)
    {
        if (name.equals("target")) return target;
        else if (name.equals("offsetAngle")) return "" + offsetAngle;
        else if (name.equals("offsetDirection")) return offsetDirection.toString();
        else if (name.equals("influence")) return influence;
        return super.getStringParameterValue(name);
    }

    @Override
    public boolean specifiesParameter(String name)
    {
        if (name.equals("target")) return true;
        else if (name.equals("offsetAngle")) return true;
        else if (name.equals("offsetDirection")) return true;
        else if (name.equals("influence")) return true;
        return super.specifiesParameter(name);
    }    

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        appendAttribute(buf, "target", target);
        appendAttribute(buf, "offsetAngle", offsetAngle);
        appendAttribute(buf, "offsetDirection", offsetDirection.toString());
        appendAttribute(buf, "influence", influence);        
        return super.appendAttributeString(buf, fmt);
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        target = getOptionalAttribute("target", attrMap);
        offsetAngle = getOptionalFloatAttribute("offsetAngle", attrMap, 0.0f);
        offsetDirection = OffsetDirection.valueOf(getOptionalAttribute("offsetDirection", attrMap,
                OffsetDirection.NONE.toString()));
        influence = getOptionalAttribute("influence", attrMap, "");
        super.decodeAttributes(attrMap, tokenizer);
    }

    private static final String XMLTAG = "gazeShift";

    public static String xmlTag()
    {
        return XMLTAG;
    }
    
    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
