package saiba.bml.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

/**
 * Test utilities for BMLParser testing
 * @author hvanwelbergen
 *
 */
public final class ParserTestUtil
{
    private ParserTestUtil(){}
    
    public static boolean syncMatch(ExpectedSync es, SyncPoint as)
    {
        if (es.bmlId != null)
        {
            if (!es.bmlId.equals(as.getBmlId()))
                return false;
        }

        if (es.name == null && as.getName() != null)
            return false;
        if (as.getName() == null && es.name != null)
            return false;
        if (as.getName() != null && es.name != null)
        {
            if (!es.name.equals(as.getName()))
                return false;
        }
        
        if (es.offset != as.offset)
            return false;
        else if (es.behaviorName == null && as.getBehaviourId() == null)
            return true;
        if (es.behaviorName == null)
            return false;
        if (as.getBehaviourId() == null)
            return false;
        if (!es.behaviorName.equals(as.getBehaviourId()))
            return false; // findbugs complains, but
                          // this is not a bug
        return true;
    }

    private static boolean syncListMatch(List<ExpectedSync> ecList, List<SyncPoint> acList)
    {
        if (ecList.size() != acList.size())
            return false;

        for (ExpectedSync es : ecList)
        {
            boolean matches = false;
            for (SyncPoint as : acList)
            {
                if (syncMatch(es, as))
                {
                    matches = true;
                    break;
                }
            }
            if (!matches)
                return false;
        }
        return true;
    }
    
    public static  boolean constraintMatch(ExpectedAfterConstraint ec, AfterConstraint ac)
    {
        if(!syncListMatch(ec.expectedSync, ac.getTargets()))return false;
        return (syncMatch(ec.expectedRef,ac.getRef()));
    }
    
    public static  boolean constraintMatch(ExpectedConstraint ec, Constraint ac)
    {
        return syncListMatch(ec.expectedSyncs, ac.getTargets());
    }

    public static  void assertEqualAfterConstraints(List<ExpectedAfterConstraint> expected, List<AfterConstraint> actual)
    {
        assertEquals("Size of actual constraint list: " + actual + " does not match size of expected constraint list " + expected, expected.size(),
                actual.size());
        for (AfterConstraint ac : actual)
        {
            boolean matches = false;
            for (ExpectedAfterConstraint ec : expected)
            {
                if (constraintMatch(ec, ac))
                {
                    matches = true;
                    break;
                }
            }
            assertTrue("Constraint " + ac + " is not matched by any of the expected constraints " + expected, matches);
        }
    }
    
    public static  void assertEqualConstraints(List<ExpectedConstraint> expected, List<Constraint> actual)
    {
        assertEquals("Size of actual constraint list: " + actual + " does not match size of expected constraint list " + expected, expected.size(),
                actual.size());
        for (Constraint ac : actual)
        {
            boolean matches = false;
            for (ExpectedConstraint ec : expected)
            {
                if (constraintMatch(ec, ac))
                {
                    matches = true;
                    break;
                }
            }
            assertTrue("Constraint " + ac + " is not matched by any of the expected constraints " + expected, matches);
        }
    }
}
