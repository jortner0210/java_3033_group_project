package app.Events;

import app.Ingredient;
import javafx.event.Event;
import javafx.event.EventType;

public class AddIngredientEvent extends Event {

	public static final EventType<AddIngredientEvent> ADD_INGREDIENT =
			new EventType<>(Event.ANY, "ADD_INGREDIENT");
	
	private Ingredient ingredient;
	
	public AddIngredientEvent(Ingredient ingredient) {
		super(ADD_INGREDIENT);
		this.ingredient = ingredient;
	}
	
	public Ingredient getIngredient() { return ingredient; }
	
}
