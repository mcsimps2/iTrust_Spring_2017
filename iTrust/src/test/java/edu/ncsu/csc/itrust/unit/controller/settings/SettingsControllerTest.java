package edu.ncsu.csc.itrust.unit.controller.settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.controller.settings.SettingsController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.user.ColorSchemeType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class SettingsControllerTest
{
	@Mock
	private SessionUtils mockSessionUtils;
	@Spy
	private SettingsController controller;
	@Spy
	private AuthDAO authDAO;
	private TestDataGenerator gen;
	
	@Before
	public void setUp() throws FileNotFoundException, SQLException, IOException, DBException {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		mockSessionUtils = mock(SessionUtils.class);
		authDAO = spy(TestDAOFactory.getTestInstance().getAuthDAO());
		Mockito.when(mockSessionUtils.getSessionLoggedInMIDLong()).thenReturn(5L);
		controller = spy(new SettingsController(authDAO, mockSessionUtils));
	}
	
	@After
	public void tearDown() throws FileNotFoundException, SQLException, IOException {
		gen.clearAllTables();
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}
	
	@Test
	public void testGetSetColorScheme()
	{
		//Should start with default color scheme
		Assert.assertEquals("Default", controller.getColorScheme());
		
		//Try changing it
		controller.setColorScheme(ColorSchemeType.MUTED_MIDTONES.toString());
		
		//Try getting it again
		Assert.assertEquals(ColorSchemeType.MUTED_MIDTONES.toString(), controller.getColorScheme());
	}
	
	@Test
	public void testGetColorSchemes()
	{
		String[] expected = {"Default", "Dark", "Muted Midtones", "Rainbow", "I Dare You"};
		List<ColorSchemeType> actual = SettingsController.getColorSchemes();
		Assert.assertEquals(expected.length, actual.size());
		for (int i = 0; i < expected.length; i++)
		{
			Assert.assertTrue(actual.contains(ColorSchemeType.matchString(expected[i])));
		}
	}
	
	@Test
	public void testSave() throws DBException
	{
		controller.setColorScheme(ColorSchemeType.DARK.toString());
		controller.save();
		Mockito.verify(authDAO).setColorScheme(Mockito.anyLong(), Mockito.any());
		Mockito.verify(controller).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Assert.assertEquals("Dark", authDAO.getColorScheme(5L).toString());
	}
	
	@Test
	public void testInvalidSave()
	{
		//Try an error path
		Mockito.when(mockSessionUtils.getSessionLoggedInMIDLong()).thenReturn(0L);
		controller = spy(new SettingsController(authDAO, mockSessionUtils));
		
		controller.setColorScheme("Default");
		controller.save();
		Mockito.verify(controller).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	
}
