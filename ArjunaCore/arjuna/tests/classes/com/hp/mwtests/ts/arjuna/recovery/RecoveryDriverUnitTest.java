/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors 
 * as indicated by the @author tags. 
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
package com.hp.mwtests.ts.arjuna.recovery;

import com.arjuna.ats.arjuna.common.*;
import com.arjuna.ats.arjuna.recovery.RecoveryDriver;
import com.arjuna.ats.arjuna.recovery.RecoveryManager;

import org.junit.Test;

import static org.junit.Assert.*;

public class RecoveryDriverUnitTest
{
    @Test
    public void testInvalid () throws Exception
    {
        RecoveryDriver rd = new RecoveryDriver(0, "foobar");
        
        try
        {
            rd.asynchronousScan();
            
            fail();
        }
        catch (final Exception ex)
        {
        }
        
        try
        {
            rd.synchronousScan();
            
            fail();
        }
        catch (final Exception ex)
        {
        }
    }
    
    @Test
    public void testValid () throws Exception
    {
        RecoveryManager rm = RecoveryManager.manager();       
        
        rm.scan(null);
        
        RecoveryDriver rd = new RecoveryDriver(RecoveryManager.getRecoveryManagerPort(), recoveryPropertyManager.getRecoveryEnvironmentBean().getRecoveryAddress());
        
        assertTrue(rd.asynchronousScan());
        assertTrue(rd.synchronousScan());
    }
}
