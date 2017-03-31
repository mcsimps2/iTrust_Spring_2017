package edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit;

import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;

public class ChildbirthVisit
{
	private Long id;
	private Long officeVisitID;
	private DeliveryMethod deliveryType;
	private Integer pitocin;
	private Integer nitrousOxide;
	private Integer pethidine;
	private Integer epiduralAnaesthesia;
	private Integer magnesiumSulfide;
	
	public ChildbirthVisit()
	{
		super();
	}
	
	public ChildbirthVisit(Long officeVisitID, DeliveryMethod deliveryType, Integer pitocin, Integer nitrousOxide,
			Integer pethidine, Integer epiduralAnaesthesia, Integer magnesiumSulfide)
	{
		super();
		this.officeVisitID = officeVisitID;
		this.deliveryType = deliveryType;
		this.pitocin = pitocin;
		this.nitrousOxide = nitrousOxide;
		this.pethidine = pethidine;
		this.epiduralAnaesthesia = epiduralAnaesthesia;
		this.magnesiumSulfide = magnesiumSulfide;
	}
	
	
	public DeliveryMethod getDeliveryType()
	{
		return deliveryType;
	}

	public void setDeliveryType(DeliveryMethod deliveryType)
	{
		this.deliveryType = deliveryType;
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
	public Integer getPitocin()
	{
		return pitocin;
	}
	public void setPitocin(Integer pitocin)
	{
		this.pitocin = pitocin;
	}
	public Integer getNitrousOxide()
	{
		return nitrousOxide;
	}
	public void setNitrousOxide(Integer nitrousOxide)
	{
		this.nitrousOxide = nitrousOxide;
	}
	public Integer getPethidine()
	{
		return pethidine;
	}
	public void setPethidine(Integer pethidine)
	{
		this.pethidine = pethidine;
	}
	public Integer getEpiduralAnaesthesia()
	{
		return epiduralAnaesthesia;
	}
	public void setEpiduralAnaesthesia(Integer epiduralAnaesthesia)
	{
		this.epiduralAnaesthesia = epiduralAnaesthesia;
	}
	public Integer getMagnesiumSulfide()
	{
		return magnesiumSulfide;
	}
	public void setMagnesiumSulfide(Integer magnesiumSulfide)
	{
		this.magnesiumSulfide = magnesiumSulfide;
	}
}
