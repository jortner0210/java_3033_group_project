import java.sql.*;

public class DBManager {
	
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

}
