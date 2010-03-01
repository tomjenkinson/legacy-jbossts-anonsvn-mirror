/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. 
 * See the copyright.txt in the distribution for a full listing 
 * of individual contributors.
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
 * Copyright (C) 2002,
 *
 * Arjuna Technologies Limited,
 * Newcastle upon Tyne,
 * Tyne and Wear,
 * UK.
 *
 * $Id: Context.java,v 1.2 2005/05/19 12:13:19 nmcl Exp $
 */

package com.arjuna.wsas.tests.junit.basic;

import com.arjuna.mw.wsas.UserActivity;
import com.arjuna.mw.wsas.UserActivityFactory;
import com.arjuna.mw.wsas.ActivityManagerFactory;
import com.arjuna.mw.wsas.activity.HLS;

import com.arjuna.mw.wsas.context.ContextManager;

import com.arjuna.wsas.tests.WSASTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Mark Little (mark.little@arjuna.com)
 * @version $Id: Context.java,v 1.2 2005/05/19 12:13:19 nmcl Exp $
 * @since 1.0.
 */

public class Context
{

    @Test
    public void testContext()
            throws Exception
    {
        UserActivity ua = UserActivityFactory.userActivity();
        HLS[] currentHLS = ActivityManagerFactory.activityManager().allHighLevelServices();

        for (HLS hls : currentHLS) {
            ActivityManagerFactory.activityManager().removeHLS(hls);
        }

	try
	{
	    ua.start();
	    
	    System.out.println("Started: "+ua.activityName());
	    
	    ua.start();

	    System.out.println("Started: "+ua.activityName());
	    
	    ContextManager manager = new ContextManager();
	    com.arjuna.mw.wsas.context.Context[] contexts = manager.contexts();
	    
	    if ((contexts != null) && (contexts.length != 0)) {
            fail("Contexts not null: "+contexts);
        }
    } finally {
        try {
            for (HLS hls : currentHLS) {
                ActivityManagerFactory.activityManager().addHLS(hls);
            }
        } catch (Exception e) {
            
        }
        WSASTestUtils.cleanup(ua);
    }
    }
}
