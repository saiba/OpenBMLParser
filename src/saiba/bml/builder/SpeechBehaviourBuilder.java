package saiba.bml.builder;

import saiba.bml.core.Behaviour;


/**
 * Builds a SpeechBehaviour
 * @author hvanwelbergen
 */
public class SpeechBehaviourBuilder
{
    private final BehaviourBuilder builder;
    
    public SpeechBehaviourBuilder(String bmlId, String id)
    {
        this.builder = new BehaviourBuilder("speech", bmlId,id);
        builder.content("<text></text>");        
    }
    
    public SpeechBehaviourBuilder content(String content)
    {
        builder.content("<text>"+content+"</text>");
        return this;
    }  
    
    public Behaviour build()
    {
        return builder.build();
    }
}
