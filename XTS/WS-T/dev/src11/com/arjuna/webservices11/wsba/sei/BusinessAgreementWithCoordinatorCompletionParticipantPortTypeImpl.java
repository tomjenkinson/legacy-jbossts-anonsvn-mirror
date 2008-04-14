
package com.arjuna.webservices11.wsba.sei;

import com.arjuna.services.framework.task.Task;
import com.arjuna.services.framework.task.TaskManager;
import com.arjuna.webservices11.wsarj.ArjunaContext;
import com.arjuna.webservices11.wsba.processors.CoordinatorCompletionParticipantProcessor;
import org.oasis_open.docs.ws_tx.wsba._2006._06.BusinessAgreementWithCoordinatorCompletionParticipantPortType;
import org.oasis_open.docs.ws_tx.wsba._2006._06.NotificationType;
import org.oasis_open.docs.ws_tx.wsba._2006._06.StatusType;

import javax.annotation.Resource;
import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.addressing.AddressingProperties;
import javax.xml.ws.addressing.JAXWSAConstants;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.Addressing;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1-b03-
 * Generated source version: 2.0
 *
 */
@WebService(name = "BusinessAgreementWithCoordinatorCompletionParticipantPortType", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06",
        wsdlLocation = "/WEB-INF/wsdl/wsba-coordinator-completion-participant-binding.wsdl",
        serviceName = "BusinessAgreementWithCoordinatorCompletionParticipantService",
        portName = "BusinessAgreementWithCoordinatorCompletionParticipantPortType"
)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@HandlerChain(file="handlers.xml")
@Addressing(required=true)
public class BusinessAgreementWithCoordinatorCompletionParticipantPortTypeImpl implements BusinessAgreementWithCoordinatorCompletionParticipantPortType
{
    @Resource
    private WebServiceContext webServiceCtx;

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "CompleteOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/mplete")
    @Oneway
    public void completeOperation(
        @WebParam(name = "Complete", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        final NotificationType complete = parameters;
        final AddressingProperties inboundAddressProperties
            = (AddressingProperties)ctx.get(JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                CoordinatorCompletionParticipantProcessor.getProcessor().complete(complete, inboundAddressProperties, arjunaContext) ;
            }
        }) ;
    }

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "CloseOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Close")
    @Oneway
    public void closeOperation(
        @WebParam(name = "Close", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        final NotificationType close = parameters;
        final AddressingProperties inboundAddressProperties
            = (AddressingProperties)ctx.get(JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                CoordinatorCompletionParticipantProcessor.getProcessor().close(close, inboundAddressProperties, arjunaContext) ;
            }
        }) ;
    }

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "CancelOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Cancel")
    @Oneway
    public void cancelOperation(
        @WebParam(name = "Cancel", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        final NotificationType cancel = parameters;
        final AddressingProperties inboundAddressProperties
            = (AddressingProperties)ctx.get(JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                CoordinatorCompletionParticipantProcessor.getProcessor().cancel(cancel, inboundAddressProperties, arjunaContext) ;
            }
        }) ;
    }

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "CompensateOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Close")
    @Oneway
    public void compensateOperation(
        @WebParam(name = "Compensate", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        final NotificationType compensate = parameters;
        final AddressingProperties inboundAddressProperties
            = (AddressingProperties)ctx.get(JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                CoordinatorCompletionParticipantProcessor.getProcessor().compensate(compensate, inboundAddressProperties, arjunaContext) ;
            }
        }) ;
    }

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "FailedOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Failed")
    @Oneway
    public void failedOperation(
        @WebParam(name = "Failed", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        final NotificationType failed = parameters;
        final AddressingProperties inboundAddressProperties
            = (AddressingProperties)ctx.get(JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                CoordinatorCompletionParticipantProcessor.getProcessor().failed(failed, inboundAddressProperties, arjunaContext) ;
            }
        }) ;
    }

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "ExitedOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Exited")
    @Oneway
    public void exitedOperation(
        @WebParam(name = "Exited", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        final NotificationType exited = parameters;
        final AddressingProperties inboundAddressProperties
            = (AddressingProperties)ctx.get(JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                CoordinatorCompletionParticipantProcessor.getProcessor().exited(exited, inboundAddressProperties, arjunaContext) ;
            }
        }) ;
    }

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "NotCompleted", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/NotCompleted")
    @Oneway
    public void notCompleted(
        @WebParam(name = "NotCompleted", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        final NotificationType notCompleted = parameters;
        final AddressingProperties inboundAddressProperties
            = (AddressingProperties)ctx.get(JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                CoordinatorCompletionParticipantProcessor.getProcessor().notCompleted(notCompleted, inboundAddressProperties, arjunaContext) ;
            }
        }) ;
    }

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "GetStatusOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/GetStatus")
    @Oneway
    public void getStatusOperation(
        @WebParam(name = "GetStatus", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        final NotificationType getStatus = parameters;
        final AddressingProperties inboundAddressProperties
            = (AddressingProperties)ctx.get(JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                CoordinatorCompletionParticipantProcessor.getProcessor().getStatus(getStatus, inboundAddressProperties, arjunaContext) ;
            }
        }) ;
    }

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "StatusOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Status")
    @Oneway
    public void statusOperation(
        @WebParam(name = "Status", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        StatusType parameters)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        final StatusType status = parameters;
        final AddressingProperties inboundAddressProperties
            = (AddressingProperties)ctx.get(JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                CoordinatorCompletionParticipantProcessor.getProcessor().status(status, inboundAddressProperties, arjunaContext) ;
            }
        }) ;
    }
}