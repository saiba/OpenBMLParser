package saiba.bml.builder;

import saiba.bml.core.Behaviour;
import saiba.bml.core.OffsetDirection;

/**
 * builds gazebehaviours
 * @author Herwin
 */
public class GazeShiftBehaviourBuilder
{
    private final BehaviourBuilder builder;

    public GazeShiftBehaviourBuilder(String bmlId, String id, String target)
    {
        this.builder = new BehaviourBuilder("gazeShift", bmlId, id);
        builder.param("target", target);
    }
    
    public GazeShiftBehaviourBuilder influence(String influence)
    {
        builder.param("influence",""+influence);
        return this;
    }
    
    public GazeShiftBehaviourBuilder offset(OffsetDirection dir, float degrees)
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
