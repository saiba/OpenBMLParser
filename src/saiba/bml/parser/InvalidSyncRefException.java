/*******************************************************************************
 * Copyright (C) 2009 Human Media Interaction, University of Twente, the Netherlands
 * 
 * This file is part of the Elckerlyc BML realizer.
 * 
 * Elckerlyc is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Elckerlyc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Elckerlyc.  If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/
package saiba.bml.parser;

/**
 * Runtime exception thrown for invalid sync references
 * 
 * @author welberge
 */
public class InvalidSyncRefException extends Exception
{
    public InvalidSyncRefException(String s)
    {
        super(s);
    }

    public InvalidSyncRefException(String s, Throwable cause)
    {
        super(s, cause);
    }

    private static final long serialVersionUID = -1944448719873442719L;
}
