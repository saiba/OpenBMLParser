package saiba.bml.builder;

import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import saiba.bml.core.BMLBlockComposition;
import saiba.bml.core.Behaviour;
import saiba.bml.core.BehaviourBlock;
import saiba.bml.core.ConstraintBlock;
import saiba.bml.core.CoreComposition;
import saiba.bml.core.SpeechBehaviour;
import saiba.bml.core.Synchronize;

/**
 * Builder for BehaviourBlocks
 * @author hvanwelbergen
 * 
 */
public class BehaviourBlockBuilder
{
    private List<Behaviour> behaviours = new ArrayList<Behaviour>();
    private List<ConstraintBlock> constraints = new ArrayList<ConstraintBlock>();

    private String id = generateUniqueIdBML("bml");
    private BMLBlockComposition composition = CoreComposition.MERGE;

    private String generateUniqueIdBML(String prefix)
    {
        return prefix + UUID.randomUUID().toString().replaceAll("-", "");
    }

    public BehaviourBlockBuilder()
    {

    }

    public BehaviourBlock build()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.setBmlId(id);
        bb.setComposition(composition);
        bb.behaviours.addAll(behaviours);
        bb.constraintBlocks.addAll(constraints);
        return bb;
    }

    public BehaviourBlockBuilder id(String id)
    {
        this.id = id;
        return this;
    }

    public HeadBehaviourBuilder createHeadBehaviourBuilder(String behid, String lexeme)
    {
        return new HeadBehaviourBuilder(id, behid, lexeme);
    }

    public BehaviourBlockBuilder uniqueIdWithPrefix(String prefix)
    {
        id = generateUniqueIdBML(prefix);
        return this;
    }

    public BehaviourBlockBuilder setComposition(BMLBlockComposition c)
    {
        composition = c;
        return this;
    }
    
    public BehaviourBlockBuilder addBehaviour(Behaviour b)
    {
        behaviours.add(b);
        return this;
    }

    public BehaviourBlockBuilder addSpeechBehaviour(String behid, String content)
    {
        try
        {
            behaviours.add(new SpeechBehaviour(id, new XMLTokenizer("<speech xmlns=\"" + BehaviourBlock.BMLNAMESPACE + "\" id=\"" + behid
                    + "\"><text>" + content + "</text></speech>")));
        }
        catch (IOException e)
        {
            throw new AssertionError();
        }
        return this;
    }

    public BehaviourBlockBuilder addAtConstraint(String beh1, String sync1, String beh2, String sync2)
    {
        ConstraintBlock cb = new ConstraintBlock(id);        
        StringBuffer buf = new StringBuffer("<synchronize "+" xmlns=\"" + BehaviourBlock.BMLNAMESPACE+ "\">");
        buf.append("<sync ref=\""+beh1+":"+sync1+"\"/>");
        buf.append("<sync ref=\""+beh2+":"+sync2+"\"/>");
        buf.append("</synchronize>");
        try
        {
            cb.synchronizes.add(new Synchronize(id, new XMLTokenizer(buf.toString())));
        }
        catch (IOException e)
        {
            throw new AssertionError();
        }        
        constraints.add(cb);
        return this;
    }
}
