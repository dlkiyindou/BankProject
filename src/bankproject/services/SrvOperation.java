package bankproject.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import bankproject.entities.AbstractEntity;
import bankproject.entities.Account;
import bankproject.entities.Operation;
import bankproject.entities.TypeOperationEnum;
import bankproject.exceptions.SrvException;
import bankproject.services.db.DatabaseManager;

public class SrvOperation extends AbstractService {
	SrvAccount srvAccount;
	private static SrvOperation INSTANCE = new SrvOperation();
	
	/**
	 * 
	 */
	private SrvOperation () {
		setEntitySqlTable("operation");
	}
	
	/**
	 * 
	 * @return
	 */
	public static SrvOperation getInstance (DatabaseManager dbm) {
		if (INSTANCE.getDbManager() == null) {
			INSTANCE.setDbManager(dbm);
			INSTANCE.srvAccount =  SrvAccount.getInstance(dbm);
		}
		
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param entity
	 * @throws SQLException 
	 */
	private void create(Operation entity) throws SQLException {
		String sql = "INSERT INTO " + getEntitySqlTable() + 
				" (account_id, type_operation, amount, date_creation) VALUES (?, ?, ?, dateTime('NOW'))";
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = getDbManager().getConnection();
			ps = connection.prepareStatement(sql, new String[]{"id"});
			ps.setInt(1, entity.getAccount().getId());
			ps.setString(2, entity.getTypeOperation().name());
			ps.setDouble(2, entity.getAmount());
	
			ps.executeUpdate();		
			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {
			    entity.setId(rs.getInt(1));
			    entity.setDateCreation(new Date());
			}
		}  catch (SQLException e) {
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
	
	/**
	 * 
	 * @param entity
	 * @throws SQLException 
	 */
	private void update(Operation entity) throws SQLException {
		String sql = "UPDATE " + getEntitySqlTable() 
		+ "SET account_id = ?, type_operation = ?, amount = ? WHERE id = ?";
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = getDbManager().getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, entity.getAccount().getId());
			ps.setString(2, entity.getTypeOperation().name());
			ps.setDouble(3, entity.getAmount());
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
	protected Operation populateEntity(ResultSet rs) throws Exception {
		Operation operation = new Operation();
		operation.setId(rs.getInt("id"));
		operation.setTypeOperation(TypeOperationEnum.valueOf(rs.getString("type_operation")));
		operation.setAmount(rs.getDouble("amount"));
		operation.setAccount((Account)srvAccount.get(rs.getInt("account_id"), operation));
		operation.setDateCreation(rs.getDate("date_creation"));
		
		return operation;
	}

	@Override
	public void save(AbstractEntity entity) throws SrvException, SQLException {
		if (entity instanceof Operation) {
			Operation operation = (Operation)entity;
			if (operation.getId() == null) {
				create(operation);
			} else {
				update(operation);
			}
		} else {
			throw new SrvException("Utilisation du mauvais service");
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public Collection<Operation> getByAccountId(Integer id) throws Exception {
		Connection connexion = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		Collection<Operation> results = new HashSet<Operation>();
		String sql = "SELECT * FROM " + getEntitySqlTable() + " WHERE accound_id = ?";
		
		try {
			connexion = getDbManager().getConnection();
			st = connexion.prepareStatement(sql);
			rs = st.executeQuery(sql);
			
			while (rs.next()) {
				results.add(populateEntity(rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connexion != null) {
				connexion.close();
			}
			
			if (st != null) {
				st.close();
			}
		}
		
		return results;
	}
}
