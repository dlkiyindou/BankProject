package bankproject.services.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteManager extends DatabaseManager {
	private static SQLiteManager INSTANCE = new SQLiteManager(); 
	private String url;
	
	/**
	 * Private constructor
	 */
	private SQLiteManager () {
		url = "jdbc:sqlite:" + getSQLiteDBPath();
	}
	
	/**
	 * This class is a singleton. Return the unique instance
	 * 
	 * @return
	 */
	public static SQLiteManager getInstance () {
		return INSTANCE;
	}
   
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
    public Connection getConnection () throws SQLException {    	
        return DriverManager.getConnection(url);
    }

    /**
     * 
     * @return
     */
	public static String getSQLiteDBPath () {
		String fs = System.getProperty("file.separator");
		String dirPath = System.getProperty("user.dir") + fs + "db"+ fs + "bank";
		File dir = new File(dirPath);
		if (!(dir.exists() && dir.isDirectory())) { // NOT (A && B) = NOT A || NOT B
			dir.mkdirs();
		}
		
    	return dirPath + fs + "data.db";
    }
	
	
	public static void main(String[] args) {
		System.out.println(getSQLiteDBPath());
	}
}
