package saiba.bml.builder;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import saiba.bml.core.BehaviourBlock;
import saiba.bml.core.CoreComposition;
import saiba.bml.core.SpeechBehaviour;
import saiba.bml.parser.BMLParser;

/**
 * Unit tests for the BehaviourBlockBuilder
 * @author hvanwelbergen
 * 
 */
public class BehaviourBlockBuilderTest
{
    private static final double PARAM_PRECISION = 0.001;
    private BehaviourBlockBuilder builder = new BehaviourBlockBuilder();
    
    @Test
    public void buildEmptyBlock()
    {
        BehaviourBlock bb = builder.build();
        assertNotNull(bb.id);
        assertEquals(CoreComposition.MERGE, bb.getComposition());
    }
    
    @Test
    public void checkDefaultCompositionOutput()
    {
        BehaviourBlock bbOut = builder.build();
        StringBuilder buf = new StringBuilder();
        bbOut.appendXML(buf);
        BehaviourBlock bbIn = new BehaviourBlock();
        bbIn.readXML(buf.toString());
        BMLParser parser = new BMLParser();
        parser.addBehaviourBlock(bbIn);
        assertEquals(CoreComposition.MERGE, bbIn.getComposition());
    }

    //TODO
    /*
    @Test
    public void checkCustomCompositionOutput()
    {
        BehaviourBlock bbOut = builder.setComposition().build();
        StringBuilder buf = new StringBuilder();
        bbOut.appendXML(buf);
        BehaviourBlock bbIn = new BehaviourBlock();
        bbIn.readXML(buf.toString());
        BMLParser parser = new BMLParser();
        parser.addBehaviourBlock(bbIn);
        assertEquals(CoreComposition.MERGE, bbIn.getComposition());
    }
    */
    
    @Test
    public void buildBlockWithId()
    {
        BehaviourBlock bb = builder.id("bml1").build();
        assertEquals("bml1", bb.id);
    }

    @Test
    public void buildBlockWithIdPrefix()
    {
        BehaviourBlock bb = builder.uniqueIdWithPrefix("bml1").build();
        assertThat(bb.id, startsWith("bml1"));
    }

    @Test
    public void buildBlockWithBehaviour()
    {
        //@formatter:off
        BehaviourBlock bb = builder
            .id("bml1")
            .addBehaviour(
                    new BehaviourBuilder("head","bml1","h1")
                    .param("lexeme","nod")
                    .build())
            .build();
        //@formatter:on
        assertEquals("h1", bb.behaviours.get(0).id);
        assertEquals("nod", bb.behaviours.get(0).getStringParameterValue("lexeme"));
        assertEquals("bml1", bb.behaviours.get(0).getBmlId());

        StringBuilder buf = new StringBuilder();
        bb.appendXML(buf);
        System.out.println(buf.toString());
    }

    public void buildBlockWithHeadBehaviour()
    {
        //@formatter:off
        BehaviourBlock bb = 
                builder.id("bml1")
                       .addBehaviour(builder.createHeadBehaviourBuilder("h1", "NOD").amount(0.7f).build())
                       .build();
        //@formatter:on   
        assertEquals("h1", bb.behaviours.get(0).id);
        assertEquals("nod", bb.behaviours.get(0).getStringParameterValue("lexeme"));
        assertEquals("bml1", bb.behaviours.get(0).getBmlId());
        assertEquals(0.7f, bb.behaviours.get(0).getFloatParameterValue("amount"), PARAM_PRECISION);
    }

    @Test
    public void buildBlockWithSpeechBehaviour()
    {
        //@formatter:off
        BehaviourBlock bb = builder
            .id("bml1")
            .addSpeechBehaviour("speech1", "Hello world")
            .build();
        //@formatter:on        
        assertEquals("speech1", bb.behaviours.get(0).id);
        assertEquals("bml1", bb.behaviours.get(0).getBmlId());
        assertEquals("Hello world", ((SpeechBehaviour) bb.behaviours.get(0)).getContent());
    }

    @Test
    public void buildBlockWithConstraint()
    {
        //@formatter:off
        BehaviourBlock bb = builder
                .id("bml1")
                .addSpeechBehaviour("speech1", "Hello world")
                .addBehaviour(builder.createHeadBehaviourBuilder("h1", "NOD").build())
                .addAtConstraint("speech1","start","h1","end")
                .build();
        //@formatter:on
        BMLParser parser = new BMLParser();
        parser.addBehaviourBlock(bb);
        assertEquals(1, parser.getConstraints().size());
        assertEquals("start",parser.getConstraints().get(0).getTargets().get(0).getName());
        assertEquals("end",parser.getConstraints().get(0).getTargets().get(1).getName());
        assertEquals("speech1",parser.getConstraints().get(0).getTargets().get(0).getBehaviourId());
        assertEquals("h1",parser.getConstraints().get(0).getTargets().get(1).getBehaviourId());
    }
}
