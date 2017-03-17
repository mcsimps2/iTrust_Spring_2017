package edu.ncsu.csc.itrust.model.obstetrics.pregnancies;

public enum DeliveryMethod {
	VAGINAL_DELIVERY("Vaginal Delivery"),
	VAGINAL_DELIVERY_VACUUM("Vaginal Delivery Vacuum Assist"),
	VAGINAL_DELIVERY_FORCEPS("Vaginal Delivery Forceps Assist"),
	CAESAREAN_SECTION("Caesarean section"),
	MISCARRIAGE("Miscarriage");
	
	private final String text;
	
	private DeliveryMethod(final String text) {
		this.text = text;
	}
	
    @Override
    public String toString() {
        return text;
    }
    
    public static DeliveryMethod matchString(String text) {
        for (DeliveryMethod i : DeliveryMethod.values()) {
          if (i.text.equalsIgnoreCase(text)) {
            return i;
          }
        }
        throw new IllegalArgumentException("No such enum exists");
      }
}
