package saiba.bml.builder;

import saiba.bml.core.Behaviour;
import saiba.bml.core.OffsetDirection;

/**
 * builds gazebehaviours
 * @author Herwin
 */
public class GazeBehaviourBuilder
{
    private final BehaviourBuilder builder;

    public GazeBehaviourBuilder(String bmlId, String id, String target)
    {
        this.builder = new BehaviourBuilder("gaze", bmlId, id);
        builder.param("target", target);
    }
    
    public GazeBehaviourBuilder influence(String influence)
    {
        builder.param("influence",""+influence);
        return this;
    }
    
    public GazeBehaviourBuilder offset(OffsetDirection dir, float degrees)
    {
        builder.param("offsetDirection",""+dir);
        builder.param("offsetAngle",""+degrees);
        return this;
    } 
    
    public Behaviour build()
    {
        return builder.build();
    }
}
