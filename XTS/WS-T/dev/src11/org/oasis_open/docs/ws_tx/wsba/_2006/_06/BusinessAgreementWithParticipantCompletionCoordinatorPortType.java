
package org.oasis_open.docs.ws_tx.wsba._2006._06;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.2-hudson-182-RC1
 * Generated source version: 2.0
 * 
 */
@WebService(name = "BusinessAgreementWithParticipantCompletionCoordinatorPortType", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface BusinessAgreementWithParticipantCompletionCoordinatorPortType {


    /**
     * 
     * @param parameters
     */
    @WebMethod(operationName = "CompletedOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Completed")
    @Oneway
    public void completedOperation(
        @WebParam(name = "Completed", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters);

    /**
     * 
     * @param parameters
     */
    @WebMethod(operationName = "FailOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Fail")
    @Oneway
    public void failOperation(
        @WebParam(name = "Fail", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        ExceptionType parameters);

    /**
     * 
     * @param parameters
     */
    @WebMethod(operationName = "CompensatedOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Compensated")
    @Oneway
    public void compensatedOperation(
        @WebParam(name = "Compensated", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters);

    /**
     * 
     * @param parameters
     */
    @WebMethod(operationName = "ClosedOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Closed")
    @Oneway
    public void closedOperation(
        @WebParam(name = "Closed", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters);

    /**
     * 
     * @param parameters
     */
    @WebMethod(operationName = "CanceledOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Canceled")
    @Oneway
    public void canceledOperation(
        @WebParam(name = "Canceled", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters);

    /**
     * 
     * @param parameters
     */
    @WebMethod(operationName = "ExitOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Exit")
    @Oneway
    public void exitOperation(
        @WebParam(name = "Exit", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters);

    /**
     * 
     * @param parameters
     */
    @WebMethod(operationName = "CannotComplete", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/CannotComplete")
    @Oneway
    public void cannotComplete(
        @WebParam(name = "CannotComplete", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters);

    /**
     * 
     * @param parameters
     */
    @WebMethod(operationName = "GetStatusOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/GetStatus")
    @Oneway
    public void getStatusOperation(
        @WebParam(name = "GetStatus", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        NotificationType parameters);

    /**
     * 
     * @param parameters
     */
    @WebMethod(operationName = "StatusOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Status")
    @Oneway
    public void statusOperation(
        @WebParam(name = "Status", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsba/2006/06", partName = "parameters")
        StatusType parameters);

}
