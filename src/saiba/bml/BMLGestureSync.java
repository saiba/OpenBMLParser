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
package saiba.bml;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Standard BML synchronization points
 * @author welberge
 */
public enum BMLGestureSync
{
    START("start"), 
    READY("ready"), 
    STROKE_START("strokeStart"), 
    STROKE("stroke"), 
    STROKE_END("strokeEnd"), 
    RELAX("relax"), 
    END("end");
    
    private String id;
    private static Map<String,BMLGestureSync> idToSync = new HashMap<String,BMLGestureSync>();
    
    static 
    {
        for(BMLGestureSync bs : EnumSet.allOf(BMLGestureSync.class))
            idToSync.put(bs.getId(), bs);
    }

    public boolean isBefore(BMLGestureSync s)
    {
        if(ordinal()<s.ordinal())return true;
        return false;
    }
    
    public boolean isAfter(BMLGestureSync s)
    {
        if(ordinal()>s.ordinal())return true;
        return false;
    }
    
    public static boolean isBMLSync(String id)
    {
        return idToSync.get(id)!=null;
    }
    
    BMLGestureSync(String id)
    {
        this.id = id;        
    }
    
    public String getId()
    {
        return id;
    }
    
    public static BMLGestureSync get(String id) 
    { 
        return idToSync.get(id); 
    }
    
    @Override
    public String toString()
    {
        return id;
    }
}
