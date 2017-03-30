package edu.ncsu.csc.itrust.unit.model.obstetrics.ultrasound;

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
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.Ultrasound;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.UltrasoundSQLLoader;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Test the UltrasoundSQLLoader
 * @author jcgonzal
 */
public class UltrasoundSQLLoaderTest {

	/** Loader to test */
	UltrasoundSQLLoader loader;
	/** DataSource to test with */
	DataSource ds;
	/** Array of data to test against */
	Ultrasound[] uArr;
	
	@Before
	public void setUp() throws Exception {
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		loader = new UltrasoundSQLLoader();
		ds = ConverterDAO.getDataSource();
		
		uArr = new Ultrasound[2];
		uArr[0] = new Ultrasound(new Long(1), new Long(51), new Float(1.1), new Float(2.2), new Float(3.3), new Float(4.4), new Float(5.5), new Float(6.6), new Float(7.7), new Float(8.8));
		uArr[1] = new Ultrasound(new Long(2), new Long(51), new Float(1.2), new Float(2.3), new Float(3.4), new Float(4.5), new Float(5.6), new Float(6.7), new Float(7.8), new Float(8.9));
	}

	@Test
	public void testLoadList() {
		try {
			Connection conn = ds.getConnection();
			String stmt = "SELECT * FROM ultrasound";
			PreparedStatement ps = conn.prepareStatement(stmt);
			ResultSet rs = ps.executeQuery();
			List<Ultrasound> list = loader.loadList(rs);
			for (int i = 0; i < uArr.length; i++)
			{
				Assert.assertTrue(list.contains(uArr[i]));
			}
		} catch (SQLException e)
		{
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testLoadSingle() {
		try {
			Connection conn = ds.getConnection();
			String stmt = "SELECT * FROM ultrasound WHERE id=1";
			PreparedStatement ps = conn.prepareStatement(stmt);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Ultrasound u = loader.loadSingle(rs);
			Assert.assertTrue(u.equals(uArr[0]));
		} catch (SQLException e)
		{
			Assert.fail(e.getMessage());
		}
	}

//	@Test
//	public void testLoadParameters() {
//		try {
//			Connection conn = ds.getConnection();
//			String insertStmt = "INSERT INTO ultrasound(officeVisitID, crl, bpd, hc, fl, ofd, ac, hl, efw) VALUES (51, 1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8);";
//			PreparedStatement insertPS = conn.prepareStatement(insertStmt);
//			Assert.assertTrue(loader.loadParameters(conn, null, uArr[0], true).equals(insertPS));
//			String updateStmt = "UPDATE ultrasound SET "
//					+ "officeVisitID=51, "
//					+ "crl=1.2, "
//					+ "bpd=2.3, "
//					+ "hc=3.4, "
//					+ "fl=4.5, "
//					+ "ofd=5.6, "
//					+ "ac=6.7, "
//					+ "hl=7.8, "
//					+ "efw=8.2 "
//					+ "WHERE id=" + 2 + ";";
//			PreparedStatement updatePS = conn.prepareStatement(updateStmt);
//			Assert.assertTrue(loader.loadParameters(conn, null, uArr[1], false).equals(updatePS));
//		} catch (SQLException e)
//		{
//			Assert.fail(e.getMessage());
//		}
//	}
//
//	@Test
//	public void testLoadDelete() {
//		try {
//			Connection conn = ds.getConnection();
//			PreparedStatement pstring = conn.prepareStatement("DELETE FROM ultrasound WHERE id=?");
//	        pstring.setLong(1, 1);
//	        loader.loadDelete(conn, 1);
//		} catch (SQLException e)
//		{
//			Assert.fail(e.getMessage());
//		}
//	}

}
