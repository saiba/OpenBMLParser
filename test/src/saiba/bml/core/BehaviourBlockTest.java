package saiba.bml.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saiba.bml.builder.BehaviourBlockBuilder;
import saiba.bml.parser.BMLParser;

/**
 * Unit tests for the BehaviourBlock
 * @author hvanwelbergen
 * 
 */
public class BehaviourBlockTest
{
    private BehaviourBlockBuilder builder = new BehaviourBlockBuilder();

    @Test
    public void testChangeId()
    {
        //@formatter:off
        BehaviourBlock bb = builder
                .id("bml1")
                .addSpeechBehaviour("speech1", "Hello world")
                .addHeadBehaviour("h1", "NOD")
                .addAtConstraint("speech1","start","h1","end")
                .build();
        //@formatter:on
        bb.setBmlId("bml2");
        assertEquals("bml2", bb.id);
        BMLParser parser = new BMLParser();
        parser.addBehaviourBlock(bb);
        assertEquals("bml2", parser.getBehaviourBlocks().get(0).id);
        assertEquals("bml2", parser.getBehaviours().get(0).bmlId);
        assertEquals("bml2", parser.getBehaviours().get(1).bmlId);
        assertEquals("bml2", parser.getConstraints().get(0).getTargets().get(0).getBmlId());
        assertEquals("bml2", parser.getConstraints().get(0).getTargets().get(1).getBmlId());
    }
}
