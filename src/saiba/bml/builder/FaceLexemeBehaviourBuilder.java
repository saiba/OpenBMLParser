package saiba.bml.builder;

import saiba.bml.core.Behaviour;

/**
 * Builds a FaceLexemeBehaviour
 * @author hvanwelbergen
 * 
 */
public class FaceLexemeBehaviourBuilder
{
    private final BehaviourBuilder builder;

    public FaceLexemeBehaviourBuilder(String bmlId, String id, String lexeme)
    {
        this.builder = new BehaviourBuilder("faceLexeme", bmlId, id);
        builder.param("lexeme", lexeme);
    }
    
    public FaceLexemeBehaviourBuilder amount(float amount)
    {
        builder.param("amount",""+amount);
        return this;
    } 
    
    public Behaviour build()
    {
        return builder.build();
    }
}
