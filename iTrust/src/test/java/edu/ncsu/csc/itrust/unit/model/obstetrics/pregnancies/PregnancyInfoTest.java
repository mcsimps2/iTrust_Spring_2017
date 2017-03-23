package edu.ncsu.csc.itrust.unit.model.obstetrics.pregnancies;

import org.junit.*;

import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfo;

public class PregnancyInfoTest {
	
	@Test
	public void testEqualsAndHashCode()
	{
		PregnancyInfo pi1 = new PregnancyInfo(5, 2, 1, 2015, 200, 15, 25.0, DeliveryMethod.VAGINAL_DELIVERY_FORCEPS, 1);
		Assert.assertEquals(pi1, pi1);
		Assert.assertNotEquals(null, pi1);
		PregnancyInfo pi2 = new PregnancyInfo(5, 2, 1, 2015, 200, 15, 25.0, DeliveryMethod.VAGINAL_DELIVERY_FORCEPS, 1);
		Assert.assertEquals(pi1, pi2);
		Assert.assertEquals(pi1.hashCode(), pi2.hashCode());
		pi2.setNumDaysPregnant(1);
		Assert.assertNotEquals(pi1, pi2);
	}
}
