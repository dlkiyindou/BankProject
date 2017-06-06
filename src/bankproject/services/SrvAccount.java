package bankproject.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import bankproject.entities.AbstractEntity;
import bankproject.entities.Account;
import bankproject.entities.CountryEnum;
import bankproject.entities.Customer;
import bankproject.entities.Operation;
import bankproject.exceptions.SrvException;
import bankproject.services.db.DatabaseManager;

public class SrvAccount extends AbstractService {
	SrvCustomer srvCustomer;
	SrvOperation srvOperation;
	private static SrvAccount INSTANCE = new SrvAccount();
	private static Map<Integer, Account> listAccountsLoaded = new HashMap<Integer, Account>();
	
	/**
	 * 
	 */
	private SrvAccount () {
		setEntitySqlTable("account");
	}
	
	/**
	 * 
	 * @return
	 */
	public static SrvAccount getInstance (DatabaseManager dbm) {
		if (INSTANCE.getDbManager() == null) {
			INSTANCE.setDbManager(dbm);
			INSTANCE.srvCustomer = SrvCustomer.getInstance(dbm);
			INSTANCE.srvOperation = SrvOperation.getInstance(dbm);
		}
		return INSTANCE;
	}
	
	
	public Account get(Integer id) {
		Account ac = null;
		
		if (listAccountsLoaded.containsKey(id)) {
			ac = listAccountsLoaded.get(id);
		} else {
		
			try {
				ac = (Account)super.get(id);
				listAccountsLoaded.put(ac.getId(), ac);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		return ac;
		
	}
	
	
	public Account get(Integer id, Operation op) {
		Account ac = get(id);
		ac.addOperation(op);
		
		return ac;
	}
	
	/**
	 * 
	 * @param entity
	 * @throws SQLException
	 */
	private void create(Account entity) throws SQLException {
		String sql = "INSERT INTO " + getEntitySqlTable() 
			+ " (account_number, country, balance, customer_id, date_creation) VALUES (?, ?, ?, ?, DateTime('NOW'))";
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = getDbManager().getConnection();
			ps = connection.prepareStatement(sql, new String[]{"id"});
			ps.setInt(1, entity.getAccountNumber());
			ps.setString(2, entity.getCountry().name());
			ps.setDouble(3,  entity.getBalance());
			ps.setInt(4, entity.getCustomer().getId());
	
			ps.executeUpdate();		
			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {
			    entity.setId(rs.getInt(1));
			    entity.setDateCreation(new Date());
			    
			    listAccountsLoaded.put(entity.getId(), entity);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (ps != null) {
				ps.close();
			}
			
			if (connection != null) {
				connection.close();
			}
		}
	}

	/**
	 * 
	 * @param entity
	 * @throws SQLException
	 */
	private void update(Account entity) throws SQLException {
		String sql = "UPDATE " + getEntitySqlTable() 
		+ "SET account_number = ?, country = ?, balance = ? WHERE id = ?";
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = getDbManager().getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, entity.getAccountNumber());
			ps.setString(2, entity.getCountry().name());
			ps.setDouble(3, entity.getBalance());
			ps.setInt(4, entity.getId());
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
			
			if (ps != null) {
				ps.close();
			}
		}
	}

	@Override
	protected Account populateEntity(ResultSet rs) throws Exception {
		Account account = new Account ();
		account.setId(rs.getInt("a.id"));
		account.setAccountNumber(rs.getInt("a.account_number"));
		account.setCountry(CountryEnum.valueOf(rs.getString("a.country")));
		account.setBalance(rs.getDouble("a.balance"));
		
		Customer customer = (Customer)srvCustomer.get(rs.getInt("c.id"));
		account.setCustomer(customer);
		account.setListOperations(srvOperation.getByAccountId(account.getId()));
		
		return null;
	}


	@Override
	public void save(AbstractEntity entity) throws SrvException, SQLException {
		if (entity instanceof Account) {
			Account account = (Account)entity;
			if (account.getId() == null) {
				create(account);
			} else {
				update(account);
			}
		} else {
			throw new SrvException("Utilisation du mauvais service");
		}
	}

	public Account getByAccountNumberAndCountry(Integer accountNumber, CountryEnum country) throws Exception {
		Connection connection = null;
		PreparedStatement pst =  null;
		Account account = null;
		String sql = "SELECT * FROM " + getEntitySqlTable() + " WHERE country = ? AND account_number = ? ";
		
		try {
			connection = getDbManager().getConnection();
			pst = connection.prepareStatement(sql);
			pst.setString(1, country.name());
			pst.setInt(2, accountNumber);
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				account = populateEntity(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {		
			if (pst != null) {
				pst.close();
			}
			
			if (connection != null) {
				connection.close();
			}
		}
		
		return account;
	}
}
