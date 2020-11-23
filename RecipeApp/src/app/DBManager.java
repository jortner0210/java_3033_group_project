package app;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class manages the database and serves as a parent to RecipeDBManager.
 * If a user or any other type of database were to be implemented they would 
 * use this class
 */
public class DBManager {
	
	// instance variables
	Connection conn = null;
	String name = null;
	
	final String BASE_LOC = "jdbc:sqlite:";	
	final int FAILED_EXECUTE = -1;
	
	// used to access db 
	public Connection get_connection() {
		return conn;
	}
	
	/*
	 * Initialize database connection
	 */
	public boolean connect_db( String db_name ) {
		try {
        	String db_loc = "jdbc:sqlite:" + db_name + ".db";
    		conn = DriverManager.getConnection( db_loc );
            if ( conn != null ) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println( "The driver name is " + meta.getDriverName() );
                System.out.println( "A new database has been created." );
                name = db_name;
                return true;
            }
            return false;
		}
		catch ( Exception e ) {
			System.out.println( e );
			return false;
		}
	}
	
	/*
	 * Execute query
	 */
	public boolean execute( String query ) {
		if ( conn == null )
			return false;
		try {
			Statement stmt = conn.createStatement();
			return stmt.execute( query );
		}
		catch ( Exception e ) {
			System.out.println( e );
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 * Execute query with update
	 */
	public int execute_update( String query ) {
		if ( conn == null )
			return FAILED_EXECUTE;
		try {
			Statement stmt = conn.createStatement();
			return stmt.executeUpdate( query );
		}
		catch ( Exception e ) {
			System.out.println( e );
			e.printStackTrace();
			return FAILED_EXECUTE;
		}
	}
	
	/*
	 * Execute query with select
	 * Returns ResultSet
	 */
	public ResultSet execute_query( String query ) {
		Statement stmt;
		if ( conn == null )
			return null;
		try {
			stmt = conn.createStatement();
			
			return stmt.executeQuery( query);
			
		}
		catch ( Exception e ) {
			System.out.println( e );
			e.printStackTrace();
			return null;
		}
		
	}
	
	// IMPORTANT! closes so the db file does not stay locked
	public void close() {
		try {
			if(conn != null)
				conn.close();
			System.out.println("connection closed");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}