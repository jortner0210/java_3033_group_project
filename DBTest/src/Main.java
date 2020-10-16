import java.sql.*;

public class Main {

	public static void main(String[] args) {
		/*
		DBManager db = new DBManager();
		db.connect_db( "db_test" );
		// create table
		String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real\n"
                + ");";
		System.out.println( "Create table: " + db.execute( sql ) );
		
		// delete table
		String delete_sql = "DROP TABLE warehouses";
		//System.out.println( "Drop table: " + db.execute_update( delete_sql ) );
		
		// insert data
		String insert_sql = "INSERT INTO warehouses " +
							"VALUES( 1, 'name field', 0.001 )";
		System.out.println( "Insert record: " + db.execute_update( insert_sql ) );
		
		// query data
		String sql_query = "SELECT * FROM warehouses";
		ResultSet result = db.execute_query( sql_query );
		if ( result != null ) {
			try {
				while( result.next() ) {
			         //Retrieve by column name
			         int id  	 = result.getInt( "id" );
			         String name = result.getString( "name" );
			         float cap   = result.getFloat( "capacity" );

			         //Display values
			         System.out.print( "ID: " + id );
			         System.out.print( ", Name: " + name );
			         System.out.print( ", Cap: " + cap );
				}
			}
			catch( Exception e ) {
				System.out.println( e );
			}
		}
		*/
		Ingredient ingredients[] = {
			new Ingredient( -1, 10, "ounces", "flower", "baking" ),
			new Ingredient( -1, 1, "cup", "olive oil", "oil" ),
			new Ingredient( -1, 1, "cup", "ground pork", "meat" )
		};
		RecipeDBManager db = new RecipeDBManager( "db_test" );
		
		db.insert_recipe( "r_name", "this is a write up", ingredients );
		
	}

}
