package bankproject.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Account extends AbstractEntity {
	private CountryEnum country;
	private Customer customer;
	private Map<Integer, Operation> listOperations = new HashMap<Integer, Operation>();
	private Double balance = (double) 0;
	private static Set<Integer> listAccountNumbers = new HashSet<Integer>();
	private Integer accountNumber;

	/**
	 * @return the accountNumber
	 */
	public Integer getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
		listAccountNumbers.add(accountNumber);
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	/**
	 * @return the country
	 */
	public CountryEnum getCountry() {
		return country;
	}
	
	/**
	 * @param country the country to set
	 */
	public void setCountry(CountryEnum country) {
		this.country = country;
	}
	
	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}
	
	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	/**
	 * @return the listOperations
	 */
	public Collection<Operation> getListOperations() {
		return listOperations.values();
	}
	
	/**
	 * @param listOperations the listOperations to set
	 */
	public void setListOperations(Collection<Operation> _listOperations) {
		for (Operation op : _listOperations) {
			this.listOperations.put(op.getId(), op);
		}
	}
	
	public void addOperation(Operation op) {
		this.listOperations.put(op.getId(), op);
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getBalance() {
		return balance;
	}
	
	/**
	 * 
	 * @param balance
	 */
	public void addAmount(Double balance) {
		this.balance += balance;
	}
	
	/**
	 * 
	 */
	public void generateAccountNumber () {
		int low = 100000;
		int high = 999999;
		do {
			Random r = new Random();
			accountNumber = r.nextInt(high - low) + low;
		} while (listAccountNumbers.contains(accountNumber));
		
		listAccountNumbers.add(accountNumber);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFullAccountNumber () {
		return country.getShortCode() + String.valueOf(accountNumber);
	}
	
	/**
	 * 
	 */
	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append(customer.toString())
			.append("\t")
			.append(getFullAccountNumber())
			.append("\t")
			.append(balance)
			.append("\n");
		
		return sb.toString();
	}
}
