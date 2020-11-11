package app.Events;

import app.Recipe;
import javafx.event.Event;
import javafx.event.EventType;

public class InsertRecipeToDatabaseEvent extends Event {
	public static final EventType<InsertRecipeToDatabaseEvent> INSERT_RECIPE =
			new EventType<>(Event.ANY, "INSERT_RECIPE");
	
	private Recipe recipe;
	
	public InsertRecipeToDatabaseEvent(Recipe recipe) {
		super(INSERT_RECIPE);
		this.recipe = recipe;
	}
	public Recipe getRecipe() { return recipe; }
	
}
