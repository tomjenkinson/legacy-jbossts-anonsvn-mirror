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

import com.arjuna.ats.arjuna.common.Uid;

/**
 * @see com.arjuna.ats.arjuna.tools.osb.mbean.common.UidBeanMBean
 */
public class UidBean extends BasicBean implements UidBeanMBean
{
    protected Uid uid;

    public UidBean(BasicBean parent, String type, Uid uid)
    {
        super(parent, type);
        this.uid = uid;
    }

    public UidBean(BasicBean parent, Uid uid)
    {
        this(parent, parent.getType(), uid);
    }

    public String getObjectName()
    {
		String pon = parent.getObjectName();
		String key = (pon.indexOf(",uid=") == -1 ? ",uid=" : ",suid=");

        return parent.getObjectName() + key + "\"" + uid.toString() + "\"";
    }

    public String getUid()
    {
        return uid.toString();
    }
}
