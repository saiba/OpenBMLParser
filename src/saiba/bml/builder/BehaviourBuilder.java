package saiba.bml.builder;

import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import saiba.bml.core.Behaviour;
import saiba.bml.core.BehaviourBlock;
import saiba.bml.core.BehaviourParser;

/**
 * Builds a behaviour
 * @author hvanwelbergen
 *
 */
public class BehaviourBuilder
{
    private final String type;
    private final String id;
    private final String bmlId;
    private String content = "";
    private String namespace = BehaviourBlock.BMLNAMESPACE;
    private List<Param> params = new ArrayList<Param>();
    
    private final class Param
    {
        public String getName()
        {
            return name;
        }
        public String getValue()
        {
            return value;
        }
        private final String name;
        private final String value;
        public Param(String name, String value)
        {
            this.name = name;
            this.value = value;
        }
    }
    
    public BehaviourBuilder(String type, String bmlId, String id)
    {
        this.type = type;
        this.bmlId = bmlId;
        this.id = id;
    }
    
    public BehaviourBuilder param(String name, String value)
    {
        params.add(new Param(name,value));
        return this;
    }
    
    public BehaviourBuilder namespace(String ns)
    {
        namespace = ns;
        return this;
    }
    
    public BehaviourBuilder content(String content)
    {
        this.content = content;
        return this;
    }
    
    private String getXMLRepresentation()
    {
        StringBuilder xmlBuilder = new StringBuilder("<"+type+" id=\""+id+"\" xmlns=\""+namespace+"\" ");
        for (Param p:params)
        {
            xmlBuilder.append(p.getName()+"=\""+p.getValue()+"\" ");
        }
        xmlBuilder.append(">");
        xmlBuilder.append(content);
        xmlBuilder.append("</"+type+">");
        return xmlBuilder.toString();
    }
    
    public Behaviour build()
    {
        try
        {
            return BehaviourParser.parseBehaviour(bmlId, new XMLTokenizer(getXMLRepresentation()));
        }
        catch (IOException e)
        {
            throw new XMLScanException("Error parsing behavior "+getXMLRepresentation(),e);
        }
    }
}
