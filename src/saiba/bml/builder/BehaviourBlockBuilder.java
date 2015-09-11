package saiba.bml.builder;

import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import saiba.bml.core.BMLBehaviorAttributeExtension;
import saiba.bml.core.BMLBlockComposition;
import saiba.bml.core.Behaviour;
import saiba.bml.core.BehaviourBlock;
import saiba.bml.core.ConstraintBlock;
import saiba.bml.core.CoreComposition;
import saiba.bml.core.Mode;
import saiba.bml.core.OffsetDirection;
import saiba.bml.core.SpeechBehaviour;
import saiba.bml.core.Synchronize;
import saiba.bml.core.ext.FaceFacsBehaviour.Side;

/**
 * Builder for BehaviourBlocks
 * @author hvanwelbergen
 * 
 */
public class BehaviourBlockBuilder
{
    private List<Behaviour> behaviours = new ArrayList<Behaviour>();
    private List<ConstraintBlock> constraints = new ArrayList<ConstraintBlock>();
    private List<BMLBehaviorAttributeExtension> extensions = new ArrayList<BMLBehaviorAttributeExtension>();
    
    private String id = generateUniqueIdBML("bml");
    private String characterId = null;
    private BMLBlockComposition composition = CoreComposition.MERGE;

    private String generateUniqueIdBML(String prefix)
    {
        return prefix + UUID.randomUUID().toString().replaceAll("-", "");
    }

    public BehaviourBlockBuilder()
    {

    }
    
    /**
     * Creates an empty BML block with a REPLACE composition and a unique id
     */
    public static BehaviourBlock resetBlock()
    {
        return new BehaviourBlockBuilder().setComposition(CoreComposition.REPLACE).build();        
    }
    
    /**
     * Creates an empty BML block with a REPLACE composition and id bmlId     
     */
    public static BehaviourBlock resetBlock(String bmlId)
    {
        return new BehaviourBlockBuilder().id(bmlId).setComposition(CoreComposition.REPLACE).build();        
    }
    
    public BehaviourBlock build()
    {
        BehaviourBlock bb = new BehaviourBlock();        
        bb.addAllBMLBehaviorAttributeExtensions(extensions);        
        bb.setBmlId(id);
        bb.setCharacterId(characterId);
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
    
    public BehaviourBlockBuilder characterId(String characterId)
    {
        this.characterId = characterId;
        return this;
    }


    public BehaviourBlockBuilder addFaceLexemeBehaviour(String behId, String lexeme)
    {
        return addBehaviour(new FaceLexemeBehaviourBuilder(id, behId, lexeme).build());
    }
    
    public BehaviourBlockBuilder addFaceLexemeBehaviour(String behId, String lexeme, float amount)
    {
        return addBehaviour(new FaceLexemeBehaviourBuilder(id, behId, lexeme).amount(amount).build());
    }
    
    public BehaviourBlockBuilder addFaceFacsBehaviour(String behId, int au)
    {
        return addBehaviour(new FaceFacsBehaviourBuilder(id, behId, au).build());
    }
    
    public BehaviourBlockBuilder addFaceFacsBehaviour(String behId, int au, Side side, float amount)
    {
        return addBehaviour(new FaceFacsBehaviourBuilder(id, behId, au).side(side).amount(amount).build());
    }
    
    public BehaviourBlockBuilder addHeadBehaviour(String behId, String lexeme)
    {
        return addBehaviour(new HeadBehaviourBuilder(id, behId, lexeme).build());
    }
    
    public BehaviourBlockBuilder addHeadBehaviour(String behId, String lexeme, float amount)
    {
        return addBehaviour(new HeadBehaviourBuilder(id, behId, lexeme).amount(amount).build());
    }
    
    public BehaviourBlockBuilder addGazeBehaviour(String behId, String target)
    {
        return addBehaviour(new GazeBehaviourBuilder(id, behId, target).build());
    }
    
    public BehaviourBlockBuilder addGazeBehaviour(String behId, String target, String influence)
    {
        return addBehaviour(new GazeBehaviourBuilder(id, behId, target).influence(influence).build());
    }
    
    public BehaviourBlockBuilder addGazeBehaviour(String behId, String target, String influence, OffsetDirection dir, float offsetDegrees)
    {
        return addBehaviour(new GazeBehaviourBuilder(id, behId, target).influence(influence).offset(dir, offsetDegrees).build());
    }
    
    public BehaviourBlockBuilder addGazeShiftBehaviour(String behId, String target)
    {
        return addBehaviour(new GazeShiftBehaviourBuilder(id, behId, target).build());
    }
    
    public BehaviourBlockBuilder addGazeShiftBehaviour(String behId, String target, String influence)
    {
        return addBehaviour(new GazeShiftBehaviourBuilder(id, behId, target).influence(influence).build());
    }
    
    public BehaviourBlockBuilder addGazeShiftBehaviour(String behId, String target, String influence, OffsetDirection dir, float offsetDegrees)
    {
        return addBehaviour(new GazeShiftBehaviourBuilder(id, behId, target).influence(influence).offset(dir, offsetDegrees).build());
    }
    
    public BehaviourBlockBuilder addGestureBehaviour(String behId, String lexeme)
    {
        return addBehaviour(new GestureBehaviourBuilder(id, behId, lexeme).build());
    }
    
    public BehaviourBlockBuilder addGestureBehaviour(String behId, String lexeme, Mode mode)
    {
        return addBehaviour(new GestureBehaviourBuilder(id, behId, lexeme).mode(mode).build());
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
    
    public BehaviourBlockBuilder addBMLBehaviorAttributeExtension(BMLBehaviorAttributeExtension ext)
    {
        extensions.add(ext);
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

    public BehaviourBlockBuilder addAtConstraint(String ref1, String ref2)
    {
        ConstraintBlock cb = new ConstraintBlock(id);        
        StringBuffer buf = new StringBuffer("<synchronize "+" xmlns=\"" + BehaviourBlock.BMLNAMESPACE+ "\">");        
        buf.append("<sync ref=\""+ref1+"\"/>");
        buf.append("<sync ref=\""+ref2+"\"/>");
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
    
    public BehaviourBlockBuilder addAtConstraint(String beh1, String sync1, String beh2, String sync2)
    {
        return addAtConstraint(beh1+":"+sync1, beh2+":"+sync2);
    }
    
    public BehaviourBlockBuilder addAtConstraint(String beh1, String sync1, double blockOffset)
    {
        return addAtConstraint(beh1+":"+sync1, ""+blockOffset);
    }
}
