package edu.ncsu.csc.itrust.unit.serverutils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.server.SessionTimeoutListener;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class SessionTimeoutListenerTest {
	private TestDataGenerator gen;
	HttpSessionEvent mockSessionEvent;
	HttpSession mockSession;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen = new TestDataGenerator();
		gen.timeout();
		mockSessionEvent = mock(HttpSessionEvent.class);
		mockSession = mock(HttpSession.class);
	}
	
	@After
	public void tearDown() {
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	// This uses a rudimentary mock object system - where we create these
	// objects that are
	// essentially stubs, except for keeping track of info passed to them.
	@Test
	public void testListenerWorked() throws Exception {
		SessionTimeoutListener listener = new SessionTimeoutListener(TestDAOFactory.getTestInstance());
		HttpSessionEvent event = new MockHttpSessionEvent();
		listener.sessionCreated(event);
		assertEquals(1200, MockHttpSession.mins);
	}

	@Test
	public void testSessionDestroyed() throws Exception {
		SessionTimeoutListener listener = new SessionTimeoutListener(TestDAOFactory.getTestInstance());
		when(mockSessionEvent.getSession()).thenReturn(mockSession);
		when(mockSession.getAttribute("loggedInMID")).thenReturn(1L);
		listener.sessionDestroyed(mockSessionEvent);
		verify(mockSession).getAttribute("loggedInMID");
		
	}

	@Test
	public void testDBException() throws Exception {
		SessionTimeoutListener listener = new SessionTimeoutListener();
		listener.sessionCreated(new MockHttpSessionEvent());
		assertEquals(1200, MockHttpSession.mins);
	}

}
