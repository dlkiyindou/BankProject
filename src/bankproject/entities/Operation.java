package bankproject.entities;

public class Operation extends AbstractEntity {
	private Double amount;
	private TypeOperationEnum typeOperation;
	private Account account;
	
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * @return the type
	 */
	public TypeOperationEnum getTypeOperation() {
		return typeOperation;
	}
	/**
	 * @param type the type to set
	 */
	public void setTypeOperation(TypeOperationEnum type) {
		this.typeOperation = type;
	}
	
	/**
	 * 
	 * @param account
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * 
	 * @return
	 */
	public Account getAccount() {
		return this.account;
	}

}
