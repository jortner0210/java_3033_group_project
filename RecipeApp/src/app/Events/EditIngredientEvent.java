package app.Events;

import app.Ingredient;
import javafx.event.Event;
import javafx.event.EventType;

public class EditIngredientEvent extends Event {

	public static final EventType<EditIngredientEvent> EDIT_INGREDIENT =
			new EventType<EditIngredientEvent>(Event.ANY, "EDIT_INGREDIENT");
	
	private Ingredient ingredient;
	
	public EditIngredientEvent(Ingredient ingredient) {
		super(EDIT_INGREDIENT);
		this.ingredient = ingredient;
	}
	
	public Ingredient getIngredient() { return ingredient; }
}
