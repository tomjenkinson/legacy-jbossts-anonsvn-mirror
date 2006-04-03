/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated 
 * by the @authors tag. All rights reserved. 
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors. 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
/*
 * Copyright (C) 1998, 1999, 2000,
 *
 * Arjuna Solutions Limited,
 * Newcastle upon Tyne,
 * Tyne and Wear,
 * UK.
 *
 * $Id: ConflictType.java 2342 2006-03-30 13:06:17Z  $
 */

package com.arjuna.ats.txoj;

import java.io.PrintWriter;

/**
 * The various types of lock conflict that can occur when
 * trying to set a lock.
 */

class ConflictType
{

    public static final int CONFLICT = 0;
    public static final int COMPATIBLE = 1;
    public static final int PRESENT = 2;

    public static String stringForm (int c)
    {
	switch (c)
	{
	case CONFLICT:
	    return "ConflictType.CONFLICT";
	case COMPATIBLE:
	    return "ConflictType.COMPATIBLE";
	case PRESENT:
	    return "ConflictType.PRESENT";
	default:
	    return "Unknown";
	}
    }
    
    /**
     * Print a human-readable form of the conflict type.
     */

    public static void print (PrintWriter strm, int c)
    {
	strm.print(c);
    }
    
}
