/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.jbossts.qa.Utils;

import org.jboss.dtf.testframework.unittest.Test;
import com.arjuna.orbportability.Services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;

/*
 * Copyright (C) 1998, 1999, 2000, 2001, 2002, 2003
 *
 * Arjuna Technology Ltd.
 * Newcastle upon Tyne,
 * Tyne and Wear,
 * UK.
 *
 * $Id: SetupOTSServer.java,v 1.2 2003/06/26 11:45:07 rbegg Exp $
 */

public class SetupOTSServer2
{
	public static void main(String[] args)
	{
		String bindName = System.getProperty(RegisterOTSServer.NAME_SERVICE_BIND_NAME_PROPERTY);

		if (bindName != null)
		{
			System.out.println("Looking up OTS Server '" + bindName + "'");

			try
			{
				String transactionServiceIOR = getService(bindName);

				ORBInterface.initORB(args, null);

				String[] transactionFactoryParams = new String[1];
				transactionFactoryParams[0] = ORBServices.otsKind;

				Services services = new Services(ORBInterface.getORB());

				services.registerService(ORBInterface.orb().string_to_object(transactionServiceIOR), ORBServices.transactionService, transactionFactoryParams);

                System.out.println("Ready");
			}
			catch (Exception e)
			{
				e.printStackTrace(System.err);
                System.out.println("Failed");
			}
		}
		else
		{
			System.out.println("Bind name '" + RegisterOTSServer.NAME_SERVICE_BIND_NAME_PROPERTY + "' not specified");
            System.out.println("Failed");
		}
	}

    public static String getService(String name) throws IOException
    {
        BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(name)));
        String returnValue = fin.readLine();
        fin.close();
        return returnValue;
    }
}