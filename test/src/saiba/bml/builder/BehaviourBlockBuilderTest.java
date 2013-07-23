package saiba.bml.builder;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import hmi.xml.XMLFormatting;
import hmi.xml.XMLNameSpace;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import org.junit.Test;

import saiba.bml.core.BMLBehaviorAttributeExtension;
import saiba.bml.core.BMLBlockComposition;
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

    @Test
    public void checkCustomCompositionOutput()
    {
        BehaviourBlock bbOut = builder.setComposition(CoreComposition.REPLACE).build();
        StringBuilder buf = new StringBuilder();
        bbOut.appendXML(buf);
        BehaviourBlock bbIn = new BehaviourBlock();
        bbIn.readXML(buf.toString());
        BMLParser parser = new BMLParser();
        parser.addBehaviourBlock(bbIn);
        assertEquals(CoreComposition.REPLACE, bbIn.getComposition());
    }

    @Test
    public void addCustomAttribute()
    {
        class MyBMLBehaviorAttributeExtension implements BMLBehaviorAttributeExtension
        {
            @Getter @Setter
            private String testVal;
            
            @Override
            public void decodeAttributes(BehaviourBlock behavior, HashMap<String, String> attrMap, XMLTokenizer tokenizer)
            {
                testVal = attrMap.get("http://mynamespace.net:test");
            }

            //TODO: move to HmiXML
            private void constructNSPrefix(String ns, XMLFormatting fmt, StringBuilder buf)
            {
                if(fmt.getNamespacePrefix(ns.intern())==null)
                {
                    String prefix = "ns"+UUID.randomUUID().toString();
                    XMLNameSpace n = new XMLNameSpace(prefix,ns);
                    fmt.pushXMLNameSpace(n);
                    buf.append(" xmlns:"+prefix+"=\""+ns+"\" ");
                }
            }
            
            @Override
            public StringBuilder appendAttributeString(StringBuilder buf,XMLFormatting fmt)
            {
                constructNSPrefix("http://mynamespace.net", fmt, buf);
                XMLStructureAdapter.appendNamespacedAttribute(buf, fmt, "http://mynamespace.net", "test", testVal);
                return buf;
            }

            @Override
            public BMLBlockComposition handleComposition(String sm)
            {
                return null;
            }

            @Override
            public Set<String> getOtherBlockDependencies()
            {
                return new HashSet<String>();
            }

        }
        MyBMLBehaviorAttributeExtension ext = new MyBMLBehaviorAttributeExtension();
        ext.setTestVal("test");
        BehaviourBlock bb = builder.addBMLBehaviorAttributeExtension(ext).build();
        assertEquals(ext, bb.getBMLBehaviorAttributeExtension(MyBMLBehaviorAttributeExtension.class));
        
        BehaviourBlock bbOut = new BehaviourBlock();
        bbOut.addBMLBehaviorAttributeExtension(new MyBMLBehaviorAttributeExtension());
        bbOut.readXML(bb.toBMLString());        
    }

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
        assertEquals("start", parser.getConstraints().get(0).getTargets().get(0).getName());
        assertEquals("end", parser.getConstraints().get(0).getTargets().get(1).getName());
        assertEquals("speech1", parser.getConstraints().get(0).getTargets().get(0).getBehaviourId());
        assertEquals("h1", parser.getConstraints().get(0).getTargets().get(1).getBehaviourId());
    }
}
