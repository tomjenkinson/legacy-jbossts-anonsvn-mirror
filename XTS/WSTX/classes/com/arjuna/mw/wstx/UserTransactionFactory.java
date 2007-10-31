/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. 
 * See the copyright.txt in the distribution for a full listing 
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
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
 * $Id: UserTransactionFactory.java,v 1.5 2005/03/10 15:37:12 nmcl Exp $
 */

package com.arjuna.mw.wstx;

import com.arjuna.mw.wstx.logging.wstxLogger;

import com.arjuna.mw.wstx.common.TransactionXSD;

import com.arjuna.mw.wscf.protocols.*;
import com.arjuna.mw.wscf.utils.*;

import com.arjuna.mw.wscf.exceptions.ProtocolNotRegisteredException;

import com.arjuna.mwlabs.wscf.utils.ProtocolLocator;

import com.arjuna.mw.wsas.exceptions.SystemException;

import java.util.HashMap;

/**
 * Return the UserTransaction implementation to use.
 *
 * @author Mark Little (mark.little@arjuna.com)
 * @version $Id: UserTransactionFactory.java,v 1.5 2005/03/10 15:37:12 nmcl Exp $
 * @since 1.0.
 */

public class UserTransactionFactory
{

    /**
     * @exception ProtocolNotRegisteredException Thrown if the default
     * protocol is not available.
     *
     * @return the UserCoordinator implementation to use. The default
     * coordination protocol is used (two-phase commit) with its
     * associated implementation.
     *
     * @message com.arjuna.mw.wstx.UserTransactionFactory_1 [com.arjuna.mw.wstx.UserTransactionFactory_1] - Failed to create document: 
     */

    public static UserTransaction userTransaction () throws ProtocolNotRegisteredException, SystemException
    {
	try
	{
	    ProtocolLocator pl = new ProtocolLocator(com.arjuna.mwlabs.wstx.model.as.twophase.UserTwoPhaseTx.class.getName());
	    org.w3c.dom.Document doc = pl.getProtocol();
	    
	    if (doc == null)
	    {
		wstxLogger.arjLoggerI18N.warn("com.arjuna.mw.wstx.UserTransactionFactory_1",
					      new Object[]{com.arjuna.mwlabs.wstx.model.as.twophase.UserTwoPhaseTx.class.getName()});
	    }
	    else
	    {
		if (!_protocolManager.present(doc))
		    _protocolManager.addProtocol(doc, com.arjuna.mwlabs.wstx.model.as.twophase.UserTwoPhaseTx.class.getName());
		
		return userTransaction(doc);
	    }
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
     * @exception ProtocolNotRegisteredException Thrown if the requested
     * protocol is not available.
     *
     * @return the UserCoordinator implementation to use.
     */
    
    /*
     * Have the type specified in XML. More data may be specified, which
     * can be passed to the implementation in the same way ObjectName was.
     */

    public static UserTransaction userTransaction (org.w3c.dom.Document protocol) throws ProtocolNotRegisteredException, SystemException
    {
	try
	{
	    synchronized (_implementations)
	    {
		org.w3c.dom.Text child = DomUtil.getTextNode(protocol, TransactionXSD.transactionType);
		String protocolType = child.getNodeValue();
		UserTransaction tx = (UserTransaction) _implementations.get(protocolType);
	
		if (tx == null)
		{
		    Object implementation = _protocolManager.getProtocolImplementation(protocol);
	
		    if (implementation instanceof String)
		    {
			Class txImpl = Class.forName((String) implementation);
			
			tx = (UserTransaction) txImpl.newInstance();
		    }
		    else
			tx = (UserTransaction) implementation;

		    _implementations.put(protocolType, tx);
		}

		return tx;
	    }
	}
	catch (ProtocolNotRegisteredException ex)
	{
	    throw ex;
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	    
	    throw new SystemException(ex.toString());
	}
    }

    private static ProtocolManager _protocolManager = ProtocolRegistry.sharedManager();
    private static HashMap         _implementations = new HashMap();
    
}

