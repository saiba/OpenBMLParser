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
