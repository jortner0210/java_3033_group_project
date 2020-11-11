package app.Events;

import javafx.event.Event;
import javafx.event.EventType;

public class AddRecipeEvent  extends Event {
		
		public static final EventType<AddRecipeEvent> ADD_RECIPE =
				new EventType<>(Event.ANY, "ADD_RECIPE");
		
		public AddRecipeEvent() {
			super(ADD_RECIPE);
		}
}

