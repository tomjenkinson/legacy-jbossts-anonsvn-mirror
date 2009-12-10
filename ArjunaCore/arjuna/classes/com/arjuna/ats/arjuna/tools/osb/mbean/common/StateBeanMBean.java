/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
 * (C) 2009
 * @author Red Hat Middleware LLC.
 */
package com.arjuna.ats.arjuna.tools.osb.mbean.common;

import com.arjuna.ats.arjuna.tools.osb.annotation.MXBeanDescription;
import com.arjuna.ats.arjuna.tools.osb.annotation.MXBeanPropertyDescription;

/**
 * JMX instrumentation of Object Store log records that extend State Manager
 *
 * @see com.arjuna.ats.arjuna.StateManager
 */
@MXBeanDescription("Generic representation of log record state")
public interface StateBeanMBean extends UidBeanMBean
{
    @MXBeanPropertyDescription("The time that this record was last written to the log.")
    String getCreationTime();
    @MXBeanPropertyDescription("The number of seconds that have elapsed since this record was last saved")
    long getAgeInSeconds();
    @MXBeanPropertyDescription("The current status of this record (active, passive, ...)")
	String getStatus();	// status
    @MXBeanPropertyDescription("The type of the object (persistent, recoverable, ...)")
	String getObjectType();	// ObjectType
}
