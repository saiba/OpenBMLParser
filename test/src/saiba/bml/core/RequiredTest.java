package saiba.bml.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import org.hamcrest.Matchers;
import org.junit.Test;

import saiba.utils.TestUtil;

/**
 * Unit test cases for the required block
 * @author welberge
 * 
 */
public class RequiredTest
{
    private RequiredBlock reqBlock = new RequiredBlock("bml1");

    @Test
    public void testEmptyRequired()
    {
        reqBlock.readXML("<required"+TestUtil.getDefNS()+"/>");
        assertThat(reqBlock.behaviours, Matchers.<Behaviour> empty());
        assertThat(reqBlock.constraintBlocks, Matchers.<ConstraintBlock> empty());
    }

    @Test
    public void testRequiredBehaviors()
    {
        reqBlock.readXML("<required"+TestUtil.getDefNS()+">" + "<gaze id=\"gaze1\" type=\"AT\" target=\"person1\"/>"
                + "<gaze id=\"gaze2\" type=\"AT\" target=\"person1\"/>" + "</required>");
        assertThat(reqBlock.behaviours, hasSize(2));
        assertThat(reqBlock.constraintBlocks, Matchers.<ConstraintBlock> empty());
    }

    @Test
    public void testRequiredConstraints()
    {
        reqBlock.readXML("<required"+TestUtil.getDefNS()+">" + "<constraint id=\"c1\"/>" + "<constraint id=\"c2\"><synchronize ref=\"beh3:start\">"
                + "<sync ref=\"beh1:start\"/><sync ref=\"beh2:start\"/>" + "</synchronize></constraint>" + "</required>");
        assertThat(reqBlock.behaviours, Matchers.<Behaviour> empty());
        assertThat(reqBlock.constraintBlocks, hasSize(2));
    }

    @Test
    public void testRequiredConstraintsAndBehaviors()
    {
        reqBlock.readXML("<required"+TestUtil.getDefNS()+">" + "<constraint id=\"c1\"/>" + "<gaze id=\"gaze1\" type=\"AT\" target=\"person1\"/>"
                + "<constraint id=\"c2\"><synchronize ref=\"beh3:start\">" + "<sync ref=\"beh1:start\"/><sync ref=\"beh2:start\"/>"
                + "</synchronize></constraint>" + "<gaze id=\"gaze1\" type=\"AT\" target=\"person1\"/>" + "</required>");
        assertThat(reqBlock.constraintBlocks, hasSize(2));
        assertThat(reqBlock.behaviours, hasSize(2));
    }
}
