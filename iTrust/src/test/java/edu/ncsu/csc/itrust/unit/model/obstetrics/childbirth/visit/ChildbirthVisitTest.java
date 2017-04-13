package edu.ncsu.csc.itrust.unit.model.obstetrics.childbirth.visit;

import org.junit.*;

import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.VisitType;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
public class ChildbirthVisitTest
{
	@Test
	public void testEqualsAndHashCode()
	{
		ChildbirthVisit cv1 = new ChildbirthVisit(1L, 3L, DeliveryMethod.VAGINAL_DELIVERY, VisitType.PRE_SCHEDULED_APPOINTMENT, 5, 4, 3, 2, 1, 1);
		ChildbirthVisit cv2 = new ChildbirthVisit(1L, 3L, DeliveryMethod.VAGINAL_DELIVERY, VisitType.PRE_SCHEDULED_APPOINTMENT, 5, 4, 3, 2, 1, 1);
		Assert.assertNotEquals(cv1, this);
		Assert.assertEquals(cv1.hashCode(), cv2.hashCode());
		Assert.assertEquals(cv1, cv1);
		Assert.assertNotEquals(cv1, null);
		Assert.assertEquals(cv1, cv2);
		cv2.setEpiduralAnaesthesia(10);
		Assert.assertNotEquals(cv1, cv2);
		cv2.setEpiduralAnaesthesia(null);
		Assert.assertNotEquals(cv2, cv1);
		cv2.setEpiduralAnaesthesia(2);
		cv2.setMagnesiumSulfate(10);
		Assert.assertNotEquals(cv1, cv2);
		cv2.setMagnesiumSulfate(null);
		Assert.assertNotEquals(cv2, cv1);
		cv2.setMagnesiumSulfate(1);
		cv2.setDeliveryType(DeliveryMethod.MISCARRIAGE);
		Assert.assertNotEquals(cv1, cv2);
		cv2.setDeliveryType(DeliveryMethod.VAGINAL_DELIVERY);
		cv2.setVisitType(VisitType.EMERGENCY_APPOINTMENT);
		Assert.assertNotEquals(cv1, cv2);
		cv2.setVisitType(VisitType.PRE_SCHEDULED_APPOINTMENT);
		cv2.setOfficeVisitID(5L);
		Assert.assertNotEquals(cv1, cv2);
		cv2.setOfficeVisitID(null);
		Assert.assertNotEquals(cv2, cv1);
		cv2.setOfficeVisitID(1L);
		Assert.assertEquals(cv1, cv2);
		cv2.setPitocin(10);
		Assert.assertNotEquals(cv1, cv2);
		cv2.setPitocin(null);
		Assert.assertNotEquals(cv2, cv1);
		cv2.setPitocin(5);
		cv2.setNitrousOxide(10);
		Assert.assertNotEquals(cv1, cv2);
		cv2.setNitrousOxide(null);
		Assert.assertNotEquals(cv2, cv1);
		cv2.setNitrousOxide(4);
		cv2.setPethidine(10);
		Assert.assertNotEquals(cv1, cv2);
		cv2.setPethidine(null);
		Assert.assertNotEquals(cv2, cv1);
	}
}
