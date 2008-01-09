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
package com.arjuna.xts.nightout.clients.weblogic.proxies;
/**
 * Generated class, do not edit.
 *
 * This stub class was generated by weblogic
 * webservice stub gen on Wed Jul 21 12:42:35 BST 2004 */

public class TaxiServiceATPort_Stub 
     extends weblogic.webservice.core.rpc.StubImpl 
     implements  com.arjuna.xts.nightout.clients.weblogic.proxies.TaxiServiceATPort{

  public TaxiServiceATPort_Stub( weblogic.webservice.Port _port ){
    super( _port, com.arjuna.xts.nightout.clients.weblogic.proxies.TaxiServiceATPort.class );
  }

  /**
   * bookTaxi 
   */

  public void bookTaxi() 
       throws java.rmi.RemoteException {

    java.util.HashMap _args = new java.util.HashMap();
    try{
      java.lang.Object _result = _invoke( "bookTaxi", _args );
              } catch (javax.xml.rpc.JAXRPCException e) {
      throw new java.rmi.RemoteException( e.getMessage(), e.getLinkedCause() );
    } catch (javax.xml.rpc.soap.SOAPFaultException e) {
      throw new java.rmi.RemoteException( "SOAP Fault:" + e + "\nDetail:\n"+e.getDetail(), e );
    } catch (java.lang.Throwable e) {
      throw new java.rmi.RemoteException( e.getMessage(), e );    }
  }
}
