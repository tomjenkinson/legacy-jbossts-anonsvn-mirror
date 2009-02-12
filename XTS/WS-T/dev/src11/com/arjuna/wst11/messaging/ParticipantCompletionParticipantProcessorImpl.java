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
package com.arjuna.wst11.messaging;

import com.arjuna.webservices.SoapFault;
import com.arjuna.webservices.SoapFaultType;
import com.arjuna.webservices.base.processors.ActivatedObjectProcessor;
import com.arjuna.webservices.logging.WSTLogger;
import com.arjuna.webservices11.wsaddr.AddressingHelper;
import com.arjuna.webservices11.wsaddr.client.SoapFaultClient;
import com.arjuna.webservices11.wsarj.ArjunaContext;
import com.arjuna.webservices11.wsarj.InstanceIdentifier;
import com.arjuna.webservices11.wsba.ParticipantCompletionParticipantInboundEvents;
import com.arjuna.webservices11.wsba.BusinessActivityConstants;
import com.arjuna.webservices11.wsba.client.ParticipantCompletionCoordinatorClient;
import com.arjuna.webservices11.wsba.processors.ParticipantCompletionParticipantProcessor;
import com.arjuna.webservices11.ServiceRegistry;
import com.arjuna.webservices11.SoapFault11;
import com.arjuna.webservices11.wscoor.CoordinationConstants;
import com.arjuna.wsc11.messaging.MessageId;
import org.oasis_open.docs.ws_tx.wsba._2006._06.NotificationType;
import org.oasis_open.docs.ws_tx.wsba._2006._06.StatusType;
import org.jboss.jbossts.xts.recovery.participant.ba.XTSBARecoveryManager;

import javax.xml.ws.addressing.AddressingProperties;
import javax.xml.ws.addressing.AttributedURI;
import javax.xml.ws.addressing.AddressingBuilder;
import java.net.URISyntaxException;


/**
 * The Participant Completion Participant processor.
 * @author kevin
 */
public class ParticipantCompletionParticipantProcessorImpl extends ParticipantCompletionParticipantProcessor
{
    /**
     * The activated object processor.
     */
    private final ActivatedObjectProcessor activatedObjectProcessor = new ActivatedObjectProcessor() ;

    /**
     * Activate the participant.
     * @param participant The participant.
     * @param identifier The identifier.
     */
    public void activateParticipant(final ParticipantCompletionParticipantInboundEvents participant, final String identifier)
    {
        activatedObjectProcessor.activateObject(participant, identifier) ;
    }

    /**
     * Deactivate the participant.
     * @param participant The participant.
     */
    public void deactivateParticipant(final ParticipantCompletionParticipantInboundEvents participant)
    {
        activatedObjectProcessor.deactivateObject(participant) ;
    }

    /**
     * Check whether a participant with the given id is currently active
     * @param identifier The identifier.
     */
    public boolean isActive(final String identifier)
    {
        return activatedObjectProcessor.getObject(identifier) != null;
    }

    /**
     * Get the participant with the specified identifier.
     * @param instanceIdentifier The participant identifier.
     * @return The participant or null if not known.
     */
    private ParticipantCompletionParticipantInboundEvents getParticipant(final InstanceIdentifier instanceIdentifier)
    {
        final String identifier = (instanceIdentifier != null ? instanceIdentifier.getInstanceIdentifier() : null) ;
        return (ParticipantCompletionParticipantInboundEvents)activatedObjectProcessor.getObject(identifier) ;
    }

    /**
     * Cancel.
     * @param cancel The cancel notification.
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_1] - Unexpected exception thrown from cancel:
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_2 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_2] - Cancel called on unknown participant: {0}
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_3 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_3] - Cancel request dropped pending WS-BA participant recovery manager initialization for participant: {0}
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_4 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_4] - Cancel request dropped pending WS-BA participant recovery manager scan for unknown participant: {0}
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_5 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_5] - Cancel request dropped pending registration of application-specific recovery module for WS-BA participant: {0}
     */
    public void cancel(final NotificationType cancel, final AddressingProperties addressingProperties, final ArjunaContext arjunaContext)
    {
        final InstanceIdentifier instanceIdentifier = arjunaContext.getInstanceIdentifier() ;

        /**
         * ensure the BA participant recovery manager is running
         */

        XTSBARecoveryManager recoveryManager = XTSBARecoveryManager.getRecoveryManager();

        if (recoveryManager == null) {
            // log warning and drop this message -- it will be resent
            if (WSTLogger.arjLoggerI18N.isWarnEnabled())
            {
                WSTLogger.arjLoggerI18N.warn("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_3", new Object[] {instanceIdentifier}) ;
            }

            return;
        }

        final ParticipantCompletionParticipantInboundEvents participant = getParticipant(instanceIdentifier) ;

        if (participant != null)
        {
            try
            {
                participant.cancel(cancel, addressingProperties, arjunaContext) ;
            }
            catch (final Throwable th)
            {
                if (WSTLogger.arjLoggerI18N.isDebugEnabled())
                {
                    WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_1", th) ;
                }
            }
        }
        else if (!recoveryManager.isParticipantRecoveryStarted())
        {
            if (WSTLogger.arjLoggerI18N.isWarnEnabled())
            {
                WSTLogger.arjLoggerI18N.warn("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_4", new Object[] {instanceIdentifier}) ;
            }
        }
        else if (recoveryManager.findParticipantRecoveryRecord(instanceIdentifier.getInstanceIdentifier()) != null)
        {
            if (WSTLogger.arjLoggerI18N.isWarnEnabled())
            {
                WSTLogger.arjLoggerI18N.warn("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_5", new Object[] {instanceIdentifier}) ;
            }
        }
        else
        {
            if (WSTLogger.arjLoggerI18N.isDebugEnabled())
            {
                WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.cancel_2", new Object[] {instanceIdentifier}) ;
            }
            sendCancelled(addressingProperties, arjunaContext) ;
        }
    }

    /**
     * Close.
     * @param close The close notification.
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_1] - Unexpected exception thrown from close:
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_2 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_2] - Close called on unknown participant: {0}
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_3 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_3] - Close request dropped pending WS-BA participant recovery manager initialization for participant: {0}
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_4 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_4] - Close request dropped pending WS-BA participant recovery manager scan for unknown participant: {0}
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_5 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_5] - Close request dropped pending registration of application-specific recovery module for WS-BA participant: {0}
     */
    public void close(final NotificationType close, final AddressingProperties addressingProperties, final ArjunaContext arjunaContext)
    {
        final InstanceIdentifier instanceIdentifier = arjunaContext.getInstanceIdentifier() ;

        /**
         * ensure the BA participant recovery manager is running
         */

        XTSBARecoveryManager recoveryManager = XTSBARecoveryManager.getRecoveryManager();

        if (recoveryManager == null) {
            // log warning and drop this message -- it will be resent
            if (WSTLogger.arjLoggerI18N.isWarnEnabled())
            {
                WSTLogger.arjLoggerI18N.warn("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_3", new Object[] {instanceIdentifier}) ;
            }

            return;
        }

        final ParticipantCompletionParticipantInboundEvents participant = getParticipant(instanceIdentifier) ;

        if (participant != null)
        {
            try
            {
                participant.close(close, addressingProperties, arjunaContext) ;
            }
            catch (final Throwable th)
            {
                if (WSTLogger.arjLoggerI18N.isDebugEnabled())
                {
                    WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_1", th) ;
                }
            }
        }
        else if (!recoveryManager.isParticipantRecoveryStarted())
        {
            if (WSTLogger.arjLoggerI18N.isWarnEnabled())
            {
                WSTLogger.arjLoggerI18N.warn("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_4", new Object[] {instanceIdentifier}) ;
            }
        }
        else if (recoveryManager.findParticipantRecoveryRecord(instanceIdentifier.getInstanceIdentifier()) != null)
        {
            if (WSTLogger.arjLoggerI18N.isWarnEnabled())
            {
                WSTLogger.arjLoggerI18N.warn("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_5", new Object[] {instanceIdentifier}) ;
            }
        }
        else
        {
            if (WSTLogger.arjLoggerI18N.isDebugEnabled())
            {
                WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.close_2", new Object[] {instanceIdentifier}) ;
            }
            sendClosed(addressingProperties, arjunaContext) ;
        }
    }

    /**
     * Compensate.
     * @param compensate The compensate notification.
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_1] - Unexpected exception thrown from compensate:
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_2 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_2] - Compensate called on unknown participant: {0}
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_3 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_3] - Compensate request dropped pending WS-BA participant recovery manager initialization for participant: {0}
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_4 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_4] - Compensate request dropped pending WS-BA participant recovery manager scan for unknown participant: {0}
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_5 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_5] - Compensate request dropped pending registration of application-specific recovery module for WS-BA participant: {0}
     */
    public void compensate(final NotificationType compensate, final AddressingProperties addressingProperties, final ArjunaContext arjunaContext)
    {
        final InstanceIdentifier instanceIdentifier = arjunaContext.getInstanceIdentifier() ;

        /**
         * ensure the BA participant recovery manager is running
         */

        XTSBARecoveryManager recoveryManager = XTSBARecoveryManager.getRecoveryManager();

        if (recoveryManager == null) {
            // log warning and drop this message -- it will be resent
            if (WSTLogger.arjLoggerI18N.isWarnEnabled())
            {
                WSTLogger.arjLoggerI18N.warn("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_3", new Object[] {instanceIdentifier}) ;
            }

            return;
        }

        final ParticipantCompletionParticipantInboundEvents participant = getParticipant(instanceIdentifier) ;

        if (participant != null)
        {
            try
            {
                participant.compensate(compensate, addressingProperties, arjunaContext) ;
            }
            catch (final Throwable th)
            {
                if (WSTLogger.arjLoggerI18N.isDebugEnabled())
                {
                    WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_1", th) ;
                }
            }
        }
        else if (!recoveryManager.isParticipantRecoveryStarted())
        {
            if (WSTLogger.arjLoggerI18N.isWarnEnabled())
            {
                WSTLogger.arjLoggerI18N.warn("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_4", new Object[] {instanceIdentifier}) ;
            }
        }
        else if (recoveryManager.findParticipantRecoveryRecord(instanceIdentifier.getInstanceIdentifier()) != null)
        {
            if (WSTLogger.arjLoggerI18N.isWarnEnabled())
            {
                WSTLogger.arjLoggerI18N.warn("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_5", new Object[] {instanceIdentifier}) ;
            }
        }
        else
        {
            if (WSTLogger.arjLoggerI18N.isDebugEnabled())
            {
                WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.compensate_2", new Object[] {instanceIdentifier}) ;
            }
            sendCompensated(addressingProperties, arjunaContext) ;
        }
    }

    /**
     * Exited.
     * @param exited The exited notification.
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.exited_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.exited_1] - Unexpected exception thrown from exited:
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.exited_2 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.exited_2] - Exited called on unknown participant: {0}
     */
    public void exited(final NotificationType exited, final AddressingProperties addressingProperties, final ArjunaContext arjunaContext)
    {
        final InstanceIdentifier instanceIdentifier = arjunaContext.getInstanceIdentifier() ;
        final ParticipantCompletionParticipantInboundEvents participant = getParticipant(instanceIdentifier) ;

        if (participant != null)
        {
            try
            {
                participant.exited(exited, addressingProperties, arjunaContext) ;
            }
            catch (final Throwable th)
            {
                if (WSTLogger.arjLoggerI18N.isDebugEnabled())
                {
                    WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.exited_1", th) ;
                }
            }
        }
        else if (WSTLogger.arjLoggerI18N.isDebugEnabled())
        {
            WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.exited_2", new Object[] {instanceIdentifier}) ;
        }
    }

    /**
     * Not Completed.
     * @param notCompleted The not completed notification.
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.notCompleted_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.notCompleted_1] - Unexpected exception thrown from notCompleted:
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.notCompleted_2 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.notCompleted_2] - Exited called on unknown participant: {0}
     */
    public void notCompleted(final NotificationType notCompleted, final AddressingProperties addressingProperties, final ArjunaContext arjunaContext)
    {
        final InstanceIdentifier instanceIdentifier = arjunaContext.getInstanceIdentifier() ;
        final ParticipantCompletionParticipantInboundEvents participant = getParticipant(instanceIdentifier) ;

        if (participant != null)
        {
            try
            {
                participant.notCompleted(notCompleted, addressingProperties, arjunaContext) ;
            }
            catch (final Throwable th)
            {
                if (WSTLogger.arjLoggerI18N.isDebugEnabled())
                {
                    WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.notCompleted_1", th) ;
                }
            }
        }
        else if (WSTLogger.arjLoggerI18N.isDebugEnabled())
        {
            WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.notCompleted_2", new Object[] {instanceIdentifier}) ;
        }
    }

    /**
     * Failed.
     * @param failed The failed notification.
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.failed_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.failed_1] - Unexpected exception thrown from failed:
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.failed_2 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.failed_2] - Failed called on unknown participant: {0}
     */
    public void failed(final NotificationType failed, final AddressingProperties addressingProperties, final ArjunaContext arjunaContext)
    {
        final InstanceIdentifier instanceIdentifier = arjunaContext.getInstanceIdentifier() ;
        final ParticipantCompletionParticipantInboundEvents participant = getParticipant(instanceIdentifier) ;

        if (participant != null)
        {
            try
            {
                participant.failed(failed, addressingProperties, arjunaContext) ;
            }
            catch (final Throwable th)
            {
                if (WSTLogger.arjLoggerI18N.isDebugEnabled())
                {
                    WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.failed_1", th) ;
                }
            }
        }
        else if (WSTLogger.arjLoggerI18N.isDebugEnabled())
        {
            WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.failed_2", new Object[] {instanceIdentifier}) ;
        }
    }

    /**
     * Get Status.
     * @param getStatus The get status notification.
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_1] - Unexpected exception thrown from getStatus:
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_2 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_2] - GetStatus called on unknown participant: {0}
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_3 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_3] - Unexpected exception while sending InvalidStateFault to coordinator for participant {0}
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_4 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_4] - GetStatus requested for unknown participant completion participant
     */
    public void getStatus(final NotificationType getStatus, final AddressingProperties addressingProperties, final ArjunaContext arjunaContext)
    {
        final InstanceIdentifier instanceIdentifier = arjunaContext.getInstanceIdentifier() ;
        final ParticipantCompletionParticipantInboundEvents participant = getParticipant(instanceIdentifier) ;

        if (participant != null)
        {
            try
            {
                participant.getStatus(getStatus, addressingProperties, arjunaContext) ;
            }
            catch (final Throwable th)
            {
                if (WSTLogger.arjLoggerI18N.isDebugEnabled())
                {
                    WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_1", th) ;
                }
            }
        }
        else
        {
            if (WSTLogger.arjLoggerI18N.isDebugEnabled())
            {
                WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_2", new Object[] {instanceIdentifier}) ;
            }
            // send an invalid state fault

            final String messageId = MessageId.getMessageId();
            final AddressingProperties faultAddressingProperties = AddressingHelper.createFaultContext(addressingProperties, messageId) ;
            try
            {
                final SoapFault11 soapFault = new SoapFault11(SoapFaultType.FAULT_SENDER, CoordinationConstants.WSCOOR_ERROR_CODE_INVALID_STATE_QNAME,
                        WSTLogger.log_mesg.getString("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_4")) ;
                AddressingHelper.installNoneReplyTo(faultAddressingProperties);
                SoapFaultClient.sendSoapFault(soapFault, faultAddressingProperties, getFaultAction()) ;
            }
            catch (final Throwable th)
            {
                if (WSTLogger.arjLoggerI18N.isInfoEnabled())
                {
                    WSTLogger.arjLoggerI18N.info("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.getStatus_3", new Object[] {instanceIdentifier},  th) ;
                }
            }
        }
    }

    private static AttributedURI faultAction = null;

    private static synchronized AttributedURI getFaultAction()
    {
        if (faultAction == null) {
            AddressingBuilder builder = AddressingBuilder.getAddressingBuilder();

            try {
                faultAction = builder.newURI(CoordinationConstants.WSCOOR_ACTION_FAULT);
            } catch (URISyntaxException e) {
                // TODO log error here
            }
        }

        return faultAction;
    }
    /**
     * Status.
     * @param status The status type.
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.status_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.status_1] - Unexpected exception thrown from status:
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.status_2 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.status_2] - Status called on unknown participant: {0}
     */
    public void status(final StatusType status, final AddressingProperties addressingProperties, final ArjunaContext arjunaContext)
    {
        final InstanceIdentifier instanceIdentifier = arjunaContext.getInstanceIdentifier() ;
        final ParticipantCompletionParticipantInboundEvents participant = getParticipant(instanceIdentifier) ;

        if (participant != null)
        {
            try
            {
                participant.status(status, addressingProperties, arjunaContext) ;
            }
            catch (final Throwable th)
            {
                if (WSTLogger.arjLoggerI18N.isDebugEnabled())
                {
                    WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.status_1", th) ;
                }
            }
        }
        else if (WSTLogger.arjLoggerI18N.isDebugEnabled())
        {
            WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.status_2", new Object[] {instanceIdentifier}) ;
        }
    }

    /**
     * SOAP Fault.
     * @param fault The SOAP fault notification.
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.soapFault_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.soapFault_1] - Unexpected exception thrown from soapFault:
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.soapFault_2 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.soapFault_2] - SoapFault called on unknown participant: {0}
     */
    public void soapFault(final SoapFault fault, final AddressingProperties addressingProperties,
        final ArjunaContext arjunaContext)
    {
        final InstanceIdentifier instanceIdentifier = arjunaContext.getInstanceIdentifier() ;
        final ParticipantCompletionParticipantInboundEvents participant = getParticipant(instanceIdentifier) ;

        if (participant != null)
        {
            try
            {
                participant.soapFault(fault, addressingProperties, arjunaContext) ;
            }
            catch (final Throwable th)
            {
                if (WSTLogger.arjLoggerI18N.isDebugEnabled())
                {
                    WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.soapFault_1", th) ;
                }
            }
        }
        else if (WSTLogger.arjLoggerI18N.isDebugEnabled())
        {
            WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.soapFault_2", new Object[] {instanceIdentifier}) ;
        }
    }

    /**
     * Send a cancelled message.
     *
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.sendCancelled_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.sendCancelled_1] - Unexpected exception while sending Cancelled
     */
    private void sendCancelled(final AddressingProperties addressingProperties, final ArjunaContext arjunaContext)
    {
        // KEV add check for recovery
        final String messageId = MessageId.getMessageId() ;
        final AddressingProperties responseAddressingProperties = AddressingHelper.createOneWayResponseContext(addressingProperties, messageId) ;
        try
        {
            // supply null endpoint to indicate that addressing properties should be used to route message
            ParticipantCompletionCoordinatorClient.getClient().sendCancelled(null, responseAddressingProperties, arjunaContext.getInstanceIdentifier()) ;
        }
        catch (final Throwable th)
        {
            if (WSTLogger.arjLoggerI18N.isDebugEnabled())
            {
                WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.sendCancelled_1", th) ;
            }
        }
    }

    /**
     * Send a closed message.
     *
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.sendClosed_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.sendClosed_1] - Unexpected exception while sending Closed
     */
    private void sendClosed(final AddressingProperties addressingProperties, final ArjunaContext arjunaContext)
    {
        // KEV add check for recovery
        final String messageId = MessageId.getMessageId() ;
        final AddressingProperties responseAddressingProperties = AddressingHelper.createOneWayResponseContext(addressingProperties, messageId) ;
        try
        {
            // supply null endpoint to indicate that addressing properties should be used to route message
            ParticipantCompletionCoordinatorClient.getClient().sendClosed(null, responseAddressingProperties, arjunaContext.getInstanceIdentifier()) ;
        }
        catch (final Throwable th)
        {
            if (WSTLogger.arjLoggerI18N.isDebugEnabled())
            {
                WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.sendClosed_1", th) ;
            }
        }
    }

    /**
     * Send a compensated message.
     *
     * @param addressingProperties The addressing context.
     * @param arjunaContext The arjuna context.
     *
     * @message com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.sendCompensated_1 [com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.sendCompensated_1] - Unexpected exception while sending Compensated
     */
    private void sendCompensated(final AddressingProperties addressingProperties, final ArjunaContext arjunaContext)
    {
        // KEV add check for recovery
        final String messageId = MessageId.getMessageId() ;
        final AddressingProperties responseAddressingProperties = AddressingHelper.createOneWayResponseContext(addressingProperties, messageId) ;
        try
        {
            // supply null endpoint to indicate that addressing properties should be used to route message
            ParticipantCompletionCoordinatorClient.getClient().sendCompensated(null, responseAddressingProperties, arjunaContext.getInstanceIdentifier()) ;
        }
        catch (final Throwable th)
        {
            if (WSTLogger.arjLoggerI18N.isDebugEnabled())
            {
                WSTLogger.arjLoggerI18N.debug("com.arjuna.wst11.messaging.ParticipantCompletionParticipantProcessorImpl.sendCompensated_1", th) ;
            }
        }
    }
}