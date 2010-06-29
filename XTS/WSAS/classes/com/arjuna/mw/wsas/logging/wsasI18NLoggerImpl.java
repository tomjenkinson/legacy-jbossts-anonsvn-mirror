/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and/or its affiliates,
 * and individual contributors as indicated by the @author tags.
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
 * (C) 2010,
 * @author JBoss, by Red Hat.
 */
package com.arjuna.mw.wsas.logging;

import org.jboss.logging.Logger;
import static org.jboss.logging.Logger.Level.*;

/**
 * i18n log messages for the wsas module.
 * This class is autogenerated. Don't mess with it.
 *
 * @author Jonathan Halliday (jonathan.halliday@redhat.com) 2010-06
 */
public class wsasI18NLoggerImpl implements wsasI18NLogger {

	private final Logger logger;

	wsasI18NLoggerImpl(Logger logger) {
		this.logger = logger;
	}

	public void warn_context_ContextManager_1(Throwable arg0) {
		logger.logv(WARN, arg0, "ARJUNA-41001 allHighLevelServices threw exception", (Object)null);
	}

	public void warn_context_ContextManager_2(Throwable arg0) {
		logger.logv(WARN, arg0, "ARJUNA-41002 assembling contexts and received exception", (Object)null);
	}

	public String get_utils_Configuration_2() {
		return "ARJUNA-41004 Failed to create doc";
	}

	public void warn_UserActivityImple_1(Throwable arg0) {
		logger.logv(WARN, arg0, "ARJUNA-41005 Activity.start caught exception", (Object)null);
	}

	public void warn_UserActivityImple_2(Throwable arg0) {
		logger.logv(WARN, arg0, "ARJUNA-41006 currentActivity.end threw:", (Object)null);
	}

	public void warn_UserActivityImple_3(Throwable arg0) {
		logger.logv(WARN, arg0, "ARJUNA-41007 Activity.completed caught exception", (Object)null);
	}

	public void warn_UserActivityImple_4(Throwable arg0) {
		logger.logv(WARN, arg0, "ARJUNA-41008 Activity.suspended caught:", (Object)null);
	}

	public void warn_UserActivityImple_5(Throwable arg0) {
		logger.logv(WARN, arg0, "ARJUNA-41009 Activity.resumed caught exception", (Object)null);
	}

	public String get_UserActivityImple_51() {
		return "ARJUNA-41010 Unknown activity implementation!";
	}

	public String get_activity_ActivityImple_1() {
		return "ARJUNA-41011 State incompatible to start activity:";
	}

	public String get_activity_ActivityImple_10() {
		return "ARJUNA-41012 Cannot remove child activity from parent as parent's status is:";
	}

	public String get_activity_ActivityImple_2() {
		return "ARJUNA-41013 Activity cannot complete as it has active children:";
	}

	public String get_activity_ActivityImple_3() {
		return "ARJUNA-41014 Cannot complete activity in status:";
	}

	public String get_activity_ActivityImple_4() {
		return "ARJUNA-41015 Cannot set completion status on activity as the status is incompatible:";
	}

	public String get_activity_ActivityImple_5() {
		return "ARJUNA-41016 Cannot change completion status, value is incompatible:";
	}

	public String get_activity_ActivityImple_6() {
		return "ARJUNA-41017 Cannot enlist null child!";
	}

	public String get_activity_ActivityImple_7() {
		return "ARJUNA-41018 Cannot enlist child activity with parent as parent's status is:";
	}

	public String get_activity_ActivityImple_8() {
		return "ARJUNA-41019 Cannot remove null child!";
	}

	public String get_activity_ActivityImple_9() {
		return "ARJUNA-41020 The following child activity is unknown to the parent:";
	}

	public void warn_activity_ActivityReaper_1() {
		logger.logv(WARN, "ARJUNA-41021 ActivityReaper: could not terminate.", (Object)null);
	}

	public String get_activity_HLSManager_1() {
		return "ARJUNA-41022 HLS not found!";
	}
}
