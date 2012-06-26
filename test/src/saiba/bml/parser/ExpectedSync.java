package saiba.bml.parser;

/**
 * Value class for an 'expected' syncpoint
 * @author hvanwelbergen
 *
 */
public class ExpectedSync
{
    public final String behaviorName;

    public final String name;

    public final double offset;
    public final String bmlId;

    public ExpectedSync(String bmlId, String behaviorName, String name, double offset)
    {
        this.bmlId = bmlId;
        this.behaviorName = behaviorName;
        this.name = name;
        this.offset = offset;
    }

    @Override
    public String toString()
    {
        return bmlId + ":" + behaviorName + ":" + name + ":" + offset;
    }

}
