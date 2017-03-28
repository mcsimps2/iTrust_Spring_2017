package edu.ncsu.csc.itrust.unit.model.obstetrics.visit;

import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;

/**
 * Test pojo for obstetrics visit
 * @author jcgonzal
 */
public class ObstetricsVisitTest {

	/** Pojo with UID */
	ObstetricsVisit o1;
	
	/** Pojo without UID */
	ObstetricsVisit o2;
	
	@Before
	public void setUp() throws Exception {
		byte[] b1 = new byte[100];
		Arrays.fill(b1, (byte)0);
		byte[] b2 = new byte[12];
		Arrays.fill(b2, (byte)1);
		o1 = new ObstetricsVisit(new Long(1), new Long(2), new Integer(13), new Integer(54), new Integer(3), new Boolean(true), new SerialBlob(b1));
		o2 = new ObstetricsVisit(new Long(3), new Integer(14), new Integer(55), new Integer(4), new Boolean(false), new SerialBlob(b2));
	}

	@Test
	public void testGetSetId() {
		Assert.assertTrue(o1.getId().equals(new Long(1)));
		o1.setId(new Long(4));
		Assert.assertTrue(o1.getId().equals(new Long(4)));
		Assert.assertNull(o2.getId());
	}

	@Test
	public void testGetSetOfficeVisitID() {
		Assert.assertTrue(o1.getOfficeVisitID().equals(new Long(2)));
		o1.setOfficeVisitID(new Long(5));
		Assert.assertTrue(o1.getOfficeVisitID().equals(new Long(5)));
	}

	@Test
	public void testGetSetWeeksPregnant() {
		Assert.assertTrue(o1.getWeeksPregnant().equals(new Integer(13)));
		o1.setWeeksPregnant(new Integer(15));
		Assert.assertTrue(o1.getWeeksPregnant().equals(new Integer(15)));
	}

	@Test
	public void testGetSetFhr() {
		Assert.assertTrue(o1.getFhr().equals(new Integer(54)));
		o1.setFhr(new Integer(56));
		Assert.assertTrue(o1.getFhr().equals(new Integer(56)));
	}

	@Test
	public void testGetSetMultiplicity() {
		Assert.assertTrue(o1.getMultiplicity().equals(new Integer(3)));
		o1.setMultiplicity(new Integer(5));
		Assert.assertTrue(o1.getMultiplicity().equals(new Integer(5)));
	}

	@Test
	public void testGetSetLowLyingPlacentaObserved() {
		Assert.assertTrue(o1.isLowLyingPlacentaObserved().equals(new Boolean(true)));
		o1.setLowLyingPlacentaObserved(new Boolean(false));
		Assert.assertTrue(o1.isLowLyingPlacentaObserved().equals(new Boolean(false)));
	}

	@Test
	public void testGetSetImageOfUltrasound() {
		byte[] b1 = new byte[100];
		Arrays.fill(b1, (byte)0);
		byte[] b2 = new byte[64];
		Arrays.fill(b2, (byte)2);
		try {
			Assert.assertTrue(o1.getImageOfUltrasound().equals(new SerialBlob(b1)));
			o1.setImageOfUltrasound(new SerialBlob(b2));
			Assert.assertTrue(o1.getImageOfUltrasound().equals(new SerialBlob(b2)));
		} catch (SerialException e) {
			Assert.fail(e.getMessage());
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
	}

}