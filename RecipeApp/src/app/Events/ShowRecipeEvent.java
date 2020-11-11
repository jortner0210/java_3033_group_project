package app.Events;

import app.Recipe;
import javafx.event.Event;
import javafx.event.EventType;

public class ShowRecipeEvent  extends Event {
		
		public static final EventType<ShowRecipeEvent> SHOW_RECIPE =
				new EventType<>(Event.ANY, "SHOW_RECIPE");
		
		private Recipe recipe;
		
		public ShowRecipeEvent(Recipe recipe) {
			super(SHOW_RECIPE);
			this.recipe = recipe;
		}
		
		public Recipe getRecipe() { return this.recipe; }
}

