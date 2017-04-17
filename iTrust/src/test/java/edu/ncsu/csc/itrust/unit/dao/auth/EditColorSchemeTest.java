package edu.ncsu.csc.itrust.unit.dao.auth;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.user.ColorSchemeType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class EditColorSchemeTest
{
	AuthDAO dao;
	
	@Before
	public void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		dao = TestDAOFactory.getTestInstance().getAuthDAO();
	}
	
	@Test
	public void testEditColorScheme()
	{
		try
		{
			Assert.assertEquals(ColorSchemeType.DEFAULT, dao.getColorScheme(1L));
			dao.setColorScheme(1L, ColorSchemeType.MUTED_MIDTONES);
			Assert.assertEquals(ColorSchemeType.MUTED_MIDTONES, dao.getColorScheme(1L));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
}
