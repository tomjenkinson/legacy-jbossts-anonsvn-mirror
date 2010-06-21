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

package com.arjuna.ats.internal.arjuna.recovery ;

import java.util.*;

import com.arjuna.ats.arjuna.recovery.RecoveryActivator ;
import com.arjuna.ats.arjuna.common.recoveryPropertyManager;

import com.arjuna.ats.arjuna.logging.tsLogger;

/**
 * RecoveryActivators are dynamically loaded. The recoveryActivator to load
 * are specified by properties beginning with "recoveryActivator"
 * <P>
 * @author Malik Saheb
 * @since ArjunaTS 3.0
*/

public class RecActivatorLoader
{

   public RecActivatorLoader()
   {
       initialise();

       // Load the Recovery Activators
       loadRecoveryActivators();

       startRecoveryActivators();

   }

  /**
   * Start the RecoveryActivator
   */

  public void startRecoveryActivators()
      //public void run()
  {
      tsLogger.i18NLogger.info_recovery_RecActivatorLoader_6();

      Enumeration activators = _recoveryActivators.elements();

      while (activators.hasMoreElements())
	  {
	      RecoveryActivator acti = (RecoveryActivator) activators.nextElement();
	      acti.startRCservice();
	  }

      return;

  }

    // These are loaded in list iteration order.
    private static void loadRecoveryActivators ()
    {
        Vector<String> activatorNames = new Vector<String>(recoveryPropertyManager.getRecoveryEnvironmentBean().getRecoveryActivators());

        for(String activatorName : activatorNames) {
            loadActivator(activatorName);
        }
    }

  private static void loadActivator (String className)
  {
      if (tsLogger.arjLogger.isDebugEnabled()) {
          tsLogger.arjLogger.debug("Loading recovery activator " +
                  className);
      }

      if (className == null) {
          tsLogger.i18NLogger.warn_recovery_RecActivatorLoader_1();

          return;
      }
      else
	  {
	      try
		  {
		      Class c = Thread.currentThread().getContextClassLoader().loadClass( className ) ;

		      try
			  {
			      RecoveryActivator ra = (RecoveryActivator) c.newInstance() ;
			      _recoveryActivators.add( ra );
			  }
		      catch (ClassCastException e) {
                  tsLogger.i18NLogger.warn_recovery_RecActivatorLoader_2(className);
              }
		      catch (IllegalAccessException iae) {
                  tsLogger.i18NLogger.warn_recovery_RecActivatorLoader_3(iae);
              }
		      catch (InstantiationException ie) {
                  tsLogger.i18NLogger.warn_recovery_RecActivatorLoader_4(ie);
              }

		      c = null;
		  }
	      catch ( ClassNotFoundException cnfe ) {
              tsLogger.i18NLogger.warn_recovery_RecActivatorLoader_5(className);
          }
      }
  }

    private final void initialise ()
   {
       _recoveryActivators = new Vector();
   }

    // this refers to the recovery activators specified in the recovery manager
    // property file which are dynamically loaded.
    private static Vector _recoveryActivators = null ;

}









