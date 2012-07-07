package saiba.bml.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Storage for an expected after constraint
 * @author hvanwelbergen 
 */
public class ExpectedAfterConstraint
{
    public List<ExpectedSync> expectedSync = new ArrayList<ExpectedSync>();
    public ExpectedSync expectedRef;  
    
    @Override
    public String toString()
    {
        return "Reference: "+expectedRef.toString()+",targets: "+expectedSync.toString();
    }
}
