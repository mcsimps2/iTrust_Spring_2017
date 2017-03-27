package edu.ncsu.csc.itrust.unit.controller.obstetrics.officeVisit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.junit.*;

import com.google.api.client.util.DateTime;

import edu.ncsu.csc.itrust.controller.obstetrics.officeVisit.GoogleScheduler;
import edu.ncsu.csc.itrust.controller.obstetrics.officeVisit.GoogleSchedulerException;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class GoogleSchedulerTest
{
	
	private final String CAL_ID = "ncsu.edu_flngq1u6biusd8qhces7tom0b0@group.calendar.google.com";
	private final String CAL_ID2 = "ncsu.edu_h6hj0pngqpdfv861d0qot7mlkc@group.calendar.google.com";
	Date d_curr;
	@Before
	public void setup() throws Exception
	{
		DBBuilder.main(null);
		TestDataGenerator.main(null);
		GoogleScheduler.useTestFactory();
		d_curr = new Date();
	}
	
	@After
	public void breakdown()
	{
		GoogleScheduler.useContextFactory();
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetAppts()
	{
		Timestamp ts_start = new Timestamp(2011-1900, 8, 22, 13, 30, 0, 0);
		
		Timestamp ts_end = new Timestamp(d_curr.getTime() + 9*86400000);
		//Timestamp ts_end = ts_start.setTime();
		//DateTime dt = new DateTime((new Timestamp("2012-08-22 13:30:00")));
		try
		{
			List<DateTime[]> list = GoogleScheduler.getAppts(9000000010L, new DateTime(ts_start.getTime()), new DateTime(ts_end.getTime()));
			System.out.println("Size: " + list.size());
			for (int i = 0; i < list.size(); i++)
			{
				System.out.println(list.get(i)[0].toStringRfc3339());
				System.out.println(list.get(i)[1].toStringRfc3339());
				System.out.println();
			}
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetGoogleEvents()
	{
		String[][] expected = { 
				{"2017-03-27T11:00:00.000-04:00", "2017-03-27T15:00:00.000-04:00"},
				{"2017-03-28T11:00:00.000-04:00", "2017-03-28T15:00:00.000-04:00"}
		};
		
		Timestamp ts_start = new Timestamp(2017-1900, 3-1, 27, 0, 0, 0, 0);
		Timestamp ts_end = new Timestamp (2017-1900, 3-1, 29, 0, 0, 0, 0);
		DateTime dt_start = new DateTime(ts_start);
		DateTime dt_end = new DateTime(ts_end);
		try
		{
			List<DateTime[]> list = GoogleScheduler.getGoogleEvents(CAL_ID2, dt_start, dt_end);
			//Assert.assertEquals(expected.length, list.size());
			for (int i = 0; i < list.size(); i++)
			{
				//System.out.println(list.get(i)[0].toStringRfc3339() + "\t" + list.get(i)[1].toStringRfc3339());
				Assert.assertEquals(expected[i].length, list.get(i).length);
				for (int j = 0; j < list.get(i).length; j++)
				{
					Assert.assertEquals(expected[i][j], list.get(i)[j].toStringRfc3339());
				}
			}
		} 
		catch (JSONException e)
		{
			Assert.fail(e.getMessage());
		} 
		catch (MalformedURLException e)
		{
			Assert.fail(e.getMessage());
		} 
		catch (IOException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testScheduleAppointment()
	{
		
		ApptDAO apptdao = TestDAOFactory.getTestInstance().getApptDAO();
		
		Calendar cal = Calendar.getInstance();
		OfficeVisit ov = new OfficeVisit();
		
		//Appointment for 10 AM - should always work
		ov.setDate(LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 10, 0));
		
		try
		{
			ApptBean appt = GoogleScheduler.scheduleAppointment(9000000010L, 1, CAL_ID2, 1, 30, ov);
			Assert.assertNotNull(appt);
			
			List<ApptBean> appts = apptdao.getApptsFor(9000000010L);
			boolean found = false;
			//Unforunate, the old equals method won't work for us
			for (int i = 0; i < appts.size(); i++)
			{
				if (appts.get(i).getDate().equals(appt.getDate()))
				{
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		catch (GoogleSchedulerException | DBException | SQLException e)
		{
			Assert.fail(e.getMessage());
		}
		
		
		//This one should never work, 11:00 AM is always taken
		ov.setDate(LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 11, 0));
		try
		{
			GoogleScheduler.scheduleAppointment(9000000010L, 1, CAL_ID2, 1, 30, ov);
			Assert.fail("Should have gotten an error");
		}
		catch (GoogleSchedulerException e)
		{
			Assert.assertEquals("Unable to schedule an appointment: Too many attempts", e.getMessage());
		}
		
		
		//But this one should (right at the boundary, 3:00 is OK)
		ov.setDate(LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 15, 0));
		try
		{
			ApptBean appt = GoogleScheduler.scheduleAppointment(9000000010L, 1, CAL_ID2, 1, 30, ov);
			Assert.assertNotNull(appt);
			
			List<ApptBean> appts = apptdao.getApptsFor(9000000010L);
			boolean found = false;
			for (int i = 0; i < appts.size(); i++)
			{
				if (appts.get(i).getDate().equals(appt.getDate()))
				{
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		catch (GoogleSchedulerException | DBException | SQLException e)
		{
			Assert.fail(e.getMessage());
		}
		
		//This should default to 9:00 AM, since appointments at 1:00 AM are not okay
		ov.setDate(LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 1, 0));
		try
		{
			ApptBean appt = GoogleScheduler.scheduleAppointment(9000000010L, 1, CAL_ID2, 1, 30, ov);
			Assert.assertNotNull(appt);
			
			List<ApptBean> appts = apptdao.getApptsFor(9000000010L);
			boolean found = false;
			for (int i = 0; i < appts.size(); i++)
			{
				if (appts.get(i).getDate().equals(appt.getDate()))
				{
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
			Assert.assertTrue(appt.getDate().getHours() == 9);
			Assert.assertTrue(appt.getDate().getMinutes() == 0);
		}
		catch (GoogleSchedulerException | DBException | SQLException e)
		{
			Assert.fail(e.getMessage());
		}
		
		//This should default to 4:00 PM, since appointments at 4:05 PM are not okay
		ov.setDate(LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 16, 5));
		try
		{
			ApptBean appt = GoogleScheduler.scheduleAppointment(9000000010L, 1, CAL_ID2, 1, 30, ov);
			Assert.assertNotNull(appt);
			
			List<ApptBean> appts = apptdao.getApptsFor(9000000010L);
			boolean found = false;
			for (int i = 0; i < appts.size(); i++)
			{
				if (appts.get(i).getDate().equals(appt.getDate()))
				{
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
			Assert.assertTrue(appt.getDate().getHours() == 16);
			Assert.assertTrue(appt.getDate().getMinutes() == 0);
		}
		catch (GoogleSchedulerException | DBException | SQLException e)
		{
			Assert.fail(e.getMessage());
		}
		
		//HCP 10 has an appointment scheduled 7 days from now at 1:45 PM
		//For CAL_ID, there is nothing during this time
		//Make sure we can't schedule 7 days from now at 1:45 PM, but rather 8 days is OK
		cal.add(Calendar.DATE, 7);
		cal.set(Calendar.HOUR_OF_DAY, 13);
		cal.set(Calendar.MINUTE, 45);
		ov.setDate(LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE), 13, 45));
		try
		{
			ApptBean appt = GoogleScheduler.scheduleAppointment(9000000010L, 1, CAL_ID, 1, 30, ov);
			Assert.assertNotNull(appt);
			
			List<ApptBean> appts = apptdao.getApptsFor(9000000010L);
			boolean found = false;
			for (int i = 0; i < appts.size(); i++)
			{
				if (appts.get(i).getDate().equals(appt.getDate()))
				{
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
			Assert.assertTrue(appt.getDate().getHours() == 13);
			Assert.assertTrue(appt.getDate().getMinutes() == 45);
			cal.add(Calendar.DATE, 1);
			Assert.assertEquals(appt.getDate().getDate(), cal.get(Calendar.DATE));
		}
		catch (GoogleSchedulerException | DBException | SQLException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	
	
	
}
