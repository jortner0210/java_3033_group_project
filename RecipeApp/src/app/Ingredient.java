package app;

public class Ingredient {

	public int id;
	public float qty;
	public String metric;
	public String name;
	public String category;
	
	public Ingredient() {
		
	}
	
	public Ingredient( int id, float qty, String metric, String name, String category ) {
		this.id = id;
		this.qty = qty;
		this.metric = metric;
		this.name = name;
		this.category = category;
	}
	
	public String toString() {
		return "id="+id+",qty="+qty+",metric="+metric+",name="+name+",category="+category+"\n";
	}
	
}