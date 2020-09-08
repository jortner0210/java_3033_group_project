package DB;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;

public class DBManager {
	
	// Creates a new database
	public void create_db( String db_name ) {
		try {
        	String db_loc = "jdbc:sqlite:" + db_name + ".db";
    		Connection conn = DriverManager.getConnection( db_loc );
            if ( conn != null ) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println( "The driver name is " + meta.getDriverName() );
                System.out.println( "A new database has been created." );
            }
		}
		catch ( Exception e ) {
			System.out.println( e );
		}
	}
	
	// Provides a connection to a given database
	public static Connection connect(String database) {
		Connection conn = null;
		try {
			String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/" + database + ".db";
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 
		
		return conn;
	}
	
	
	/*
	 * This stuff might be used but I'm not sure yet
	 * 
	public void connectTable( String database, String table) {
		String sql = "SELECT * FROM " + table;
		
		try (Connection conn = this.connect(database);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public boolean recordExists(String database, 
			String table, 
			String column,
			String value) {
		String sql = "SELECT * FROM " + table + " WHERE " + column + " = " + value;
		System.out.println(sql);
		boolean recordFound = false;
		try (Connection conn = this.connect(database);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
				if(rs != null) {
					recordFound = true;
				}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return recordFound;
	}*/

}
