package bankproject;

import java.sql.SQLException;

import bankproject.readers.AccountCustomerThread;
import bankproject.readers.OperationThread;

public class Main {

	public static void main(String[] args) throws SQLException {
		AccountCustomerThread act = new AccountCustomerThread();
		OperationThread ot = new OperationThread();
		
		act.start();
		ot.start();
	}

}
