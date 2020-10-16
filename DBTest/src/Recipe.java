
public class Recipe {

	public int id;
	public String name;
	public String write_up;
	public Ingredient[] ingredients = new Ingredient[100];
	public int ingredient_count = 0;
	
	public Recipe( int id, String name, String write_up, Ingredient[] ingredients ) {
		this.id = id;
		this.name = name;
		this.write_up = write_up;
		for ( int i = 0; i < this.ingredients.length; i++ ) {
			add_ingredient( ingredients[i] );
		}
	}
	
	public int add_ingredient( Ingredient in ) {
		ingredients[ingredient_count].id       = in.id;
		ingredients[ingredient_count].qty      = in.qty;
		ingredients[ingredient_count].metric   = in.metric;
		ingredients[ingredient_count].name     = in.name;
		ingredients[ingredient_count].category = in.category;
		ingredient_count += 1;
		return ingredient_count;
	}
	
}
