/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated 
 * by the @authors tag. All rights reserved. 
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
 * Copyright (C) 2001,
 *
 * Hewlett-Packard Arjuna Labs,
 * Newcastle upon Tyne,
 * Tyne and Wear,
 * UK.  
 *
 * $Id: TwoPhaseCoordinator.java 2342 2006-03-30 13:06:17Z  $
 */

package com.arjuna.ats.arjuna.coordinator;

import com.arjuna.ats.arjuna.common.Uid;

import com.arjuna.ats.arjuna.logging.tsLogger;

import com.arjuna.ats.internal.arjuna.template.*;

/**
 * Adds support for synchronizations to BasicAction. It does not change thread
 * associations either. It also allows any thread to terminate a transaction,
 * even if it is not the transaction that is marked as current for the thread
 * (unlike the BasicAction default).
 * 
 * @author Mark Little (mark@arjuna.com)
 * @version $Id: TwoPhaseCoordinator.java 2342 2006-03-30 13:06:17Z  $
 * @since JTS 3.0.
 */

public class TwoPhaseCoordinator extends BasicAction implements Reapable
{

	public TwoPhaseCoordinator ()
	{
	}

	public TwoPhaseCoordinator (Uid id)
	{
		super(id);
	}

	public int start ()
	{
		return start(BasicAction.Current());
	}

	public int start (BasicAction parentAction)
	{
		int status = ActionStatus.INVALID;

		if (parentAction != null)
			parentAction.addChildAction(this);

		return super.Begin(parentAction);
	}

	public int end (boolean report_heuristics)
	{
		int outcome;

		if (parent() != null)
			parent().removeChildAction(this);

		if (beforeCompletion())
		{
			outcome = super.End(report_heuristics);
		}
		else
			outcome = super.Abort();

		afterCompletion(outcome);

		return outcome;
	}

	public int cancel ()
	{
		if (parent() != null)
			parent().removeChildAction(this);

		// beforeCompletion();

		int outcome = super.Abort();

		afterCompletion(outcome);

		return outcome;
	}

	public int addSynchronization (SynchronizationRecord sr)
	{		
		if (sr == null)
			return AddOutcome.AR_REJECTED;

		int result = AddOutcome.AR_REJECTED;
		
		if (parent() != null)
			return AddOutcome.AR_REJECTED;

		switch (status())
		{
			case ActionStatus.RUNNING:
			{
				if (_synchs == null)
					_synchs = new HashList(10);
	
				if (_synchs.add(sr))
					result = AddOutcome.AR_ADDED;
			}
			break;
		default:
			break;
		}

		return result;
	}

	/**
	 * @return <code>true</code> if the transaction is running,
	 *         <code>false</code> otherwise.
	 */

	public boolean running ()
	{
		return (boolean) (status() == ActionStatus.RUNNING);
	}

	/**
	 * Overloads BasicAction.type()
	 */

	public String type ()
	{
		return "/StateManager/BasicAction/AtomicAction/TwoPhaseCoordinator";
	}

	protected TwoPhaseCoordinator (int at)
	{
		super(at);
	}

	protected TwoPhaseCoordinator (Uid u, int at)
	{
		super(u, at);
	}

	/**
	 * @message com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_1
	 *          [com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_1]
	 *          TwoPhaseCoordinator.beforeCompletion - attempted rollback_only
	 *          failed!
	 * @message com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_2
	 *          [com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_2]
	 *          TwoPhaseCoordinator.beforeCompletion - failed for {0}
	 */

	protected boolean beforeCompletion ()
	{
		boolean problem = false;

		/*
		 * If we have a synchronization list then we must be top-level.
		 */

		if (_synchs != null)
		{
			HashListIterator iterator = new HashListIterator(_synchs);
			SynchronizationRecord record = (SynchronizationRecord) iterator.iterate();

			/*
			 * We must always call afterCompletion() methods, so just catch (and
			 * log) any exceptions/errors from beforeCompletion() methods.
			 */
			while ((record != null) && !problem)
			{
				try
				{
					problem = !record.beforeCompletion();

					record = (SynchronizationRecord) iterator.iterate();
				}
				catch (Exception ex)
				{
					tsLogger.arjLoggerI18N.warn("com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_2", new Object[]
					{ record });
					problem = true;
				}
				catch (Error er)
				{
					tsLogger.arjLoggerI18N.warn("com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_2", new Object[]
					{ record });
					problem = true;
				}
			}

			if (problem)
			{
				if (!preventCommit())
				{
					/*
					 * This should not happen. If it does, continue with commit
					 * to tidy-up.
					 */

					tsLogger.arjLoggerI18N.warn("com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_1");
				}
			}
		}

		return !problem;
	}

	/**
	 * @message com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_3
	 *          [com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_3]
	 *          TwoPhaseCoordinator.beforeCompletion
	 *          TwoPhaseCoordinator.afterCompletion called on still running
	 *          transaction!
	 * @message com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_4
	 *          [com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_4]
	 *          TwoPhaseCoordinator.afterCompletion - failed for {0}
	 */

	protected boolean afterCompletion (int myStatus)
	{
		if (myStatus == ActionStatus.RUNNING)
		{
			tsLogger.arjLoggerI18N.warn("com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_3");

			return false;
		}

		boolean problem = false;

		if (_synchs != null)
		{
			HashListIterator iterator = new HashListIterator(_synchs);
			SynchronizationRecord record = (SynchronizationRecord) iterator.iterate();

			/*
			 * Regardless of failures, we must tell all synchronizations what
			 * happened.
			 */

			while (record != null)
			{
				try
				{
					if (!record.afterCompletion(myStatus))
					{
						tsLogger.arjLoggerI18N.warn("com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_4", new Object[]
						{ record });

						problem = true;
					}
				}
				catch (Exception ex)
				{
					tsLogger.arjLoggerI18N.warn("com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_4", new Object[]
					{ record });
					problem = true;
				}
				catch (Error er)
				{
					tsLogger.arjLoggerI18N.warn("com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator_4", new Object[]
					{ record });
					problem = true;
				}

				record = (SynchronizationRecord) iterator.iterate();
			}

			_synchs = null;
		}

		return !problem;
	}

	private HashList _synchs;

}
