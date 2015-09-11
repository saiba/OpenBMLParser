package saiba.bml.core;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CustomAttributeHandler
{
    private Map<String, String> customStringAttributes = new HashMap<String, String>();
    private Map<String, Float> customFloatAttributes = new HashMap<String, Float>();

    public float getCustomFloatParameterValue(String name)
    {
        if (customFloatAttributes.containsKey(name))
        {
            return customFloatAttributes.get(name);
        }
        throw new IllegalArgumentException("Parameter " + name + " not found/not a float.");
    }

    public void addCustomStringParameterValue(String name, String value)
    {
        customStringAttributes.put(name, value);
    }

    public void addCustomFloatParameterValue(String name, float value)
    {
        customFloatAttributes.put(name, value);
    }

    public String getCustomStringParameterValue(String name)
    {
        if (customStringAttributes.containsKey(name))
        {
            return customStringAttributes.get(name);
        }
        return null;
    }

    public boolean specifiesCustomStringParameter(String name)
    {
        if (customStringAttributes.containsKey(name)) return true;
        return false;
    }

    public boolean specifiesCustomFloatParameter(String name)
    {
        if (customFloatAttributes.containsKey(name)) return true;
        return false;
    }

    public boolean specifiesCustomParameter(String name)
    {
        if (customStringAttributes.containsKey(name)) return true;
        if (customFloatAttributes.containsKey(name)) return true;
        return false;
    }

    public boolean satisfiesCustomConstraint(String name, String value)
    {
        if (customStringAttributes.containsKey(name))
        {
            return customStringAttributes.get(name).equals(value);
        }
        return false;
    }

    public void decodeCustomAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer, Set<String> floatAttributes,
            Set<String> stringAttributes, XMLStructureAdapter beh)
    {
        for (String attr : floatAttributes)
        {
            String value = beh.getOptionalAttribute(attr, attrMap);
            if (value != null)
            {
                customFloatAttributes.put(attr, Float.parseFloat(value));
            }
        }

        for (String attr : stringAttributes)
        {
            String value = beh.getOptionalAttribute(attr, attrMap);
            if (value != null)
            {
                customStringAttributes.put(attr, value);
            }
        }
    }

    public StringBuilder appendCustomAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        for (Entry<String, String> entries : customStringAttributes.entrySet())
        {
            String ns = entries.getKey().substring(0, entries.getKey().lastIndexOf(":"));
            String attr = entries.getKey().substring(entries.getKey().lastIndexOf(":") + 1);
            XMLStructureAdapter.appendNamespacedAttribute(buf, fmt, ns, attr, entries.getValue());
        }

        for (Entry<String, Float> entries : customFloatAttributes.entrySet())
        {
            String ns = entries.getKey().substring(0, entries.getKey().lastIndexOf(":"));
            String attr = entries.getKey().substring(entries.getKey().lastIndexOf(":") + 1);
            XMLStructureAdapter.appendNamespacedAttribute(buf, fmt, ns, attr, "" + entries.getValue());
        }
        return buf;
    }
}
