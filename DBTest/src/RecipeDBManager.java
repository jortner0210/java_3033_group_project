import java.sql.*;

public class RecipeDBManager {
	
	DBManager db;
	
	public RecipeDBManager( String db_name ) {
		db = new DBManager();
		db.connect_db( db_name );
	}
	
	/*
	 * insert ingredient into ingredient table
	 */
	public int insert_ingredient( Ingredient ingredient ) {
		int in_id = -1;
		String ingredient_check = "SELECT * FROM ingredient WHERE name='";
		
		ResultSet result = db.execute_query( ingredient_check  + ingredient.name + "'" );
		try {
			// if ingredient is in data base, retrieve its id
			if ( !result.isClosed() ) {
				in_id = result.getInt( "inid" );
			}
			// if ingredient is not in database, insert it and retrieve the id
			else {
				//db.execute_update( ingredient_to_sql( ingredient ) );
				String insert_ingredient_query = "INSERT INTO ingredient ( name, category ) Values( ?, ? )";
				PreparedStatement prep_stmt = db.get_connection().prepareStatement( insert_ingredient_query );
				prep_stmt.setString( 1, ingredient.name );
				prep_stmt.setString( 2, ingredient.category );
				prep_stmt.execute();
				result = db.execute_query( ingredient_check  + ingredient.name + "'" );
				in_id = result.getInt( "inid" );
			}
		}
		catch ( SQLException e ){
			System.out.println( e );
		}
		return in_id;
	}
	
	/*
	 * insert recipe into table and update ingredients and contains tables
	 */
	public int insert_recipe( String name, String write_up, Ingredient ingredients[] ) {
		// insert to recipe
		String recipe_check = "SELECT Count(*) count FROM recipe";
		int rid = -1;
		try {
			String insert_recipe_query = "INSERT INTO recipe ( name, write_up ) Values( ?, ? )";
			PreparedStatement prep_stmt = db.get_connection().prepareStatement( insert_recipe_query );
			prep_stmt.setString( 1, name );
			prep_stmt.setString( 2, write_up );
			prep_stmt.execute();
			ResultSet result = db.execute_query( recipe_check );
			rid = result.getInt( "count" );
			
		}
		catch ( Exception e ) {
			System.out.println( e );
		}
		
		// insert to ingredients and update ids
		for ( int i = 0; i < ingredients.length; i++ ) {
			ingredients[i].id = insert_ingredient( ingredients[i] );
		}
		// insert to contains table
		insert_contains( rid, ingredients );
		
		return 0;
	}
	
	/*
	 * Insert ingredients into contains tables
	 */
	public int insert_contains( int recipe_id, Ingredient ingredients[] ) {
		try {
			for ( int i = 0; i < ingredients.length; i++ ) {
				String insert_contains_query = "INSERT INTO contains ( rid, inid, qty, metric ) Values( ?, ?, ?, ? )";
				PreparedStatement prep_stmt = db.get_connection().prepareStatement( insert_contains_query );
				prep_stmt.setInt( 1, recipe_id );
				prep_stmt.setInt( 2, ingredients[i].id );
				prep_stmt.setInt( 3, ingredients[i].qty );
				prep_stmt.setString( 4, ingredients[i].metric );
				prep_stmt.execute();
			}
		}
		catch ( Exception e ) {
			System.out.println( e );
		}
		
		return 0;
	}
	
	/*
	 * Returns a list of ingredients in desire recipe
	 */
	public Ingredient[] get_ingredients( int id, String recipe_name ) {
		return new Ingredient[1];
	}
	
	/*
	 * Returns a list of all recipes with name
	 */
	public Recipe[] get_recipe( String name ) {
		Recipe[] recipe_results = new Recipe[1];
		int i = 0;
		try {
			String recipe_count = "SELECT Count(*) count FROM recipe";
			ResultSet result = db.execute_query( recipe_count );
			int count = result.getInt( "count" );
			if ( count > 1 ) {
				recipe_results = new Recipe[count];
			}
			String recipe_select = "SELECT * FROM recipe";
			result = db.execute_query( recipe_select );
			while( result.next() ) {
		         //Retrieve by column name
		         int id  	     = result.getInt( "id" );
		         String r_name   = result.getString( "name" );
		         String write_up = result.getString( "write_up" );
		         
		         recipe_results[i].id = id;
		         recipe_results[i].write_up = write_up;
		         recipe_results[i].name = r_name;
		         
		         //Display values
		         System.out.print( "ID: " + id );
		         System.out.print( ", Name: " + name );
		         System.out.print( ", write_up: " + write_up );
		         
		         // TO DO : QUERY CONTAINS AND INGREDIENT
		         
		         i++;
			}
			
		}
		catch ( Exception e ) {
			System.out.println( e );
		}
		return recipe_results;
	}
	

}