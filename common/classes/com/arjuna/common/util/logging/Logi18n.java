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
/*
* Log.java
*
* Copyright (c) 2003 Arjuna Technologies Ltd.
* Arjuna Technologies Ltd. Confidential
*
* Created on Jun 30, 2003, 10:04:22 AM by Thomas Rischbeck
*/
package com.arjuna.common.util.logging;

/**
 * Internationalised logging interface abstracting the various logging APIs
 * supported by Arjuna CLF.
 *
 * <p> The five logging levels used by <code>Log</code> are (in order):
 * <ol>
 * <li>debug (the least serious</li>
 * <li>info</li>
 * <li>warn</li>
 * <li>error</li>
 * <li>fatal (the most serious)</li>
 * </ol>
 *
 * The mapping of these log levels to the concepts used by the underlying
 * logging system is implementation dependent. The implemention should ensure,
 * though, that this ordering behaves as expected.</p>
 *
 * <p>Performance is often a logging concern. By examining the appropriate property,
 * a component can avoid expensive operations (producing information
 * to be logged).</p>
 *
 * <p> For example,
 * <code><pre>
 *    if (log.isDebugEnabled()) {
 *        ... do something expensive ...
 *        log.debug(theResult);
 *    }
 * </pre></code>
 * </p>
 *
 * <p>Configuration of the underlying logging system will generally be done
 * external to the Logging APIs, through whatever mechanism is supported by
 * that system.</p>
 *
 * @author Thomas Rischbeck <thomas.rischbeck@arjuna.com>
 * @version $Revision: 2342 $ $Date: 2006-03-30 14:06:17 +0100 (Thu, 30 Mar 2006) $
 * @since clf-2.0
 */
public interface Logi18n
{
   /**
    * Determine if this logger is enabled for DEBUG messages.
    *
    * This method returns true when the underlying logger is configured with DEBUG level on.
    *
    * @return  True if the logger is enabled for DEBUG, false otherwise
    */
   boolean isDebugEnabled();

   /**
    * Determine if this logger is enabled for INFO messages.
    * @return  True if the logger is enabled for INFO, false otherwise
    */
   boolean isInfoEnabled();

   /**
    * Determine if this logger is enabled for WARN messages.
    * @return  True if the logger is enabled for WARN, false otherwise
    */
   boolean isWarnEnabled();

   /**
    * Determine if this logger is enabled for ERROR messages.
    * @return  True if the logger is enabled for ERROR, false otherwise
    */
   boolean isErrorEnabled();

   /**
    * Determine if this logger is enabled for FATAL messages.
    * @return  True if the logger is enabled for FATAL, false otherwise
    */
   boolean isFatalEnabled();


   /************************   Log Debug Messages   ****************************/

   /**
    * Log a message with DEBUG Level
    *
    * @param key resource bundle key for the message to log
    */
   void debug(String key);

   /**
    * Log a message with the DEBUG Level and with a throwable arguments
    * @param key resource bundle key for the message to log
    * @param throwable The Throwable to log
    */
   void debug(String key, Throwable throwable);

   /**
    * Log a message with DEBUG Level and with arguments
    *
    * @param key resource bundle key for the message to log
    * @param params parameters passed to the message
    */
   void debug(String key, Object[] params);

   /**
    * Log a message with DEBUG Level, with arguments and with a throwable arguments
    *
    * @param key resource bundle key for the message to log
    * @param params parameters passed to the message
    * @param throwable The Throwable to log
    */
   void debug(String key, Object[] params, Throwable throwable);

   /************************   Log Info Messages   ****************************/

   /**
    * Log a message with INFO Level
    *
    * @param key resource bundle key for the message to log
    */
   void info(String key);

   /**
    * Log a message with the INFO Level and with a throwable arguments
    * @param key resource bundle key for the message to log
    * @param throwable Throwable associated to the logging message
    */
   void info(String key, Throwable throwable);

   /**
    * Log a message with the INFO Level and  with arguments
    * @param key resource bundle key for the message to log
    * @param params parameters passed to the message
    */
   void info(String key, Object[] params);

   /**
    * Log a message with the INFO Level, with arguments and with a throwable arguments
    * @param key resource bundle key for the message to log
    * @param params parameters passed to the message
    * @param throwable Throwable associated with the logging request
    */
   void info(String key, Object[] params, Throwable throwable);


   /************************   Log Warn Messages   ****************************/

   /**
    * Log a message with the WARN Level
    * @param key resource bundle key for the message to log
    */
   void warn(String key);

   /**
    * Log a message with the WARN Level and with a throwable arguments
    * @param key resource bundle key for the message to log
    * @param throwable Throwable associated with the logging request
    */
   void warn(String key, Throwable throwable);

   /**
    * Log a message with the WARN Level and  with arguments
    * @param key resource bundle key for the message to log
    * @param params parameters passed to the message
    */
   void warn(String key, Object[] params);

   /**
    * Log a message with the WARN Level, with arguments and with a throwable arguments
    * @param key resource bundle key for the message to log
    * @param params parameters passed to the message
    * @param throwable Throwable associated with the logging request
    */
   void warn(String key, Object[] params, Throwable throwable);


   /************************   Log Error Messages   ****************************/

   /**
    * Log a message with the ERROR Level
    * @param key resource bundle key for the message to log
    */
   void error(String key);

   /**
    * Log a message with the ERROR Level and with a throwable arguments
    * @param key resource bundle key for the message to log
    * @param throwable Throwable associated with the logging request
    */
   void error(String key, Throwable throwable);

   /**
    * Log a message with the ERROR Level and  with arguments
    * @param key resource bundle key for the message to log
    * @param params parameters passed to the message
    */
   void error(String key, Object[] params);

   /**
    * Log a message with the ERROR Level, with arguments and with a throwable arguments
    * @param key resource bundle key for the message to log
    * @param params parameters passed to the message
    * @param throwable Throwable associated with the logging request
    */
   void error(String key, Object[] params, Throwable throwable);

   /************************   Log Fatal Messages   ****************************/

   /**
    * Log a message with the FATAL Level
    * @param key resource bundle key for the message to log
    */
   void fatal(String key);

   /**
    * Log a message with the FATAL Level and with a throwable arguments
    * @param key resource bundle key for the message to log
    * @param throwable Throwable associated with the logging request
    */
   void fatal(String key, Throwable throwable);

   /**
    * Log a message with the FATAL Level and  with arguments
    * @param key resource bundle key for the message to log
    * @param params parameters passed to the message
    */
   void fatal(String key, Object[] params);

   /**
    * Log a message with the FATAL Level, with arguments and with a throwable arguments
    * @param key resource bundle key for the message to log
    * @param params parameters passed to the message
    * @param throwable Throwable associated with the logging request
    */
   void fatal(String key, Object[] params, Throwable throwable);


   /**********************************************************************************************************/
   /* RESOURCE BUNDLE EVALUATION */
   /**********************************************************************************************************/

   /**
    * Obtain a localized message from one of the resource bundles associated
    * with this logger.
    *
    * The user supplied parameter <code>key</code> is replaced by its localized
    * version from the resource bundle.
    *
    * @param key unique key to identify an entry in the resource bundle.
    * @return The localised string according to user's locale and available resource bundles. placeholder message
    *    if the resource bundle or key cannot be found.
    */
   public String getString(String key);

   /**
    * Obtain a localized and parameterized message from one of the resource
    * bundles associated with this logger.
    *
    * First, the user supplied <code>key</code> is searched in the resource
    * bundle. Next, the resulting pattern is formatted using
    * {@link java.text.MessageFormat#format(String,Object[])} method with the
    * user supplied object array <code>params</code>.
    *
    * @param key unique key to identify an entry in the resource bundle.
    * @param params parameters to fill placeholders (e.g., {0}, {1}) in the resource bundle string.
    * @return The localised string according to user's locale and available resource bundles. placeholder message
    *    if the resource bundle or key cannot be found.
    */
   public String getString(String key, Object[] params);
}
