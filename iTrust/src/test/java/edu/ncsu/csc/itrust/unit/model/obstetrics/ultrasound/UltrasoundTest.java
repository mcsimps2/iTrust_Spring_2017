package edu.ncsu.csc.itrust.unit.model.obstetrics.ultrasound;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.Ultrasound;

/**
 * Unit test for ultrasound pojo
 * @author jcgonzal
 */
public class UltrasoundTest {

	/** Ultrasound object with id */
	Ultrasound ultrasound1;
	
	/** Ultrasound object without id */
	Ultrasound ultrasound2;	
	
	@Before
	public void setUp() throws Exception {
		ultrasound1 = new Ultrasound(new Long(1), new Long(2), new Float(0.1), new Float(0.2), new Float(0.3), new Float(0.4), new Float(0.5), new Float(0.6), new Float(0.7), new Float(0.8));
		ultrasound2 = new Ultrasound(new Long(3), new Float(0.9), new Float(1.0), new Float(1.1), new Float(1.2), new Float(1.3), new Float(1.4), new Float(1.5), new Float(1.6));
		
	}

	@Test
	public void testGetSetId() {
		Assert.assertTrue(ultrasound1.getId().equals(new Long(1)));
		ultrasound1.setId(new Long(4));
		Assert.assertTrue(ultrasound1.getId().equals(new Long(4)));
		Assert.assertNull(ultrasound2.getId());
	}

	@Test
	public void testGetSetOfficeVisitID() {
		Assert.assertTrue(ultrasound1.getOfficeVisitID().equals(new Long(2)));
		ultrasound1.setOfficeVisitID(new Long(5));
		Assert.assertTrue(ultrasound1.getOfficeVisitID().equals(new Long(5)));
	}

	@Test
	public void testGetSetCrl() {
		Assert.assertTrue(ultrasound1.getCrl().equals(new Float(0.1)));
		ultrasound1.setCrl(new Float(1.7));
		Assert.assertTrue(ultrasound1.getCrl().equals(new Float(1.7)));
	}
	
	@Test
	public void testGetSetBpd() {
		Assert.assertTrue(ultrasound1.getBpd().equals(new Float(0.2)));
		ultrasound1.setBpd(new Float(1.8));
		Assert.assertTrue(ultrasound1.getBpd().equals(new Float(1.8)));
	}

	@Test
	public void testGetSetHc() {
		Assert.assertTrue(ultrasound1.getHc().equals(new Float(0.3)));
		ultrasound1.setHc(new Float(1.9));
		Assert.assertTrue(ultrasound1.getHc().equals(new Float(1.9)));
	}

	@Test
	public void testSetFl() {
		Assert.assertTrue(ultrasound1.getFl().equals(new Float(0.4)));
		ultrasound1.setFl(new Float(2.0));
		Assert.assertTrue(ultrasound1.getFl().equals(new Float(2.0)));
	}

	@Test
	public void testGetSetOfd() {
		Assert.assertTrue(ultrasound1.getOfd().equals(new Float(0.5)));
		ultrasound1.setOfd(new Float(2.1));
		Assert.assertTrue(ultrasound1.getOfd().equals(new Float(2.1)));
	}

	@Test
	public void testGetSetAc() {
		Assert.assertTrue(ultrasound1.getAc().equals(new Float(0.6)));
		ultrasound1.setAc(new Float(2.2));
		Assert.assertTrue(ultrasound1.getAc().equals(new Float(2.2)));
	}

	@Test
	public void testGetSetHl() {
		Assert.assertTrue(ultrasound1.getHl().equals(new Float(0.7)));
		ultrasound1.setHl(new Float(2.3));
		Assert.assertTrue(ultrasound1.getHl().equals(new Float(2.3)));
	}
	
	@Test
	public void testGetSetEfw() {
		Assert.assertTrue(ultrasound1.getEfw().equals(new Float(0.8)));
		ultrasound1.setEfw(new Float(2.4));
		Assert.assertTrue(ultrasound1.getEfw().equals(new Float(2.4)));
	}
	
	@Test
	public void testGettersSetters()
	{
		Ultrasound us = new Ultrasound(1L);
		Ultrasound us2 = new Ultrasound(1L);
		Assert.assertEquals(us.hashCode(), us2.hashCode());
	}

}
