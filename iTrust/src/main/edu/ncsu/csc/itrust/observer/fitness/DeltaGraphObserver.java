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
 * Graph observer implementation for the Delta Graph
 * @author jcgonzal
 *
 */
public class DeltaGraphObserver extends GraphObserver {

	/**
	 * Constructor with data source
	 */
	public DeltaGraphObserver(DataSource ds) {
		super(ds);
		this.setFitnessController(new FitnessInfoController(ds));
	}
	
	/**
	 * Constructor
	 */
	public DeltaGraphObserver() {
		super();
		this.setFitnessController(new FitnessInfoController());
	}
	
	/**
	 * Implementation of the update data function to update data based on start and end dates.
	 */
	public void updateData(Long pid, String startDate, String endDate) {
		ArrayList<GraphKeyValue> data = new ArrayList<GraphKeyValue>();
		
		FitnessInfo[] fitnessInfos;
		if (pid == null) fitnessInfos = this.getFitnessController().getFitnessInfo(startDate, endDate);
		else fitnessInfos = this.getFitnessController().getFitnessInfo(pid, startDate, endDate);
		
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
		
		Date currentD = FitnessInfo.stringToJavaDate(startDate);
		Date endD = FitnessInfo.stringToJavaDate(endDate);
		
		FitnessInfo prev = fitnessInfos[0];
		for (int i = 1; i < fitnessInfos.length; i++) {
			while (currentD.before(fitnessInfos[i].getJavaDate())) {
				data.add(new GraphKeyValue(FitnessInfo.dateToString(currentD), null));
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentD);
				cal.add(Calendar.DATE, 1);
				currentD = cal.getTime();
			}
			data.add(new GraphKeyValue(fitnessInfos[i].getDate(), fitnessInfos[i].getSteps() - prev.getSteps()));
			prev = fitnessInfos[i];
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentD);
			cal.add(Calendar.DATE, 1);
			currentD = cal.getTime();
		}
		
		while (currentD.before(endD) || currentD.equals(endD)) {
			data.add(new GraphKeyValue(FitnessInfo.dateToString(currentD), null));
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentD);
			cal.add(Calendar.DATE, 1);
			currentD = cal.getTime();
		}
		
		this.setData(data);
		this.generateDataString();
	}

}
