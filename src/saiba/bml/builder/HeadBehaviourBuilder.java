package saiba.bml.builder;

import saiba.bml.core.Behaviour;

/**
 * Builds a HeadBehaviour
 * @author hvanwelbergen
 *
 */
public class HeadBehaviourBuilder
{
    private final BehaviourBuilder builder;
    
    public HeadBehaviourBuilder(String bmlId, String id, String lexeme)
    {
        this.builder = new BehaviourBuilder("head", bmlId,id);
        builder.param("lexeme",lexeme);
    }
    
    public HeadBehaviourBuilder repetition(int repetition)
    {
        builder.param("repetition",""+repetition);
        return this;
    }
    
    public HeadBehaviourBuilder amount(float amount)
    {
        builder.param("amount",""+amount);
        return this;
    }    
    
    public Behaviour build()
    {
        return builder.build();
    }
}
