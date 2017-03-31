package edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns;

public class Newborn
{
	private Long id;
	private Long officeVisitID;
	private String dateOfBirth;
	private String timeOfBirth;
	private SexType sex;
	private Boolean timeEstimated;
	
	public Newborn()
	{
		super();
	}
	
	public Newborn(Long officeVisitID, String dateOfBirth, String timeOfBirth, SexType sex, Boolean timeEstimated)
	{
		super();
		this.officeVisitID = officeVisitID;
		this.dateOfBirth = dateOfBirth;
		this.timeOfBirth = timeOfBirth;
		this.sex = sex;
		this.timeEstimated = timeEstimated;
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
	public String getDateOfBirth()
	{
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}
	public String getTimeOfBirth()
	{
		return timeOfBirth;
	}
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
}
