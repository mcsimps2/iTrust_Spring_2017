package edu.ncsu.csc.itrust.unit.model.obstetrics.visit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitSQLLoader;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Tests for obstetrics visit sql loader
 * @author jcgonzal
 */
public class ObstetricsVisitSQLLoaderTest {

	/** Loader obj to test */
	ObstetricsVisitSQLLoader loader;
	/** Data source to test with */
	DataSource ds;
	/**Data to test against */
	ObstetricsVisit ov;
	
	@Before
	public void setUp() throws Exception {
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		loader = new ObstetricsVisitSQLLoader();
		ds = ConverterDAO.getDataSource();
		
		ov = new ObstetricsVisit(new Long(1), new Long(51), new Long(3), new Integer(22), new Integer(130), new Integer(1), new Boolean(false), null, "image.jpg");
		
	}

	@Test
	public void testLoadList() {
		String stmt = "SELECT * FROM obstetricsVisit WHERE id=1";
		try (Connection conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(stmt);
			ResultSet rs = ps.executeQuery();)
		{
			List<ObstetricsVisit> list = loader.loadList(rs);
			Assert.assertTrue(list.get(0).equals(ov));
		} catch (SQLException e)
		{
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testLoadSingle() {
		String stmt = "SELECT * FROM obstetricsVisit WHERE id=1";
		try (Connection conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(stmt);
			ResultSet rs = ps.executeQuery();)
		{
			rs.next();
			ObstetricsVisit u = loader.loadSingle(rs);
			Assert.assertTrue(u.equals(ov));
		} catch (SQLException e)
		{
			Assert.fail(e.getMessage());
		}
	}

}
