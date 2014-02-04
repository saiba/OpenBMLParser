package saiba.bml.feedback;

import lombok.Delegate;
import saiba.bml.core.CustomAttributeHandler;
import hmi.xml.XMLStructureAdapter;

/**
 * Skeleton class for BMLFeedback
 * @author herwinvw
 *
 */
public class AbstractBMLFeedback extends XMLStructureAdapter implements BMLFeedback
{
    @Delegate
    protected CustomAttributeHandler caHandler = new CustomAttributeHandler();

    public static final double UNKNOWN_TIME = -Double.MAX_VALUE;
}
