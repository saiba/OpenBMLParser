package saiba.bml.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * After constraint
 * @author Herwin
 */
public class AfterConstraint
{
    private SyncPoint ref;
    public SyncPoint getRef()
    {
        return ref;
    }

    private List<SyncPoint> targets = new ArrayList<SyncPoint>();
    
    public void addTarget(SyncPoint target)
    {
        targets.add(target);
    }
    
    public AfterConstraint(SyncPoint ref)
    {
        this.ref = ref;
    }
    
    public List<SyncPoint> getTargets()
    {
        return targets;
    }
    
    @Override
    public String toString()
    {
        StringBuffer retval = new StringBuffer();
        retval.append("Reference: ");
        retval.append(ref);
        retval.append(", Targets: ");
        for (SyncPoint target : targets)
        {
            retval.append(target.toString() + ", ");
        }
        retval.delete(retval.length() - 2, retval.length());

        return retval.toString();
    }
}
