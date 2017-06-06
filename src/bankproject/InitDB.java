package bankproject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import bankproject.services.db.SQLiteManager;

public class InitDB {
	

	public static void main(String[] args) {
		StringBuilder sql = new StringBuilder();
				
		Connection connection = null;
		Statement st = null;
		try {
			connection = SQLiteManager.getInstance().getConnection();
			st = connection.createStatement();
			
			st.addBatch("DROP TABLE IF EXISTS customer");
			st.addBatch("DROP TABLE IF EXISTS account");
			st.addBatch("DROP TABLE IF EXISTS operation");
			
			sql = new StringBuilder("CREATE TABLE IF NOT EXISTS customer ( ")
				.append("id INTEGER PRIMARY KEY AUTOINCREMENT, ")
				.append("firstname TEXT, ")
				.append("lastname TEXT, ")
				.append("date_creation DATETIME ")
				.append(");");
			st.addBatch(sql.toString());
			
			sql = new StringBuilder("CREATE TABLE IF NOT EXISTS account ( ")
				.append("id INTEGER PRIMARY KEY AUTOINCREMENT, ")
				.append("customer_id INTEGER, ")
				.append("account_number TEXT, ")
				.append("balance INTEGER, ")
				.append("country TEXT, ")
				.append("date_creation DATETIME ")
				.append(");");
			st.addBatch(sql.toString());
			
			sql = new StringBuilder("CREATE TABLE IF NOT EXISTS operation ( ")
				.append("id INTEGER PRIMARY KEY AUTOINCREMENT, ")
				.append("account_id INTEGER, ")
				.append("type_operation TEXT, ")
				.append("amount TEXT, ")
				.append("date_creation DATETIME ")
				.append(");");
			st.addBatch(sql.toString());
		
			
			st.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
				
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
	}

}
