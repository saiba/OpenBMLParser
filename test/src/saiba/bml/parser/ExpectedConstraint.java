package saiba.bml.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Storage for an expected constraint
 * @author hvanwelbergen
 *
 */
public class ExpectedConstraint
{
    public List<ExpectedSync> expectedSyncs = new ArrayList<ExpectedSync>();

    @Override
    public String toString()
    {
        return expectedSyncs.toString();
    }
}
