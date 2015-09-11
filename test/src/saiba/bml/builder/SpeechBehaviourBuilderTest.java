package saiba.bml.builder;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import saiba.bml.core.Behaviour;
import saiba.bml.core.SpeechBehaviour;

/**
 * Unit test for the SpeechBehaviourBuilder
 * @author hvanwelbergen
 *
 */
public class SpeechBehaviourBuilderTest
{
    @Test
    public void testWithContent()
    {
        SpeechBehaviourBuilder sbb = new SpeechBehaviourBuilder("bml1","speech1");
        Behaviour beh = sbb.content("Hello world").build();
        assertThat(beh, instanceOf(SpeechBehaviour.class));
        SpeechBehaviour speechBeh = (SpeechBehaviour)beh;
        assertEquals("Hello world",speechBeh.getContent());        
    }
    
    @Test
    public void testNoContent()
    {
        SpeechBehaviourBuilder sbb = new SpeechBehaviourBuilder("bml1","speech1");
        Behaviour beh = sbb.build();
        assertThat(beh, instanceOf(SpeechBehaviour.class));
        SpeechBehaviour speechBeh = (SpeechBehaviour)beh;
        assertEquals("",speechBeh.getContent());        
    }
}
