package bankproject.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import bankproject.entities.AbstractEntity;
import bankproject.entities.Customer;
import bankproject.exceptions.SrvException;
import bankproject.services.db.DatabaseManager;

public class SrvCustomer extends AbstractService {
	/**
	 * 
	 */
	private static SrvCustomer INSTANCE = new SrvCustomer();
	private static Map<Integer, Customer> listCustomersLoaded = new HashMap<Integer, Customer>();
	
	/**
	 * 
	 */
	private SrvCustomer () {
		setEntitySqlTable("customer");
	}
	
	/**
	 * 
	 * @return
	 */
	public static SrvCustomer getInstance (DatabaseManager dbm) {
		if (INSTANCE.getDbManager() == null) {
			INSTANCE.setDbManager(dbm);
		}
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param entity
	 * @throws SQLException
	 */
	private void create(Customer entity) throws SQLException {
		String sql = "INSERT INTO " + getEntitySqlTable() + 
				" (firstname, lastname, date_creation) VALUES (?, ?, dateTime('NOW'))";
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = getDbManager().getConnection();
			ps = connection.prepareStatement(sql, new String[]{"id"});
			ps.setString(1, entity.getFirstname());
			ps.setString(2, entity.getLastname());
	
			ps.executeUpdate();		
			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {
			    entity.setId(rs.getInt(1));
			    entity.setDateCreation(new Date());
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
	
	public Customer get(Integer id) {
		Customer cu = null;
		
		if (listCustomersLoaded .containsKey(id)) {
			cu = listCustomersLoaded.get(id);
		} else {
		
			try {
				cu = (Customer)super.get(id);
				listCustomersLoaded.put(cu.getId(), cu);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		return cu;
		
	}

	/**
	 * 
	 * @param entity
	 * @throws SQLException
	 */
	private void update(Customer entity) throws SQLException {
		String sql = "UPDATE " + getEntitySqlTable() + " SET firstname = ?, lastname = ? WHERE id = ?";
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = getDbManager().getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, entity.getFirstname());
			ps.setString(2, entity.getLastname());
			ps.setInt(3, entity.getId());
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
	protected Customer populateEntity(ResultSet rs) throws SQLException {
		Customer customer = new Customer();
		customer.setId(rs.getInt("id"));
		customer.setFirstname(rs.getString("firstname"));
		customer.setLastname(rs.getString("lastname"));
		//customer.setDateCreation(rs.getDate("date_creation"));
		
		return customer;
	}

	@Override
	public void save(AbstractEntity entity) throws SrvException, SQLException {
		if (entity instanceof Customer) {
			Customer customer = (Customer)entity;
			if (customer.getId() == null) {
				create(customer);
			} else {
				update(customer);
			}
		} else {
			throw new SrvException("Utilisation du mauvais service");
		}
	}
	
	public Customer getOrCreateByName(String firstname, String lastname) throws SQLException {
		Customer customer = getByName(firstname, lastname);
		if (customer == null) {
			customer = new Customer();
			customer.setLastname(lastname);
			customer.setFirstname(firstname);
			this.create(customer);
		}
		
		return customer;
	}
	
	
	public Customer getByName(String firstname, String lastname) throws SQLException {
		Connection connection = null;
		PreparedStatement pst =  null;
		Customer customer = null;
		String sql = "SELECT * FROM " + getEntitySqlTable() + " WHERE UPPER(firstname) = ? AND UPPER(lastname) = ? ";
		
		try {
			connection = getDbManager().getConnection();
			pst = connection.prepareStatement(sql);
			pst.setString(1, firstname.trim().toUpperCase());
			pst.setString(2, lastname.trim().toUpperCase());
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				customer = populateEntity(rs);
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
		
		return customer;
	}
}
