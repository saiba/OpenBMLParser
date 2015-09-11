/*******************************************************************************
 * Copyright (c) 2013 University of Twente, Bielefeld University
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package saiba.bml.core;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;
import java.util.Set;

/**
 * This interface can be used to enhance the &ltbml&gt tag with external elements and provides 
 * BML extensions with a way to specify new scheduling mechanisms.
 * BMLBehaviorAttributeExtension are to be registered with each BMLBehaviorBlock. 
 * The BMLBehaviorBlock automatically calls decodeAttribute on all its registered
 * BMLBehaviorAttributeExtensions.  
 * 
 * @see BMLBehaviorBlock 
 * @author hvanwelbergen
 */
public interface BMLBehaviorAttributeExtension
{
    /**
     * Decodes the attributes in attrMap. Once decoded, they should be removed from attrMap. The easiest way to do this is using
     * bb.getRequiredAttribute, bb.getOptionalAttribute, etc.
     * 
     */
    void decodeAttributes(BehaviourBlock bb, HashMap<String, String> attrMap, XMLTokenizer tokenizer);
    
    StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt);
    
    /**
     * Attempts to parse the composition mechanism described by sm
     * @return the composition is successfully parsed, CoreSchedulingMechanism.UNKOWN otherwise
     */
    BMLBlockComposition handleComposition(String sm);
    
    /**
     * Get the set of other blocks this block depends upon (e.g. is appended after, activates)
     */
    Set<String> getOtherBlockDependencies();
}
