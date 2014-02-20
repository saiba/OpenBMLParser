package saiba.bml.parser;

import hmi.xml.XMLTokenizer;

import java.io.IOException;

/**
 * Stubbehavior in external namespace
 * @author hvanwelbergen
 *
 */
public class ExternalStubBehavior extends StubBehaviour
{
    public ExternalStubBehavior(String bmlId, XMLTokenizer tok)throws IOException
    {
        super(bmlId, tok);            
    }
    
    public ExternalStubBehavior(String bmlId)
    {
        super(bmlId);            
    }
    
    public static final String NAMESPACE = "stubnamespace";

    @Override
    public String getNamespace()
    {
        return NAMESPACE;
    }
    
    public static final String XMLTAG = "stubexternal";

    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
