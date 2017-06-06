package bankproject.services.db;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseManager {
	public abstract Connection getConnection () throws SQLException;
}
