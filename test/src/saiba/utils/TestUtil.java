package saiba.utils;

import saiba.bml.core.BMLElement;

/**
 * Test utility functions
 * @author Herwin
 */
public final class TestUtil
{
    private TestUtil()
    {
    }
    
    public static final String getDefNS()
    {
        return " xmlns=\""+BMLElement.BMLNAMESPACE+"\" ";
    }
}
