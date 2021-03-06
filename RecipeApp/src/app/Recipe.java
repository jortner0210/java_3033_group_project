package app;

import java.util.ArrayList;
import java.util.List;

// stores data for a single recipe
public class Recipe {

	public int id;
	public String name;
	public String write_up;
	String prepTime;
	String cookTime;
	String totalTime;
	String yield;
	public ArrayList<Ingredient> ingredients = new ArrayList<>();
	
	public Recipe() {
		
	}
	
	public Recipe( int id, String name, 
				   String write_up, String prep, 
				   String cook, String total_time, 
				   String yield, ArrayList<Ingredient> ingredients ) {
		// set instance variables
		this.id = id;
		this.name = name;
		this.write_up = write_up;
		this.prepTime = prep;
		this.cookTime = cook;
		this.totalTime = total_time;
		this.yield = yield;
		
		// instantiate ingredient list with new ingredient objects
		for ( Ingredient i : ingredients ) {
			this.ingredients.add( new Ingredient( i.id, i.qty, i.metric, i.name, i.category ) );
		}
	}
	
	// adds ingredient
	public int add_ingredient( Ingredient in ) {
		ingredients.add( in );
		return ingredients.size();
	}
	
	public String toString() {
		return "id="+id+",name="+name+",write_up="+write_up+",ingredient_count="+ingredients.size()+"\n";
	}
	
	// returns list of ingredient names
	public List<String> get_ingredients() {
		ArrayList<String> ingredientsStrings = new ArrayList<String>();
		for (Ingredient i : ingredients )
		{
			ingredientsStrings.add(i.toString());
		}
		return ingredientsStrings;
	}
	
}
