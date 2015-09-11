package saiba.bml.core;

import hmi.xml.XMLTokenizer;

import java.io.IOException;

/**
 * Stub description behavior for some description level
 * @author hvanwelbergen
 *
 */
public class StubDescription extends Behaviour
{
    public StubDescription(String bmlId)
    {
        super(bmlId);
    }

    public StubDescription(String bmlId, String id, XMLTokenizer tok) throws IOException
    {
        super(bmlId, id);
        readXML(tok);
    }

    @Override
    public void addDefaultSyncPoints()
    {

    }

    private static final String XMLTAG = "stubdescription";

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
