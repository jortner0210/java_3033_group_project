package app;

import java.sql.*;
import java.util.*;

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
	public int insert_recipe( String name, String write_up, ArrayList<Ingredient> ingredients ) {
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
		for ( Ingredient i : ingredients ) {
			i.id = insert_ingredient( i );
		}
		// insert to contains table
		insert_contains( rid, ingredients );
		
		return 0;
	}
	
	/*
	 * Insert ingredients into contains tables
	 */
	public int insert_contains( int recipe_id, ArrayList<Ingredient> ingredients ) {
		try {
			//for ( int i = 0; i < ingredients.length; i++ ) {
			for ( Ingredient i : ingredients ) {
				String insert_contains_query = "INSERT INTO contains ( rid, inid, qty, metric ) Values( ?, ?, ?, ? )";
				PreparedStatement prep_stmt = db.get_connection().prepareStatement( insert_contains_query );
				prep_stmt.setInt( 1, recipe_id );
				prep_stmt.setInt( 2, i.id );
				prep_stmt.setFloat( 3, i.qty );
				prep_stmt.setString( 4, i.metric );
				prep_stmt.execute();
			}
		}
		catch ( Exception e ) {
			System.out.println( e );
		}
		
		return 0;
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
	
	/*
	 * Returns a list of ingredients in desire recipe
	 */
	public ArrayList<Ingredient> get_ingredients( int rid ) {
		ArrayList<Ingredient> ingredients = new ArrayList<>();
		try {
			String contains_select = "SELECT * FROM contains WHERE rid=" + rid;
			ResultSet contains_result = db.execute_query( contains_select );
			
			while( contains_result.next() ) {
				// add contains data
				Ingredient curr_ingredient = new Ingredient();
				curr_ingredient.id     = contains_result.getInt( "inid" );
				curr_ingredient.qty	   = contains_result.getFloat( "qty" );
				curr_ingredient.metric = contains_result.getString( "metric" );
				// add ingredient data
				String ingredient_select = "SELECT * FROM ingredient WHERE inid=" + curr_ingredient.id;
				ResultSet ingredient_result = db.execute_query( ingredient_select );
				
				curr_ingredient.name	 = ingredient_result.getString( "name" );
				curr_ingredient.category = ingredient_result.getString( "category" );
				
				ingredients.add( curr_ingredient );
				
				System.out.println( curr_ingredient );
			}
		}
		catch ( Exception e ) {
			System.out.println( e );
		}
		return ingredients;
	}
	
	
	public Recipe get_recipe( int id ) {
		Recipe recipe = new Recipe();
		try {
			String recipe_select = "SELECT * FROM recipe WHERE rid=" + id;
			ResultSet result = db.execute_query( recipe_select );
			//Retrieve by column name
	        recipe.id		= id;
			recipe.name 	= result.getString( "name" );
			recipe.write_up = result.getString( "write_up" );
			recipe.ingredients = get_ingredients( id );
	        //Display values
	        System.out.print( "ID: " + id );
	        System.out.print( ", Name: " + recipe.name );
	        System.out.print( ", write_up: " + recipe.write_up + "\n" );			
		}
		catch ( Exception e ) {
			System.out.println( e );
			return null;
		}
		return recipe;
	}
	

}