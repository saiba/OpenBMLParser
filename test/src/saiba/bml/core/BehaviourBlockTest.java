package saiba.bml.core;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLScanException;

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

    @Test(timeout = 300)
    public void testCharacterId()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.readXML("<bml id=\"bml1\" characterId=\"Alice\"" + " xmlns=\"" + BehaviourBlock.BMLNAMESPACE +"\"/>");
        assertEquals("Alice",bb.getCharacterId());
    }
    
    @Test(timeout = 300, expected = XMLScanException.class)
    public void testInvalidAttribute()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.readXML("<bml id=\"bml1\" bmla:appendAfter=\"bml1\" " + "xmlns=\"" + BehaviourBlock.BMLNAMESPACE + "\"/>");
    }

    @Test(timeout = 300, expected = XMLScanException.class)
    public void testUnregisteredBehaviour()
    {
        String beh = "<bml id=\"bml1\" xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" xmlns:pe=\"http://hmi.ewi.utwente.nl/pictureengine\">"
                + "<pe:invalidbeh/>" + "</bml>";
        BehaviourBlock bb = new BehaviourBlock();
        bb.readXML(beh);
    }
}
