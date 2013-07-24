package saiba.bml.builder;

import saiba.bml.core.Behaviour;
import saiba.bml.core.ext.FaceFacsBehaviour;
import saiba.bml.core.ext.FaceFacsBehaviour.Side;

/**
 * Builds FaceFacsBehaviours
 * @author Herwin
 * 
 */
public class FaceFacsBehaviourBuilder
{
    private final BehaviourBuilder builder;

    public FaceFacsBehaviourBuilder(String bmlId, String id, int au)
    {
        this.builder = new BehaviourBuilder("faceFacs", bmlId, id).namespace(FaceFacsBehaviour.BMLEXTNAMESPACE);
        builder.param("au", "" + au);
    }

    public FaceFacsBehaviourBuilder amount(float amount)
    {
        builder.param("amount", "" + amount);
        return this;
    }

    public FaceFacsBehaviourBuilder side(Side side)
    {
        builder.param("side", side.toString());
        return this;
    }

    public Behaviour build()
    {
        return builder.build();
    }
}
