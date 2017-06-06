package bankproject.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Customer extends AbstractEntity {
	private String firstname;
	private String lastname;
	private Set<Account> listAccounts = new HashSet<Account>();
	
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		firstname = firstname.toLowerCase();
		char[] array = firstname.toCharArray();
		array[0] = Character.toUpperCase(array[0]);
		this.firstname = String.copyValueOf(array);
	}
	
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname.toUpperCase();
	}
	
	public String getFullName() {
		return firstname + " " + lastname;
	}

	public Collection<Account> getListAccounts() {
		return listAccounts;
	}

	public void setListAccounts(Collection<Account> listAccounts) {
		this.listAccounts.addAll(listAccounts);
	}
	
	public void addAccount(Account account) {
		this.listAccounts.add(account);
	}
}
