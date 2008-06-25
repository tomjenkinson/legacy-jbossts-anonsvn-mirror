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
 * $Id: UserCoordinatorFactory.java,v 1.6 2005/05/19 12:13:25 nmcl Exp $
 */

package com.arjuna.mw.wscf11.model.twophase;

import com.arjuna.mw.wscf.logging.wscfLogger;

import com.arjuna.mw.wscf.model.twophase.hls.TwoPhaseHLS;
import com.arjuna.mw.wscf.model.twophase.api.UserCoordinator;

import com.arjuna.mw.wsas.exceptions.SystemException;

import com.arjuna.mwlabs.wscf.utils.ProtocolLocator;

import com.arjuna.mw.wscf.common.CoordinatorXSD;

import com.arjuna.mw.wscf.utils.*;

import com.arjuna.mw.wscf.exceptions.ProtocolNotRegisteredException;
import com.arjuna.mw.wscf11.protocols.ProtocolManager;
import com.arjuna.mw.wscf11.protocols.ProtocolRegistry;

import java.util.HashMap;

import com.arjuna.mwlabs.wscf11.model.twophase.arjunacore.TwoPhaseHLSImple;

/**
 * The factory which returns the UserCoordinator implementation to use.
 *
 * @author Mark Little (mark.little@arjuna.com)
 * @version $Id: UserCoordinatorFactory.java,v 1.6 2005/05/19 12:13:25 nmcl Exp $
 * @since 1.0.
 */

public class UserCoordinatorFactory
{

    /**
     * @exception com.arjuna.mw.wscf.exceptions.ProtocolNotRegisteredException Thrown if the default
     * protocol is not available.
     *
     * @return the CoordinatorManager implementation to use. The default
     * coordination protocol is used (two-phase commit) with its
     * associated implementation.
     *
     * @message com.arjuna.mw.wscf11.model.twophase.UCF_1 [com.arjuna.mw.wscf11.model.twophase.UCF_1] - Failed to create
     */

    public static UserCoordinator userCoordinator () throws ProtocolNotRegisteredException, SystemException
    {
	try
	{
	    ProtocolLocator pl = new ProtocolLocator(TwoPhaseHLSImple.class);
	    org.w3c.dom.Document doc = pl.getProtocol();

	    if (doc == null)
	    {
		wscfLogger.arjLoggerI18N.warn("com.arjuna.mw.wscf11.model.twophase.UCF_1",
					      new Object[]{TwoPhaseHLSImple.className()});
	    }
	    else
		return userCoordinator(doc);
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();

	    throw new SystemException(ex.toString());
	}

	return null;
    }

    /**
     * Obtain a reference to a coordinator that implements the specified
     * protocol.
     *
     * @param protocol The XML definition of the type of
     * coordination protocol required.
     *
     * @exception com.arjuna.mw.wscf.exceptions.ProtocolNotRegisteredException Thrown if the requested
     * protocol is not available.
     *
     * @return the CoordinatorManager implementation to use.
     */

    /*
     * Have the type specified in XML. More data may be specified, which
     * can be passed to the implementation in the same way ObjectName was.
     */

    public static UserCoordinator userCoordinator (org.w3c.dom.Document protocol) throws ProtocolNotRegisteredException, SystemException
    {
	try
	{
	    synchronized (_implementations)
	    {
		org.w3c.dom.Text child = DomUtil.getTextNode(protocol, CoordinatorXSD.coordinatorType);
		String protocolType = child.getNodeValue();
		TwoPhaseHLS coordHLS = (TwoPhaseHLS) _implementations.get(protocolType);

		if (coordHLS == null)
		{
		    Object implementation = _protocolManager.getProtocolImplementation(protocol);

		    if (implementation instanceof String)
		    {
			Class c = Class.forName((String) implementation);

			coordHLS = (TwoPhaseHLS) c.newInstance();
		    }
		    else
			coordHLS = (TwoPhaseHLS) implementation;

		    _implementations.put(protocolType, coordHLS);
		}

		return coordHLS.userCoordinator();
	    }
	}
	catch (ProtocolNotRegisteredException ex)
	{
	    throw ex;
	}
	catch (Exception ex)
	{
	    throw new SystemException(ex.toString());
	}
    }

    private static ProtocolManager _protocolManager = ProtocolRegistry.sharedManager();
    private static HashMap         _implementations = new HashMap();

}