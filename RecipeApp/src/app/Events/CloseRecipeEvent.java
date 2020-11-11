package app.Events;

import app.Recipe;
import javafx.event.Event;
import javafx.event.EventType;

public class CloseRecipeEvent extends Event {
	public static final EventType<CloseRecipeEvent> CLOSE_RECIPE =
			new EventType<>(Event.ANY, "CLOSE_RECIPE");
	
	private Recipe recipe;
	
	public CloseRecipeEvent(Recipe recipe) {
		super(CLOSE_RECIPE);
		this.recipe = recipe;
	}
	
	public Recipe getRecipe() { return this.recipe; }
}
