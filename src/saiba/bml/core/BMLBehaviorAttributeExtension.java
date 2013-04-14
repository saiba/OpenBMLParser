package saiba.bml.core;

import hmi.xml.XMLTokenizer;

import java.util.HashMap;
import java.util.Set;

/**
 * This interface can be used to enhance the &ltbml&gt tag with external elements and provides 
 * BML extensions with a way to specify new scheduling mechanisms.
 * BMLBehaviorAttributeExtension are to be registered with each BMLBehaviorBlock. 
 * The BMLBehaviorBlock automatically calls decodeAttribute on all its registered
 * BMLBehaviorAttributeExtensions.  
 * 
 * @see BMLBehaviorBlock 
 * @author hvanwelbergen
 */
public interface BMLBehaviorAttributeExtension
{
    void decodeAttributes(BehaviourBlock behavior, HashMap<String, String> attrMap, XMLTokenizer tokenizer);
    
    /**
     * Attempts to parse the composition mechanism described by sm
     * @return the composition is successfully parsed, CoreSchedulingMechanism.UNKOWN otherwise
     */
    BMLBlockComposition handleComposition(String sm);
    
    /**
     * Get the set of other blocks this block depends upon (e.g. is appended after, activates)
     */
    Set<String> getOtherBlockDependencies();
}
