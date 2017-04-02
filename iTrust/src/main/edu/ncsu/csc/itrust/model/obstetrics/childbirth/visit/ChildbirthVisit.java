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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deliveryType == null) ? 0 : deliveryType.hashCode());
		result = prime * result + ((epiduralAnaesthesia == null) ? 0 : epiduralAnaesthesia.hashCode());
		result = prime * result + ((magnesiumSulfide == null) ? 0 : magnesiumSulfide.hashCode());
		result = prime * result + ((nitrousOxide == null) ? 0 : nitrousOxide.hashCode());
		result = prime * result + ((officeVisitID == null) ? 0 : officeVisitID.hashCode());
		result = prime * result + ((pethidine == null) ? 0 : pethidine.hashCode());
		result = prime * result + ((pitocin == null) ? 0 : pitocin.hashCode());
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
		ChildbirthVisit other = (ChildbirthVisit) obj;
		if (deliveryType != other.deliveryType)
			return false;
		if (epiduralAnaesthesia == null)
		{
			if (other.epiduralAnaesthesia != null)
				return false;
		}
		else if (!epiduralAnaesthesia.equals(other.epiduralAnaesthesia))
			return false;
		if (magnesiumSulfide == null)
		{
			if (other.magnesiumSulfide != null)
				return false;
		}
		else if (!magnesiumSulfide.equals(other.magnesiumSulfide))
			return false;
		if (nitrousOxide == null)
		{
			if (other.nitrousOxide != null)
				return false;
		}
		else if (!nitrousOxide.equals(other.nitrousOxide))
			return false;
		if (officeVisitID == null)
		{
			if (other.officeVisitID != null)
				return false;
		}
		else if (!officeVisitID.equals(other.officeVisitID))
			return false;
		if (pethidine == null)
		{
			if (other.pethidine != null)
				return false;
		}
		else if (!pethidine.equals(other.pethidine))
			return false;
		if (pitocin == null)
		{
			if (other.pitocin != null)
				return false;
		}
		else if (!pitocin.equals(other.pitocin))
			return false;
		return true;
	}
	
	
}