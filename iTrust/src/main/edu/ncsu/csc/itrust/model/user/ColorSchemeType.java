package edu.ncsu.csc.itrust.model.user;

public enum ColorSchemeType
{
	DEFAULT("Default"),
	DARK("Dark"),
	MUTED_MIDTONES("Muted Midtones"),
	RAINBOW("Rainbow"),
	I_DARE_YOU("I Dare You");
	
	private final String text;
	
	private ColorSchemeType(final String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}

	/**
	 * Converts a string to the Enum type it matches
	 * 
	 * @param text the string to convert
	 * @return a ColorSchemeType enum value representing the string
	 */
	public static ColorSchemeType matchString(String text) {
		for (ColorSchemeType i : ColorSchemeType.values()) {
			if (i.text.equalsIgnoreCase(text)) {
				return i;
			}
		}
		throw new IllegalArgumentException("No such enum exists");
	}
}
