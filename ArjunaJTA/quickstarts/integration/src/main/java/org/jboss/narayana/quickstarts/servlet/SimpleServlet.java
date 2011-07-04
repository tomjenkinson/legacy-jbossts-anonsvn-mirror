/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates,
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
 * (C) 2011,
 * @author JBoss, by Red Hat.
 */
package org.jboss.narayana.quickstarts.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.jboss.narayana.quickstarts.ejb.SimpleEJB;
import org.jboss.narayana.quickstarts.txoj.AtomicObject;

@WebServlet(displayName = "customer-db", urlPatterns = "/customer-db")
public class SimpleServlet extends HttpServlet {

	@EJB(lookup = "java:module/SimpleEJBImpl")
	private SimpleEJB simpleEJB;

	private AtomicObject atomicObject = new AtomicObject();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>Customer Database</title></head>");
		out.println("<body>");
		out.println("<h1>Customer Database</h2>");
		String operation = request.getParameter("operation");
		if (operation != null) {
			if (operation.equals("list")) {
				out.println(listCustomers());
			} else if (operation.equals("create")) {
				String name = request.getParameter("name");
				if (name != null) {
					out.println(createCustomer(name));
				} else {
					out.println(error("Requires name parameter"));
				}
			} else {
				out.println(error("Unknown operation: " + operation));
			}
		} else {
			out.println("<form method=\"get\" >");
			out.println("<input type=\"text\" name=\"name\" size=30>");
			out.println("<input type=\"hidden\" name=\"operation\" value=\"create\">");
			out.println("<input type=\"submit\" value=\"Create customer\">");
			out.println("</form>");

			out.println("<form method=\"get\" >");
			out.println("<input type=\"hidden\" name=\"operation\" value=\"list\">");
			out.println("<input type=\"submit\" value=\"List customers\">");
			out.println("</form>");
		}
		out.println("</body>");
		out.println("</html>");
	}

	private String error(String error) {
		StringBuffer toWrite = new StringBuffer();
		toWrite.append("<h1>Invalid parameters</h1>\n");
		toWrite.append("<p>" + error + "</p>");
		return toWrite.toString();
	}

	public String listCustomers() {
		StringBuffer toWrite = new StringBuffer();
		toWrite.append("<h1>List of customer Ids</h1>\n");

		try {
			toWrite.append("<p>Customers created this run: "
					+ atomicObject.get() + "</p>\n");

			UserTransaction tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			tx.begin();
			toWrite.append("<p>" + simpleEJB.listCustomers() + "</p>");
			tx.commit();
		} catch (Throwable e) {
			toWrite.append("FAILED: " + e.toString());
		}
		return toWrite.toString();
	}

	public String createCustomer(String name) {
		StringBuffer toWrite = new StringBuffer();
		toWrite.append("<h1>Update summary</h1>\n");

		try {
			UserTransaction tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			tx.begin();
			atomicObject.incr(1);
			simpleEJB.createCustomer(name);
			toWrite.append("<p>Created: " + name + "</p>");
			tx.commit();
		} catch (Throwable e) {
			toWrite.append("FAILED: " + e.toString());
		}
		return toWrite.toString();
	}

	public String getCustomerCount() {
		StringBuffer toWrite = new StringBuffer();
		toWrite.append("<h1>Customer count</h1>\n");

		try {
			UserTransaction tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			tx.begin();
			toWrite.append("<p>Customers created this run: "
					+ atomicObject.get() + "</p>");
			tx.commit();
		} catch (Throwable e) {
			toWrite.append("FAILED: " + e.toString());
		}
		return toWrite.toString();
	}
}
