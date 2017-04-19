package edu.ncsu.csc.itrust.observer.fitness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.fitness.FitnessInfoController;
import edu.ncsu.csc.itrust.controller.fitness.FitnessInfoController.GraphKeyValue;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;

/**
 * Graph observer implementation for the Weekly Average Graph
 * @author jcgonzal
 *
 */
public class WeeklyAvgGraphObserver extends GraphObserver {

	/**
	 * Constructor with data source
	 */
	public WeeklyAvgGraphObserver(DataSource ds) {
		super(ds);
		this.setFitnessController(new FitnessInfoController(ds));
	}
	
	/**
	 * Constructor
	 */
	public WeeklyAvgGraphObserver() {
		super();
		this.setFitnessController(new FitnessInfoController());
	}
	
	/**
	 * Implementation of the update data function to update data based on start and end dates.
	 */
	@SuppressWarnings("deprecation")
	public void updateData(Long pid, String startDate, String endDate) {
		ArrayList<GraphKeyValue> data = new ArrayList<GraphKeyValue>();
		
		Date currentD = FitnessInfo.stringToJavaDate(startDate);
		//Set to first day of week
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentD);
		cal.add(Calendar.DATE, currentD.getDay() * -1);
		currentD = cal.getTime();
		Date endD = FitnessInfo.stringToJavaDate(endDate);
		//Set to last day
		cal.setTime(endD);
		cal.add(Calendar.DATE, 6 - endD.getDay());
		endD = cal.getTime();
		
		FitnessInfo[] fitnessInfos;
		if (pid == null) fitnessInfos = this.getFitnessController().getFitnessInfo(FitnessInfo.dateToString(currentD), FitnessInfo.dateToString(endD));
		else fitnessInfos = this.getFitnessController().getFitnessInfo(pid, FitnessInfo.dateToString(currentD), FitnessInfo.dateToString(endD));
		
		if (fitnessInfos == null || fitnessInfos.length == 0) {
			this.getFitnessController().printFacesMessage(FacesMessage.SEVERITY_ERROR, NO_DATA_FOUND_IN_DATES, NO_DATA_FOUND_IN_DATES, null);
			return;
		}
		
		Arrays.sort(fitnessInfos, new Comparator<FitnessInfo>() {
			@Override
			public int compare(FitnessInfo arg0, FitnessInfo arg1) {
				if (arg0.getJavaDate().equals(arg1.getJavaDate())) return 0;
				if (arg0.getJavaDate().before(arg1.getJavaDate())) return -1;
				else return 1;
			}
		});
		
		int currentTotal = 0;
		int currentDenom = 0;
		int numProcessed = 0;
		String currentWeek = "";
		
		for (FitnessInfo info : fitnessInfos){
			while (currentD.before(info.getJavaDate())) {
				if (numProcessed == 0) currentWeek = FitnessInfo.dateToString(currentD);
				//Process current date, which since there is no data here means add 0 to total and denom
				numProcessed++;
				if (numProcessed == 7) {
					if (currentDenom == 0) {
						data.add(new GraphKeyValue(currentWeek, null));
					} else {
						data.add(new GraphKeyValue(currentWeek, currentTotal / currentDenom));
					}
					numProcessed = 0;
					currentTotal = 0;
					currentDenom = 0;
				}
				cal.setTime(currentD);
				cal.add(Calendar.DATE, 1);
				currentD = cal.getTime();
			}
			if (numProcessed == 0) currentWeek = FitnessInfo.dateToString(currentD);
			currentTotal += info.getSteps();
			currentDenom++;
			numProcessed++;
			if (numProcessed == 7) {
				data.add(new GraphKeyValue(currentWeek, currentTotal / currentDenom));
				numProcessed = 0;
				currentTotal = 0;
				currentDenom = 0;
			}
			cal.setTime(currentD);
			cal.add(Calendar.DATE, 1);
			currentD = cal.getTime();
		}
		
		while (currentD.before(endD) || currentD.equals(endD)) {
			if (numProcessed == 0) currentWeek = FitnessInfo.dateToString(currentD);
			//Process current date, which since there is no data here means add 0 to total and denom
			numProcessed++;
			if (numProcessed == 7) {
				if (currentDenom == 0) {
					data.add(new GraphKeyValue(currentWeek, null));
				} else {
					data.add(new GraphKeyValue(currentWeek, currentTotal / currentDenom));
				}
				numProcessed = 0;
				currentTotal = 0;
				currentDenom = 0;
			}
			cal.setTime(currentD);
			cal.add(Calendar.DATE, 1);
			currentD = cal.getTime();
		}
		
		this.setData(data);
		this.generateDataString();
	}

}
