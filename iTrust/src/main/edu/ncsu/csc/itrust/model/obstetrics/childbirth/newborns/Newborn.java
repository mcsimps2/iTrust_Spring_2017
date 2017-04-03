package edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns;

public class Newborn
{
	private Long id;
	private Long officeVisitID;
	/** Date in format yyyy-M-d (otherwise, will fail validation)*/
	private String dateOfBirth;
	/** Time in format h:m:s (otherwise, will fail validation) */
	private String timeOfBirth;
	private SexType sex;
	private Boolean timeEstimated;
	private Long pid;
	
	public Newborn()
	{
		super();
	}
	
	public Newborn(Long pid, Long officeVisitID, String dateOfBirth, String timeOfBirth, SexType sex, Boolean timeEstimated)
	{
		super();
		this.pid = pid;
		this.officeVisitID = officeVisitID;
		this.dateOfBirth = dateOfBirth;
		this.timeOfBirth = timeOfBirth;
		this.sex = sex;
		this.timeEstimated = timeEstimated;
	}
	
	public Newborn(Long officeVisitID)
	{
		super();
		this.officeVisitID = officeVisitID;
	}
	
	public Long getPID()
	{
		return pid;
	}
	public void setPID(Long pid)
	{
		this.pid = pid;
	}
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Long getOfficeVisitID()
	{
		return officeVisitID;
	}
	public void setOfficeVisitID(Long officeVisitID)
	{
		this.officeVisitID = officeVisitID;
	}
	
	/**
	 * Gets the date of birth
	 * @return the date of birth
	 */
	public String getDateOfBirth()
	{
		return dateOfBirth;
	}
	/**
	 * Sets the date of birth
	 * To pass validation from the validator, it is advisable the date be in the format
	 * yyyy-M-d
	 * @param dateOfBirth
	 */
	public void setDateOfBirth(String dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}
	/**
	 * Gets the time of birth
	 * @return the time of birth
	 */
	public String getTimeOfBirth()
	{
		return timeOfBirth;
	}
	/**
	 * Sets the time of birth
	 * To pass validation from the validator, it is advisable the date bei in the format
	 * H:m:s (H is anywhere from 0-23, m anywhere from 0 to 59, s from 0 to 59)
	 * @param timeOfBirth
	 */
	public void setTimeOfBirth(String timeOfBirth)
	{
		this.timeOfBirth = timeOfBirth;
	}
	public SexType getSex()
	{
		return sex;
	}
	public void setSex(SexType sex)
	{
		this.sex = sex;
	}
	public Boolean getTimeEstimated()
	{
		return timeEstimated;
	}
	public void setTimeEstimated(Boolean timeEstimated)
	{
		this.timeEstimated = timeEstimated;
	}

	
	public int hashCode1()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result + ((officeVisitID == null) ? 0 : officeVisitID.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((timeEstimated == null) ? 0 : timeEstimated.hashCode());
		result = prime * result + ((timeOfBirth == null) ? 0 : timeOfBirth.hashCode());
		return result;
	}

	public boolean equals1(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Newborn other = (Newborn) obj;
		if (dateOfBirth == null)
		{
			if (other.dateOfBirth != null)
				return false;
		}
		else if (!dateOfBirth.contains(other.dateOfBirth) && !other.dateOfBirth.contains(dateOfBirth))
			return false;
		if (officeVisitID == null)
		{
			if (other.officeVisitID != null)
				return false;
		}
		else if (!officeVisitID.equals(other.officeVisitID))
			return false;
		if (sex != other.sex)
			return false;
		if (timeEstimated == null)
		{
			if (other.timeEstimated != null)
				return false;
		}
		else if (!timeEstimated.equals(other.timeEstimated))
			return false;
		if (timeOfBirth == null)
		{
			if (other.timeOfBirth != null)
				return false;
		}
		else if (!timeOfBirth.contains(other.timeOfBirth) && !other.timeOfBirth.contains(timeOfBirth))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result + ((officeVisitID == null) ? 0 : officeVisitID.hashCode());
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((timeEstimated == null) ? 0 : timeEstimated.hashCode());
		result = prime * result + ((timeOfBirth == null) ? 0 : timeOfBirth.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Newborn other = (Newborn) obj;
		if (dateOfBirth == null)
		{
			if (other.dateOfBirth != null)
				return false;
		}
		else if (!dateOfBirth.contains(other.dateOfBirth) && !other.dateOfBirth.contains(dateOfBirth))
			return false;
		if (officeVisitID == null)
		{
			if (other.officeVisitID != null)
				return false;
		}
		else if (!officeVisitID.equals(other.officeVisitID))
			return false;
		if (pid == null)
		{
			if (other.pid != null)
				return false;
		}
		else if (!pid.equals(other.pid))
			return false;
		if (sex != other.sex)
			return false;
		if (timeEstimated == null)
		{
			if (other.timeEstimated != null)
				return false;
		}
		else if (!timeEstimated.equals(other.timeEstimated))
			return false;
		if (timeOfBirth == null)
		{
			if (other.timeOfBirth != null)
				return false;
		}
		else if (!timeOfBirth.contains(other.timeOfBirth) && !other.timeOfBirth.contains(timeOfBirth))
			return false;
		return true;
	}
	
	
}
