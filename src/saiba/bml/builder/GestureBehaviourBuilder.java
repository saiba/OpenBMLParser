package saiba.bml.builder;

import saiba.bml.core.Behaviour;
import saiba.bml.core.Mode;

/**
 * Builder for gesture behaviors
 * @author Herwin
 *
 */
public class GestureBehaviourBuilder
{
    private final BehaviourBuilder builder;
    
    public GestureBehaviourBuilder(String bmlId, String id, String lexeme)
    {
        this.builder = new BehaviourBuilder("gesture", bmlId,id);
        builder.param("lexeme",lexeme);
    }
    
    public GestureBehaviourBuilder mode(Mode mode)
    {
        builder.param("mode",mode.toString());
        return this;
    }
    
    public Behaviour build()
    {
        return builder.build();
    }
}
