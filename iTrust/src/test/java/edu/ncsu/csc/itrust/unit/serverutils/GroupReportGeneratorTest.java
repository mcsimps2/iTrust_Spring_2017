package edu.ncsu.csc.itrust.unit.serverutils;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createControl;

import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.easymock.classextension.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.XmlGenerator;
import edu.ncsu.csc.itrust.action.GroupReportGeneratorAction;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.server.GroupReportGeneratorServlet;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

import org.w3c.dom.Document;

import junit.framework.TestCase;

@SuppressWarnings("unused")
public class GroupReportGeneratorTest {

	protected GroupReportGeneratorAction grga;

	private IMocksControl ctrl;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private Document doc;
	private XmlGenerator xmlgen;
	private ArrayList<String> headers = new ArrayList<String>();
	private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		ctrl = createControl();
		req = ctrl.createMock(HttpServletRequest.class);
		resp = ctrl.createMock(HttpServletResponse.class);
		grga = ctrl.createMock(GroupReportGeneratorAction.class);
		xmlgen = ctrl.createMock(XmlGenerator.class);
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}
	
	@Test
	public void testGroupReportGeneratorServletPost() throws Exception {
		String demo = new String();
		String med = new String();
		String pers = new String();
		LittleDelegatorServlet servlet = new LittleDelegatorServlet();
		expect(grga.getReportHeaders()).andReturn(headers);
		expect(grga.getReportData()).andReturn(data);
		expect(req.getParameter("demoparams")).andReturn(demo).anyTimes();
		expect(req.getParameter("medparams")).andReturn(med).anyTimes();
		expect(req.getParameter("persparams")).andReturn(pers).anyTimes();
		resp.sendRedirect("");
		expectLastCall();

		resp.setContentType("application/x-download");
		expectLastCall();
		resp.setHeader((String) EasyMock.anyObject(), (String) EasyMock.anyObject());
		expectLastCall();

		ServletOutputStream sos = ctrl.createMock(ServletOutputStream.class);
		sos.write((byte[]) EasyMock.anyObject(), EasyMock.anyInt(), EasyMock.anyInt());
		expectLastCall().anyTimes();
		sos.flush();
		expectLastCall().anyTimes();
		expect(resp.getOutputStream()).andReturn(sos);

		ctrl.replay();

		servlet.setUp();
		servlet.doPost(req, resp);
	}

	private class LittleDelegatorServlet extends GroupReportGeneratorServlet {

		private static final long serialVersionUID = 1L;

		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
			super.doPost(req, resp);
		}

		public void setUp() {
			factory = TestDAOFactory.getTestInstance();
		}
	}
}