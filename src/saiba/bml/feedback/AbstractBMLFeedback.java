package saiba.bml.feedback;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLNameSpace;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import saiba.bml.core.CustomAttributeHandler;

import com.google.common.collect.ImmutableList;

/**
 * Skeleton class for BMLFeedback
 * @author herwinvw
 *
 */
public class AbstractBMLFeedback extends XMLStructureAdapter implements BMLFeedback
{
    protected CustomAttributeHandler caHandler = new CustomAttributeHandler();

    public float getCustomFloatParameterValue(String name)
    {
        return caHandler.getCustomFloatParameterValue(name);
    }

    public void addCustomStringParameterValue(String name, String value)
    {
        caHandler.addCustomStringParameterValue(name, value);
    }

    public void addCustomFloatParameterValue(String name, float value)
    {
        caHandler.addCustomFloatParameterValue(name, value);
    }

    public String getCustomStringParameterValue(String name)
    {
        return caHandler.getCustomStringParameterValue(name);
    }

    public boolean specifiesCustomStringParameter(String name)
    {
        return caHandler.specifiesCustomStringParameter(name);
    }

    public boolean specifiesCustomFloatParameter(String name)
    {
        return caHandler.specifiesCustomFloatParameter(name);
    }

    public boolean specifiesCustomParameter(String name)
    {
        return caHandler.specifiesCustomParameter(name);
    }

    public boolean satisfiesCustomConstraint(String name, String value)
    {
        return caHandler.satisfiesCustomConstraint(name, value);
    }

    public void decodeCustomAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer, Set<String> floatAttributes,
            Set<String> stringAttributes, XMLStructureAdapter beh)
    {
        caHandler.decodeCustomAttributes(attrMap, tokenizer, floatAttributes, stringAttributes, beh);
    }

    public StringBuilder appendCustomAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        return caHandler.appendCustomAttributeString(buf, fmt);
    }

    public static final double UNKNOWN_TIME = -Double.MAX_VALUE;
    
    public String toBMLFeedbackString(XMLNameSpace... xmlNamespaces)
    {
        return toBMLFeedbackString(ImmutableList.copyOf(xmlNamespaces));        
    }
    
    public String toBMLFeedbackString(List<XMLNameSpace> xmlNamespaceList)
    {
        StringBuilder buf = new StringBuilder();
        appendXML(buf, new XMLFormatting(), xmlNamespaceList);
        return buf.toString();
    }
}
