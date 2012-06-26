package saiba.bml.feedback;

import java.util.HashMap;

import saiba.bml.parser.InvalidSyncRefException;
import saiba.bml.parser.SyncRef;
import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

/**
 * Sync point progress feedback XML
 * @author Herwin
 */
public class BMLSyncPointProgressFeedback extends XMLStructureAdapter implements BMLFeedback
{
    private String characterId;
    public void setCharacterId(String characterId)
    {
        this.characterId = characterId;
    }

    private SyncRef syncRef;
    private double globalTime;
    private double time;
    
    public BMLSyncPointProgressFeedback()
    {
        
    }
    
    public BMLSyncPointProgressFeedback(String bmlId, String behaviorId, String syncId, double time, double globalTime)
    {
        try
        {
            syncRef = new SyncRef(bmlId,bmlId+":"+behaviorId+":"+syncId);
        }
        catch (InvalidSyncRefException e)
        {
            throw new RuntimeException(e);
        }
        this.globalTime = globalTime;
        this.time = time;
    }
    
    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        characterId = getOptionalAttribute("characterId", attrMap);
        String id = getRequiredAttribute("id", attrMap, tokenizer);
        try
        {
            syncRef = new SyncRef("",id);
        }
        catch (InvalidSyncRefException e)
        {
            throw new XMLScanException("error parsing id", e);
        }
        globalTime = getRequiredFloatAttribute("globalTime", attrMap, tokenizer);
        time = getRequiredFloatAttribute("time", attrMap, tokenizer);
    }
    
    @Override
    public StringBuilder appendAttributes(StringBuilder buf)
    {
        if(characterId!=null && characterId.length()>0)
        {
            appendAttribute(buf, "characterId", characterId);
        }
        appendAttribute(buf, "id", syncRef.toString());
        appendAttribute(buf, "time", time);
        appendAttribute(buf, "globalTime", globalTime);
        return buf;
    }
    public String getBMLId()
    {
        return syncRef.bbId;
    }
    
    public String getBehaviourId()
    {
        return syncRef.sourceId;
    }
    
    public String getSyncId()
    {
        return syncRef.syncId;
    }
    
    public String getCharacterId()
    {
        return characterId;
    }

    public SyncRef getSyncRef()
    {
        return syncRef;
    }

    public double getGlobalTime()
    {
        return globalTime;
    }

    public double getTime()
    {
        return time;
    }

    /**
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "syncPointProgress";

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
