package edu.ncsu.csc.itrust.model.obstetrics.pregnancies;

public class PregnancyInfo 
{
	/** Unique ID corresponding to this record */
	private int id;
	/** Points to which obstetrics initialization visit created this pregnancy record */
	private int obstetricsInitID;
	private long pid;
	private int yearOfConception;
	private int numDaysPregnant;
	private int numHoursInLabor;
	private double weightGain;
	private DeliveryMethod deliveryType;
	private int multiplicity;
	
	/**
	 * Constructor used when just creating a PregnancyInfo object to validate
	 * @param pid
	 * @param yearOfConception
	 * @param numDaysPregnant
	 * @param numHoursInLabor
	 * @param weightGain
	 * @param deliveryType
	 * @param multiplicity
	 */
	public PregnancyInfo(int pid, int yearOfConception, int numDaysPregnant, int numHoursInLabor,
			double weightGain, DeliveryMethod deliveryType, int multiplicity) {
		super();
		this.pid = pid;
		this.yearOfConception = yearOfConception;
		this.numDaysPregnant = numDaysPregnant;
		this.numHoursInLabor = numHoursInLabor;
		this.weightGain = weightGain;
		this.deliveryType = deliveryType;
		this.multiplicity = multiplicity;
	}
	
	/**
	 * Constructor used when adding a record to the database
	 * @param obstetricsInitID
	 * @param pid
	 * @param yearOfConception
	 * @param numDaysPregnant
	 * @param numHoursInLabor
	 * @param weightGain
	 * @param deliveryType
	 * @param multiplicity
	 */
	public PregnancyInfo(int obstetricsInitID, int pid, int yearOfConception, int numDaysPregnant, int numHoursInLabor,
			double weightGain, DeliveryMethod deliveryType, int multiplicity) {
		super();
		this.obstetricsInitID = obstetricsInitID;
		this.pid = pid;
		this.yearOfConception = yearOfConception;
		this.numDaysPregnant = numDaysPregnant;
		this.numHoursInLabor = numHoursInLabor;
		this.weightGain = weightGain;
		this.deliveryType = deliveryType;
		this.multiplicity = multiplicity;
	}
	
	/**
	 * Constructor used when retrieving a record from the database
	 * @param recordID
	 * @param obstetricsInitID
	 * @param pid
	 * @param yearOfConception
	 * @param numDaysPregnant
	 * @param numHoursInLabor
	 * @param weightGain
	 * @param deliveryType
	 * @param multiplicity
	 */
	public PregnancyInfo(int id, int obstetricsInitID, int pid, int yearOfConception, int numDaysPregnant, int numHoursInLabor,
			double weightGain, DeliveryMethod deliveryType, int multiplicity) {
		super();
		this.id = id;;
		this.obstetricsInitID = obstetricsInitID;
		this.pid = pid;
		this.yearOfConception = yearOfConception;
		this.numDaysPregnant = numDaysPregnant;
		this.numHoursInLabor = numHoursInLabor;
		this.weightGain = weightGain;
		this.deliveryType = deliveryType;
		this.multiplicity = multiplicity;
	}
	
	public PregnancyInfo()
	{
		super();
	}

	public int getObstetricsInitID() {
		return obstetricsInitID;
	}
	public void setObstetricsInitID(int obstetricsInitID) {
		this.obstetricsInitID = obstetricsInitID;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public int getYearOfConception() {
		return yearOfConception;
	}
	public void setYearOfConception(int yearOfConception) {
		this.yearOfConception = yearOfConception;
	}
	public int getNumDaysPregnant() {
		return numDaysPregnant;
	}
	public void setNumDaysPregnant(int numDaysPregnant) {
		this.numDaysPregnant = numDaysPregnant;
	}
	public int getNumWeeksPregnant() {
		return numDaysPregnant / 7;
	}
	public int getNumHoursInLabor() {
		return numHoursInLabor;
	}
	public void setNumHoursInLabor(int numHoursInLabor) {
		this.numHoursInLabor = numHoursInLabor;
	}
	public DeliveryMethod getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(DeliveryMethod deliveryType) {
		this.deliveryType = deliveryType;
	}
	public int getMultiplicity() {
		return multiplicity;
	}
	public void setMultiplicity(int multiplicity) {
		this.multiplicity = multiplicity;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setWeightGain(double weightGain) {
		this.weightGain = weightGain;
	}
	
	public double getWeightGain() {
		return weightGain;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deliveryType == null) ? 0 : deliveryType.hashCode());
		result = prime * result + multiplicity;
		result = prime * result + numDaysPregnant;
		result = prime * result + numHoursInLabor;
		result = prime * result + obstetricsInitID;
		result = prime * result + (int) (pid ^ (pid >>> 32));
		long temp;
		temp = Double.doubleToLongBits(weightGain);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + yearOfConception;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PregnancyInfo other = (PregnancyInfo) obj;
		if (deliveryType != other.deliveryType)
			return false;
		if (multiplicity != other.multiplicity)
			return false;
		if (numDaysPregnant != other.numDaysPregnant)
			return false;
		if (numHoursInLabor != other.numHoursInLabor)
			return false;
		if (obstetricsInitID != other.obstetricsInitID)
			return false;
		if (pid != other.pid)
			return false;
		if (Double.doubleToLongBits(weightGain) != Double.doubleToLongBits(other.weightGain))
			return false;
		if (yearOfConception != other.yearOfConception)
			return false;
		return true;
	}
	
}
