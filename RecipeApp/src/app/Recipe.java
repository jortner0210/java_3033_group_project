package app;

import java.util.ArrayList;

public class Recipe {

	public int id;
	public String name;
	public String write_up;
	public ArrayList<Ingredient> ingredients = new ArrayList<>();
	
	public Recipe() {
		
	}
	
	public Recipe( int id, String name, String write_up, ArrayList<Ingredient> ingredients ) {
		this.id = id;
		this.name = name;
		this.write_up = write_up;
		for ( Ingredient i : ingredients ) {
			this.ingredients.add( new Ingredient( i.id, i.qty, i.metric, i.name, i.category ) );
		}
	}
	
	public int add_ingredient( Ingredient in ) {
		ingredients.add( in );
		return ingredients.size();
	}
	
	public String toString() {
		return "id="+id+",name="+name+",write_up="+write_up+",ingredient_count="+ingredients.size()+"\n";
	}
	
}
