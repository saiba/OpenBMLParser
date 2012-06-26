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
