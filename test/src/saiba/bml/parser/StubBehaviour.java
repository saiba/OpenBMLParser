package saiba.bml.parser;

import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.Set;

import saiba.bml.core.Behaviour;

import com.google.common.collect.ImmutableSet;

/**
 * Test stub for Behaviour
 * @author Herwin
 *
 */
public class StubBehaviour extends Behaviour
{
    public StubBehaviour(String bmlId)
    {
        super(bmlId);            
    }

    public StubBehaviour(String bmlId,XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }
    
    @Override
    public void addDefaultSyncPoints()
    {
                    
    }
    
    public Set<String> getOtherBlockDependencies()
    {
        return ImmutableSet.of("bmlx");
    }
    
    public static final String XMLTAG = "stub";

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
