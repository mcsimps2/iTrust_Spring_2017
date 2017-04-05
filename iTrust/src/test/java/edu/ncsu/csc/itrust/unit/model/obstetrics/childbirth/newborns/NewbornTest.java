package edu.ncsu.csc.itrust.unit.model.obstetrics.childbirth.newborns;


import org.junit.*;

import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.Newborn;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.SexType;

public class NewbornTest
{
	@Test
	public void testEqualsAndHashCode()
	{
		Newborn nb1 = new Newborn(999L, 1L, "2017-01-01", "15:15:15", SexType.FEMALE, false);
		Newborn nb2 = new Newborn(999L, 1L, "2017-01-01", "15:15:15", SexType.FEMALE, false);
		Assert.assertEquals(nb1.hashCode(), nb2.hashCode());
		Assert.assertNotEquals(nb1, null);
		Assert.assertNotEquals(nb1, this);
		Assert.assertEquals(nb1, nb1);
		nb2.setDateOfBirth("2015-01-01");
		Assert.assertNotEquals(nb1, nb2);
		nb2.setDateOfBirth(null);
		Assert.assertNotEquals(nb2, nb1);
		nb2.setDateOfBirth("2017-01-01");
		Assert.assertEquals(nb1, nb2);
		
		nb2.setTimeOfBirth("15:12:12");
		Assert.assertNotEquals(nb1, nb2);
		nb2.setTimeOfBirth(null);
		Assert.assertNotEquals(nb2, nb1);
		nb2.setTimeOfBirth("15:15:15");
		Assert.assertEquals(nb1, nb2);
		
		nb2.setOfficeVisitID(3L);
		Assert.assertNotEquals(nb1, nb2);
		nb2.setOfficeVisitID(null);
		Assert.assertNotEquals(nb2, nb1);
		nb2.setOfficeVisitID(1L);
		Assert.assertEquals(nb1, nb2);
		
		nb2.setTimeEstimated(true);
		Assert.assertNotEquals(nb1, nb2);
		nb2.setTimeEstimated(null);
		Assert.assertNotEquals(nb2, nb1);
		nb2.setTimeEstimated(false);
		Assert.assertEquals(nb1, nb2);
	}
}
