package bankproject.entities;

public enum CountryEnum {
	FRANCE ("FR", "France"), GRANDEBRETAGNE ("GB", "Grande-Bretagne"), ALLEMAGNE("DE", "Allemagne"),
	BELGIQUE ("BE", "Belgique"), PAYSBAS("NL", "Pays-Bas"), ESPAGNE("ES", "Espagne"), ITALIE("IT", "Italie");
	
	private String shortCode;
	private String longName;
	private CountryEnum (String _shortCode, String _name) {
		shortCode = _shortCode;
		longName = _name;
	}
	
	/**
	 * Getter
	 * 
	 * @return
	 */
	public String getShortCode () {
		return shortCode;
	}

	public String getLongName() {
		return longName;
	}

	public static CountryEnum getByShortCode (String s) {
		for (CountryEnum ce : CountryEnum.values()) {
			if (ce.getShortCode().toLowerCase().equals(s.toLowerCase())) {
				return ce;
			}
		}
		
		return null;
	}

	public static CountryEnum getByLongName(String s) {
		for (CountryEnum ce : CountryEnum.values()) {
			if (ce.getLongName().toLowerCase().equals(s.toLowerCase())) {
				return ce;
			}
		}
		
		return null;
	}

}
