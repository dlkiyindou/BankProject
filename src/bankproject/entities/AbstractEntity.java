package bankproject.entities;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class AbstractEntity {
	private Integer id;
	private Date dateCreation;
	
	/**
	 * 
	 * @return
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getDateCreation() {
		return dateCreation;
	}
	
	/**
	 * 
	 * @param dateCreation
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**
	 * 
	 * @return
	 */
	public String getDateCreationToString() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRENCH);
		return df.format(this.dateCreation);
	}
}
