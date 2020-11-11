package app.Events;

import app.Ingredient;
import javafx.event.Event;
import javafx.event.EventType;

public class DeleteIngredientEvent extends Event { 
	public static final EventType<DeleteIngredientEvent> DELETE_INGREDIENT =
			new EventType<DeleteIngredientEvent>(Event.ANY, "DELETE_INGREDIENT");
	
	private Ingredient ingredient;
	
	public DeleteIngredientEvent(Ingredient ingredient)
	{
		super(DELETE_INGREDIENT);
		this.ingredient = ingredient;
	}
	
	
	public Ingredient getIngredient() { return ingredient; }
}
