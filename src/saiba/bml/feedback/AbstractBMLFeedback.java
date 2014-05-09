package saiba.bml.feedback;

import java.util.List;

import com.google.common.collect.ImmutableList;

import lombok.Delegate;
import saiba.bml.core.CustomAttributeHandler;
import hmi.xml.XMLFormatting;
import hmi.xml.XMLNameSpace;
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
    
    public String toBMLFeedbackString(XMLNameSpace... xmlNamespaces)
    {
        return toBMLFeedbackString(ImmutableList.copyOf(xmlNamespaces));        
    }
    
    public String toBMLFeedbackString(List<XMLNameSpace> xmlNamespaceList)
    {
        StringBuilder buf = new StringBuilder();
        appendXML(buf, new XMLFormatting(), xmlNamespaceList);
        return buf.toString();
    }
}
