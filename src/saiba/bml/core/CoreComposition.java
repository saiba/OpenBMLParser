package saiba.bml.core;
/**
 * Standard BML block compositions:<br>
 * REPLACE: remove all previous behavior, start instantly<br>
 * MERGE: merge with previous behavior, start instantly<br>
 * APPEND: start when all previously sent behavior is finished<br>
 * @author welberge
 */
public enum CoreComposition implements BMLBlockComposition
{
    UNKNOWN
    {
        @Override
        public String getNameStart()
        {
            return "UNKNOWN";
        }
    },
    REPLACE
    {
        @Override
        public String getNameStart()
        {
            return "REPLACE";
        }
    }, 
    MERGE
    {
        @Override
        public String getNameStart()
        {
            return "MERGE";
        }
    }, 
    APPEND
    {
        @Override
        public String getNameStart()
        {
            return "APPEND";
        }
    };
    
    public static CoreComposition parse(String input)
    {
        for(CoreComposition mech: CoreComposition.values())
        {
            if(mech.getNameStart().equals(input))
            {
                return mech;
            }
        }
        return UNKNOWN;
    }    
}
