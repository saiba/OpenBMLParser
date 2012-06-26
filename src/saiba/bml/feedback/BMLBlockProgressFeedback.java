package saiba.bml.feedback;

import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;

public class BMLBlockProgressFeedback extends XMLStructureAdapter implements BMLFeedback
{
    private String bmlId;
    private String syncId;
    private double globalTime;
    private String characterId;
    
    public BMLBlockProgressFeedback()
    {
        
    }
    
    public BMLBlockProgressFeedback(String bmlId, String syncId, double globalTime)
    {
        this(bmlId,syncId,globalTime,null);        
    }
    
    public BMLBlockProgressFeedback(String bmlId, String syncId, double globalTime, String characterId)
    {
        this.bmlId = bmlId;
        this.syncId = syncId;
        this.globalTime = globalTime;
        this.characterId = characterId;
    }
    
    public void setCharacterId(String characterId)
    {
        this.characterId = characterId;
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        String id = getRequiredAttribute("id", attrMap, tokenizer);
        String[] idSplit = id.split(":");
        bmlId = idSplit[0];
        syncId = idSplit[1];
        globalTime = getRequiredFloatAttribute("globalTime", attrMap, tokenizer);
        characterId = getOptionalAttribute("characterId", attrMap);        
    }
    
    @Override
    public StringBuilder appendAttributes(StringBuilder buf)
    {
        if(characterId!=null)
        {
            appendAttribute(buf, "characterId", characterId);
        }
        appendAttribute(buf, "id", bmlId+":"+syncId);
        appendAttribute(buf, "globalTime", globalTime);
        return buf;
    }

    
    public String getBmlId()
    {
        return bmlId;
    }

    public String getSyncId()
    {
        return syncId;
    }

    public double getGlobalTime()
    {
        return globalTime;
    }

    public String getCharacterId()
    {
        return characterId;
    }

    /**
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "blockProgress";

    /**
     * The XML Stag for XML encoding -- use this static method when you want to
     * see if a given String equals the xml tag for this class
     */
    public static String xmlTag()
    {
        return XMLTAG;
    }

    /**
     * The XML Stag for XML encoding -- use this method to find out the run-time
     * xml tag of an object
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
