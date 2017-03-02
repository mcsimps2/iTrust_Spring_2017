package edu.ncsu.csc.itrust.observer.fitness;

import java.util.List;

import edu.ncsu.csc.itrust.controller.fitness.FitnessInfoController;
import edu.ncsu.csc.itrust.controller.fitness.FitnessInfoController.GraphKeyValue;

/**
 * Abstract class for the observer pattern. Will have one observer for each type of graph
 * @author jcgonzal
 *
 */
public abstract class GraphObserver 
{
	/**
	 * The data to be put in the graph
	 */
	private List<GraphKeyValue> data;
	
	/**
	 * The string representation of the data to be put in the graph
	 */
	private String dataString;
	
	/**
	 * A fitness controller for access to the database
	 */
	private FitnessInfoController fitnessController;
	
	/**
	 * Error message for when no data is found.
	 */
	protected static String NO_DATA_FOUND_IN_DATES = "No data found in date ranges.";
	
	/**
	 * Constructor. Also instatiates a new fitness controller.
	 */
	public GraphObserver() {
		setFitnessController(new FitnessInfoController());
	}
	
	/**
	 * Method for updating the data and dataString objects based on the input dates
	 * @param startDate input start date
	 * @param endDate input end date
	 */
	public abstract void updateData(Long pid, String startDate, String endDate);
	
	/**
	 * Helper method used to generate the string version of the graph data.
	 */
	public void generateDataString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (GraphKeyValue d : data) {
			stringBuilder.append("['");
			stringBuilder.append(d.getKey());
			stringBuilder.append("',");
			stringBuilder.append(d.getValue());
			stringBuilder.append("],");
		}
		dataString = stringBuilder.toString().substring(0,
		stringBuilder.toString().length() - 1);
	}
	
	/**
	 * Get the graph data
	 * @return the graph data
	 */
	public List<GraphKeyValue> getData() {
		return data;
	}
	
	/**
	 * Set the graph data
	 * @param data the graph data
	 */
	public void setData(List<GraphKeyValue> data) {
		this.data = data;
	}

	/**
	 * Get the string representation of the graph data
	 * @return the string representation of the graph data
	 */
	public String getDataString() {
		return dataString;
	}

	/**
	 * Set the string representation of the graph data
	 * @param dataString the string representation of the graph data
	 */
	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

	/**
	 * Get the fitness controller for access to database
	 * @return the fitness controller
	 */
	public FitnessInfoController getFitnessController() {
		return fitnessController;
	}

	/**
	 * Set the fitness controller
	 * @param fitnessController the fitness controller
	 */
	public void setFitnessController(FitnessInfoController fitnessController) {
		this.fitnessController = fitnessController;
	}
	
}
